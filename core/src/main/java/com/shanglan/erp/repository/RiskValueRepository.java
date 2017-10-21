package com.shanglan.erp.repository;

import com.shanglan.erp.entity.RiskItem;
import com.shanglan.erp.entity.RiskValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RiskValueRepository extends JpaRepository<RiskValue,Integer>,JpaSpecificationExecutor<RiskValue> {


    Page<RiskValue> findAll(Pageable pageable);

    @Query("select count(*) from RiskValue r where r.riskAddr=?1 or r.riskLevel=?1 or r.riskNumber=?1")
    Integer findCountByRiskItem(RiskItem riskItem);

//    @Query("select r from RiskValue r where r.deleted=true")
//    Page<RiskValue> findAll(Specification<RiskValue> spec,Pageable pageable);

}
