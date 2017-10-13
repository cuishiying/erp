package com.shanglan.erp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class RiskQueryDTO {
    private Integer riskAddrId;
    private Integer riskLevelId;
    private Integer riskValueId;
    private Integer deptId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate beginDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    public Integer getRiskAddrId() {
        return riskAddrId;
    }

    public void setRiskAddrId(Integer riskAddrId) {
        this.riskAddrId = riskAddrId;
    }

    public Integer getRiskLevelId() {
        return riskLevelId;
    }

    public void setRiskLevelId(Integer riskLevelId) {
        this.riskLevelId = riskLevelId;
    }

    public Integer getRiskValueId() {
        return riskValueId;
    }

    public void setRiskValueId(Integer riskValueId) {
        this.riskValueId = riskValueId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
