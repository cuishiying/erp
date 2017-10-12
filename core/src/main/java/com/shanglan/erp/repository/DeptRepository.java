package com.shanglan.erp.repository;

import com.shanglan.erp.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeptRepository extends JpaRepository<Dept,Integer> {

    @Query("SELECT d FROM Dept d WHERE d.fid > 0")
    List<Dept> findAll();
}
