package com.shanglan.erp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class RiskQueryDTO {

    private String keyword;
    private Integer riskTypeId; //危险因素
    private Integer riskLevelId;    //风险分级

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate beginDate;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate endDate;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getRiskTypeId() {
        return riskTypeId;
    }

    public void setRiskTypeId(Integer riskTypeId) {
        this.riskTypeId = riskTypeId;
    }

    public Integer  getRiskLevelId() {
        return riskLevelId;
    }

    public void setRiskLevelId(Integer riskLevelId) {
        this.riskLevelId = riskLevelId;
    }
}
