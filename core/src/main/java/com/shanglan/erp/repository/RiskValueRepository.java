package com.shanglan.erp.repository;

import com.shanglan.erp.entity.RiskItem;
import com.shanglan.erp.entity.RiskValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RiskValueRepository extends JpaRepository<RiskValue,Integer>,JpaSpecificationExecutor<RiskValue> {


    Page<RiskValue> findAll(Pageable pageable);

}
