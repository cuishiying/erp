package com.shanglan.erp.repository;

import com.shanglan.erp.entity.AttClasses;
import com.shanglan.erp.entity.AttMgrJoinatt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttClassesRepository extends JpaRepository<AttClasses,Integer> {

}
