package com.shanglan.erp.service;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.RiskQueryDTO;
import com.shanglan.erp.entity.Dept;
import com.shanglan.erp.entity.RiskCondition;
import com.shanglan.erp.entity.RiskItem;
import com.shanglan.erp.entity.RiskValue;
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
            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }

    public AjaxResponse saveRiskItem(Integer conditionId,String conditionValue){
        RiskCondition condition = riskConditionRepository.findOne(conditionId);
        RiskItem riskItem = new RiskItem();
        riskItem.setRiskCondition(condition);
        riskItem.setName(conditionValue);
        riskItemRepository.save(riskItem);
        return AjaxResponse.success();
    }

    public AjaxResponse saveRiskValue(Integer riskAddrId,String riskDesc,Integer riskLevelId,String precaution,Integer riskMkDeptId,Integer riskvalueId){
        RiskItem riskAddr = riskItemRepository.findOne(riskAddrId);
        RiskItem riskLevel = riskItemRepository.findOne(riskLevelId);
        RiskItem riskvalue = riskItemRepository.findOne(riskvalueId);
        Dept dept = deptRepository.findOne(riskMkDeptId);
        RiskValue item = new RiskValue();
        item.setPublishTime(LocalDateTime.now());
        item.setRiskAddr(riskAddr);
        item.setRiskDesc(riskDesc);
        item.setRiskLevel(riskLevel);
        item.setPrecaution(precaution);
        item.setRiskMkDept(dept);
        item.setRiskValue(riskvalue);
        riskValueRepository.save(item);
        return AjaxResponse.success();
    }

}
