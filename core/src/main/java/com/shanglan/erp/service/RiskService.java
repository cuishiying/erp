package com.shanglan.erp.service;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.RiskQueryDTO;
import com.shanglan.erp.entity.*;
import com.shanglan.erp.repository.DeptRepository;
import com.shanglan.erp.repository.RiskConditionRepository;
import com.shanglan.erp.repository.RiskItemRepository;
import com.shanglan.erp.repository.RiskValueRepository;
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


    public RiskCondition findById(Integer id){
        RiskCondition one = riskConditionRepository.findOne(id);
        return one;
    }

    public List<RiskCondition> findRiskConditions(){
        List<RiskCondition> all = riskConditionRepository.findAll();
        return all;
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

            if(queryVo!=null&&queryVo.getRiskAddrId()!=null){
                Predicate ownerQuery = cb.equal(root.<Integer>get("riskAddr"), riskItemRepository.findOne(queryVo.getRiskAddrId()));
                predicate.add(ownerQuery);
            }
            if(queryVo!=null&&queryVo.getRiskLevelId()!=null){
                Predicate ownerQuery = cb.equal(root.<Integer>get("riskLevel"), riskItemRepository.findOne(queryVo.getRiskLevelId()));
                predicate.add(ownerQuery);
            }
            if(queryVo!=null&&queryVo.getRiskValueId()!=null){
                Predicate ownerQuery = cb.equal(root.<Integer>get("riskValue"),riskItemRepository.findOne(queryVo.getRiskValueId()));
                predicate.add(ownerQuery);
            }
            if(queryVo!=null&&queryVo.getDeptId()!=null){
                Predicate ownerQuery = cb.equal(root.<Integer>get("riskMkDept"),deptRepository.findOne(queryVo.getDeptId()));
                predicate.add(ownerQuery);
            }
            //检查时间
            if (queryVo!=null&&queryVo.getBeginDate() != null) {
                LocalDateTime begin = LocalDateTime.of(queryVo.getBeginDate(), LocalTime.MIN);
                Predicate dateQuery = cb.greaterThanOrEqualTo(root.<LocalDateTime>get("checkTime"), begin);
                predicate.add(dateQuery);
            }
            if (queryVo!=null&&queryVo.getEndDate() != null) {
                LocalDateTime end = LocalDateTime.of(queryVo.getEndDate(), LocalTime.MAX);
                Predicate dateQuery = cb.lessThanOrEqualTo(root.<LocalDateTime>get("checkTime"), end);
                predicate.add(dateQuery);
            }
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
     * @param uid
     * @param riskAddrId
     * @param riskDesc
     * @param riskLevelId
     * @param precaution
     * @param riskMkDeptId
     * @param riskvalue
     * @param checkTime
     * @param riskNumberId
     * @param responsible
     * @return
     */
    public AjaxResponse saveRiskValue(Integer uid,Integer riskAddrId,String riskDesc,Integer riskLevelId,String precaution,Integer riskMkDeptId,String riskvalue,LocalDateTime checkTime,Integer riskNumberId,String responsible){


        RiskItem riskAddr = riskItemRepository.findOne(riskAddrId);
        RiskItem riskLevel = riskItemRepository.findOne(riskLevelId);
        RiskItem riskNumber = riskItemRepository.findOne(riskNumberId);
        Dept dept = deptRepository.findOne(riskMkDeptId);
        RiskValue item = new RiskValue();
        item.setPublishTime(LocalDateTime.now());
        item.setRiskAddr(riskAddr);
        item.setRiskDesc(riskDesc);
        item.setRiskLevel(riskLevel);
        item.setPrecaution(precaution);
        item.setRiskMkDept(dept);
        item.setRiskValue(riskvalue);
        item.setCheckTime(checkTime);
        item.setRiskNumber(riskNumber);
        item.setResponsible(responsible);
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
     * @param valueId
     * @param handleResult
     * @return
     */
    public AjaxResponse updateRiskValue(Integer uid,Integer valueId,String handleResult){
        RiskValue riskValue = riskValueRepository.findOne(valueId);
        if(null!=uid){
            User user = userService.findByUid(uid);
            riskValue.setHandleUser(user);
        }else {
            riskValue.setHandleUser(null);
        }
        riskValue.setHandleResult(handleResult);
        riskValue.setHandleTime(LocalDateTime.now());
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
