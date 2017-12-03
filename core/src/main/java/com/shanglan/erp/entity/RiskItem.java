package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 配置项
 */
@Entity
@Table(name = "cnoa_risk_item")
public class RiskItem extends BaseEntity{


    private static final long serialVersionUID = 1211747394022548863L;

    private String name;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private RiskCondition riskCondition;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RiskCondition getRiskCondition() {
        return riskCondition;
    }

    public void setRiskCondition(RiskCondition riskCondition) {
        this.riskCondition = riskCondition;
    }
}
