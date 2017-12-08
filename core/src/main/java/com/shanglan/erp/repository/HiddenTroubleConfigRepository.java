package com.shanglan.erp.repository;

import com.shanglan.erp.entity.HiddenTrouble;
import com.shanglan.erp.entity.HiddenTroubleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HiddenTroubleConfigRepository extends JpaRepository<HiddenTroubleConfig,Integer>,JpaSpecificationExecutor<HiddenTroubleConfig> {

}
