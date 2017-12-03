package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 管控措施项
 */
@Entity
@Table(name = "cnoa_risk_handle")
public class RiskHandle extends BaseEntity{

    private static final long serialVersionUID = -937254997956709023L;

    @Column(length = 3000)
    private String precaution;

    @Column(length = 3000)
    private String handleResult;

    private boolean warming;

    public String getPrecaution() {
        return precaution;
    }

    public void setPrecaution(String precaution) {
        this.precaution = precaution;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public boolean isWarming() {
        return warming;
    }

    public void setWarming(boolean warming) {
        this.warming = warming;
    }
}
