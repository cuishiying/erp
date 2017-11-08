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


    public void truncateTable(){
        riskRepository.truncateTable();
    }

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
            String[] names = new String[]{"类型","风险分级"};
            for (int i=0;i<2;i++){
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

    public Page<RiskValue> findRiskValues(RiskQueryDTO queryDTO,Pageable pageable){
        Specification<RiskValue> spec = this.getWhereClause(queryDTO);
        Page<RiskValue> all = riskValueRepository.findAll(spec,pageable);
        return all;
    }

    public RiskValue findRiskValueById(Integer id){
        RiskValue one = riskValueRepository.findOne(id);
        return one;
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
                        cb.like(root.<String>get("riskDept"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("handleResult"), "%" + queryVo.getKeyword().trim() + "%")));

            }
            //类型
            if(queryVo!=null&&queryVo.getRiskTypeId()!=null){
                Predicate riskTypeQuery = cb.equal(root.<Integer>get("riskType").get("id"),queryVo.getRiskTypeId());
                predicate.add(riskTypeQuery);
            }
            //风险分级
            if(queryVo!=null&&queryVo.getRiskLevelId()!=null){
                Predicate ownerQuery = cb.equal(root.<Integer>get("riskLevel").get("id"), queryVo.getRiskLevelId());
                predicate.add(ownerQuery);
            }

            //检查时间
//            if (queryVo!=null&&queryVo.getBeginDate() != null) {
//                LocalDateTime begin = LocalDateTime.of(queryVo.getBeginDate(), LocalTime.MIN);
//                Predicate dateQuery = cb.greaterThanOrEqualTo(root.<LocalDateTime>get("checkTime"), begin);
//                predicate.add(dateQuery);
//            }
//            if (queryVo!=null&&queryVo.getEndDate() != null) {
//                LocalDateTime end = LocalDateTime.of(queryVo.getEndDate(), LocalTime.MAX);
//                Predicate dateQuery = cb.lessThanOrEqualTo(root.<LocalDateTime>get("checkTime"), end);
//                predicate.add(dateQuery);
//            }
            Predicate deletedQuery = cb.equal(root.<Integer>get("deleted"),false);
            predicate.add(deletedQuery);

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
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
            Integer count = riskValueRepository.findCountByRiskItem(riskItem);
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

    /**
     * 保存风险数据
     * @return
     */
    public AjaxResponse saveRiskValue(Integer uid,String riskAddr,String riskDesc,Integer riskTypeId,String riskValue,Integer riskLevelId,String precaution,String riskAnalysis,String riskDept,String responsible,String handleTimeLimit,String handleMoney,String handleResult){


        RiskItem riskType = riskItemRepository.findOne(riskTypeId);
        RiskItem riskLevel = riskItemRepository.findOne(riskLevelId);
        RiskValue item = new RiskValue();

        item.setRiskAddr(riskAddr);
        item.setRiskDesc(riskDesc);
        item.setRiskType(riskType);
        item.setRiskValue(riskValue);
        item.setRiskLevel(riskLevel);
        item.setPrecaution(precaution);
        item.setRiskAnalysis(riskAnalysis);
        item.setRiskDept(riskDept);
        item.setResponsible(responsible);
        item.setHandleTimeLimit(handleTimeLimit);
        item.setHandleMoney(handleMoney);
        item.setHandleResult(handleResult);

        item.setPublishTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        if(null!=uid){
            User user = userService.findByUid(uid);
            item.setPublishUser(user);
        }else {
            item.setPublishUser(null);
        }
        riskValueRepository.save(item);
        return AjaxResponse.success();
    }

    /**
     * 更新风险数据
     * @param uid
     * @param riskValueId
     * @param handleResult
     * @return
     */
    public AjaxResponse updateRiskValue(Integer uid,Integer riskValueId,String riskAddr,String riskDesc,Integer riskTypeId,String riskValue,Integer riskLevelId,String precaution,String riskAnalysis,String riskDept,String responsible,String handleTimeLimit,String handleMoney,String handleResult){
        RiskValue item = riskValueRepository.findOne(riskValueId);
        RiskItem riskType = riskItemRepository.findOne(riskTypeId);
        RiskItem riskLevel = riskItemRepository.findOne(riskLevelId);

        item.setRiskAddr(riskAddr);
        item.setRiskDesc(riskDesc);
        item.setRiskType(riskType);
        item.setRiskValue(riskValue);
        item.setRiskLevel(riskLevel);
        item.setPrecaution(precaution);
        item.setRiskAnalysis(riskAnalysis);
        item.setRiskDept(riskDept);
        item.setResponsible(responsible);
        item.setHandleTimeLimit(handleTimeLimit);
        item.setHandleMoney(handleMoney);
        item.setHandleResult(handleResult);

        if(null!=uid){
            User user = userService.findByUid(uid);
            item.setHandleUser(user);
        }else {
            item.setHandleUser(null);
        }
        item.setUpdateTime(LocalDateTime.now());
        return AjaxResponse.success();
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
