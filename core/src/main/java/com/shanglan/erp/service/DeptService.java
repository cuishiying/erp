package com.shanglan.erp.service;

import com.shanglan.erp.entity.Dept;
import com.shanglan.erp.repository.DeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 部门
 */
@Service
@Transactional
public class DeptService {
    @Autowired
    private DeptRepository deptRepository;


    public List<Dept> findAll(){
        List<Dept> all = deptRepository.findAll();
        return all;
    }

    public Dept findById(Integer id){
        Dept dept = deptRepository.findOne(id);
        return dept;
    }
 }
