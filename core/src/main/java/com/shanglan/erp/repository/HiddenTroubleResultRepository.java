package com.shanglan.erp.repository;

import com.shanglan.erp.entity.HiddenTroubleItem;
import com.shanglan.erp.entity.HiddenTroubleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HiddenTroubleResultRepository extends JpaRepository<HiddenTroubleResult,Integer>,JpaSpecificationExecutor<HiddenTroubleResult> {

}
