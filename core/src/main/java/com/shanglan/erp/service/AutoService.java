package com.shanglan.erp.service;

import com.shanglan.erp.repository.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class AutoService {
    @Autowired
    private RiskRepository riskRepository;
    @Autowired
    private RiskService riskService;

    @PostConstruct
    public void initTable(){
//        riskRepository.truncateTable();
        riskService.initRiskConditions();
    }
}
