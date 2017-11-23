package com.shanglan.erp.repository;

import com.shanglan.erp.entity.AttClasses;
import com.shanglan.erp.entity.HiddenTrouble;
import com.shanglan.erp.entity.HiddenTroubleItem;
import com.shanglan.erp.entity.RiskValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HiddenTroubleRepository extends JpaRepository<HiddenTrouble,Integer>,JpaSpecificationExecutor<HiddenTrouble> {

}
