package com.shanglan.erp.repository;

import com.shanglan.erp.entity.RiskCondition;
import com.shanglan.erp.entity.RiskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RiskItemRepository extends JpaRepository<RiskItem,Integer>{

    @Query("SELECT c FROM RiskItem AS c ORDER BY c.riskCondition")
    List<RiskItem> findAll();

    @Query("SELECT c FROM RiskItem AS c WHERE c.riskCondition.id = ?1")
    List<RiskItem> findAll(Integer riskConditionId);

    @Query("SELECT c FROM RiskItem AS c WHERE c.riskCondition.name = ?1")
    List<RiskItem> findAll(String riskConditionName);

//    @Query("select count(1) from RiskValue r where r.riskLevel.id=?1")
//    Integer findCountByRiskItem(Integer id);
}
