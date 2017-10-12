package com.shanglan.erp.dto;

public class RiskQueryDTO {
    private Integer riskAddrId;
    private Integer riskLevelId;
    private Integer riskValueId;
    private Integer deptId;

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
}
