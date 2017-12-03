package com.shanglan.erp.service;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.RiskQueryDTO;
import com.shanglan.erp.entity.*;
import com.shanglan.erp.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RiskService {

    @Autowired
    private RiskConditionRepository riskConditionRepository;
    @Autowired
    private RiskItemRepository riskItemRepository;
    @Autowired
    private DeptRepository deptRepository;
    @Autowired
    private RiskValueRepository riskValueRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RiskRepository riskRepository;


    //******************************初始化表格****************************
    public void truncateTable(){
        riskRepository.truncateTable();
    }

    //******************************配置项****************************
    public RiskCondition findById(Integer id){
        RiskCondition one = riskConditionRepository.findOne(id);
        return one;
    }

    public List<RiskCondition> findRiskConditions(){
        List<RiskCondition> all = riskConditionRepository.findAll();
        return all;
    }

    public AjaxResponse initRiskConditions(){
        List<RiskCondition> all = riskConditionRepository.findAll();
        if (null!=all&&all.size()<=0){
            List<RiskCondition> list = new ArrayList<>();
            String[] names = new String[]{"风险分级"};
            for (int i=0;i<names.length;i++){
                RiskCondition r = new RiskCondition();
                r.setName(names[i]);
                list.add(r);
            }
            riskConditionRepository.save(list);
        }
        return AjaxResponse.success();
    }



    public List<RiskItem> findRiskItems(){
        List<RiskItem> all = riskItemRepository.findAll();
        return all;
    }

    public List<RiskItem> findRiskItems(String riskConditionName){
        List<RiskItem> all = riskItemRepository.findAll(riskConditionName);
        return all;
    }

    /**
     * 保存配置项
     * @param conditionId
     * @param conditionValue
     * @return
     */
    public AjaxResponse saveRiskItem(Integer conditionId,String conditionValue){
        RiskCondition condition = riskConditionRepository.findOne(conditionId);
        RiskItem riskItem = new RiskItem();
        riskItem.setRiskCondition(condition);
        riskItem.setName(conditionValue);
        riskItemRepository.save(riskItem);
        return AjaxResponse.success();
    }

    /**
     * 删除配置项
     * @param id
     * @return
     */
    public AjaxResponse deleteRiskItem(Integer id){
        try{
            RiskItem riskItem = riskItemRepository.findOne(id);
            Integer count = riskValueRepository.findCountByRiskItem(riskItem.getId());
            if(count<=0){
                riskItemRepository.delete(id);
            }else{
                return AjaxResponse.fail("该参数项正在使用,无法删除");
            }
        }catch (Exception e){
            return AjaxResponse.fail("删除出现错误");
        }
        return AjaxResponse.success();
    }

    //******************************风险管控****************************

    public Page<RiskValue> findRiskValues(RiskQueryDTO queryDTO,Pageable pageable){
        Specification<RiskValue> spec = this.getWhereClause(queryDTO);
        Page<RiskValue> page = riskValueRepository.findAll(spec,pageable);
        for (RiskValue r:page.getContent()) {
            r.setHasWarmingItem(false);
            for (RiskHandle h:r.getRiskHandles()) {
                if (h.isWarming()){
                    r.setHasWarmingItem(true);
                    break;
                }
            }
        }
        return page;
    }

    /**
     * 查询条件
     * @param queryVo
     * @return
     */
    private Specification<RiskValue> getWhereClause(RiskQueryDTO queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //关键词
            if(queryVo!=null&& StringUtils.isNotBlank(queryVo.getKeyword())){
                predicate.add(cb.or(cb.like(root.<String>get("riskAddr"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("riskElement"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("riskDesc"), "%" + queryVo.getKeyword().trim() + "%")));

            }
            //风险分级
            if(queryVo!=null&&queryVo.getRiskLevelId()!=null){
                Predicate ownerQuery = cb.equal(root.<Integer>get("riskLevel").get("id"), queryVo.getRiskLevelId());
                predicate.add(ownerQuery);
            }

            //检查时间
            Predicate deletedQuery = cb.equal(root.<Integer>get("deleted"),false);
            predicate.add(deletedQuery);

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }

    public RiskValue findRiskValueById(Integer id){
        RiskValue one = riskValueRepository.findOne(id);
        return one;
    }

    /**
     * 风险项增加
     * @param riskValue
     * @return
     */
    public AjaxResponse addRiskValue(Integer uid,RiskValue riskValue){
        try {
            riskValue.setPublishTime(LocalDateTime.now());
            riskValue.setUpdateTime(LocalDateTime.now());
            if(null!=uid){
                User user = userService.findByUid(uid);
                riskValue.setPublishUser(user);
            }else {
                riskValue.setPublishUser(null);
            }
            riskValueRepository.save(riskValue);
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.fail("保存失败");
        }
    }

    /**
     * 更新风险项信息
     * @param uid
     * @param r
     * @return
     */
    public AjaxResponse updateRiskValue(Integer uid,RiskValue r){
        try {
            RiskValue riskValue = riskValueRepository.findOne(r.getId());
            riskValue.setRiskElement(r.getRiskElement());
            riskValue.setRiskDesc(r.getRiskDesc());
            riskValue.setRiskAddr(r.getRiskAddr());
            riskValue.setRiskLevel(r.getRiskLevel());
            riskValue.setRiskDept(r.getRiskDept());
            riskValue.setResponsible(r.getResponsible());
            riskValue.setBranchLeader(r.getBranchLeader());
            riskValue.setOrgLeader(r.getOrgLeader());
            riskValue.setHandleTimeLimit(r.getHandleTimeLimit());

            riskValue.setUpdateTime(LocalDateTime.now());
            if(null!=uid){
                User user = userService.findByUid(uid);
                riskValue.setHandleUser(user);
            }else {
                riskValue.setHandleUser(null);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.fail("保存失败");
        }
    }
    /**
     * 逻辑删除风险值
     * @param valueId
     * @return
     */
    public AjaxResponse logicalDeletionRiskValue(Integer uid,Integer valueId){
        try {
            RiskValue riskValue = riskValueRepository.findOne(valueId);
            riskValue.setDeleted(true);
            if(null!=uid){
                User user = userService.findByUid(uid);
                riskValue.setDeleteUser(user);
            }else {
                riskValue.setDeleteUser(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            AjaxResponse.fail();
        }
        return AjaxResponse.success();
    }

    /**
     * 物理删除风险值
     * @param valueId
     * @return
     */
    public AjaxResponse physicsDeleteRiskValue(Integer uid,Integer valueId){
        try {
            riskValueRepository.delete(valueId);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.fail();
        }
        return AjaxResponse.success();
    }



}
