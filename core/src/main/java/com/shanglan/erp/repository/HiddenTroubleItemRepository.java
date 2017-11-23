package com.shanglan.erp.repository;

import com.shanglan.erp.entity.HiddenTroubleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HiddenTroubleItemRepository extends JpaRepository<HiddenTroubleItem,Integer>,JpaSpecificationExecutor<HiddenTroubleItem> {

}
