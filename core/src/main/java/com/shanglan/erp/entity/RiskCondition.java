package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 警报可设置参数
 */
@Entity
@Table(name = "cnoa_risk_condition")
public class RiskCondition extends BaseEntity{

    private static final long serialVersionUID = -8988984020093283306L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
