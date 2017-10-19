package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 警报项
 */
@Entity
@Table(name = "cnoa_risk_value")
public class RiskValue extends BaseEntity{


    private static final long serialVersionUID = 1211747394022548863L;

    private String name;
    private String riskDesc;//风险描述
    private String precaution;//管控措施
    private LocalDateTime checkTime;//检查时间
    private LocalDateTime publishTime;//发布时间
    private LocalDateTime handleTime;//处理时间
    private String riskValue;//风险分析
    private String handleResult;//管控措施执行情况


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User publishUser;//发布人

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User handleUser;//处理人

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private RiskItem riskLevel;//风险等级

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private RiskItem riskAddr;//风险地点

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Dept riskMkDept;//管控部门


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRiskDesc() {
        return riskDesc;
    }

    public void setRiskDesc(String riskDesc) {
        this.riskDesc = riskDesc;
    }

    public String getPrecaution() {
        return precaution;
    }

    public void setPrecaution(String precaution) {
        this.precaution = precaution;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public LocalDateTime getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(LocalDateTime handleTime) {
        this.handleTime = handleTime;
    }

    public User getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(User publishUser) {
        this.publishUser = publishUser;
    }

    public User getHandleUser() {
        return handleUser;
    }

    public void setHandleUser(User handleUser) {
        this.handleUser = handleUser;
    }

    public RiskItem getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskItem riskLevel) {
        this.riskLevel = riskLevel;
    }

    public RiskItem getRiskAddr() {
        return riskAddr;
    }

    public void setRiskAddr(RiskItem riskAddr) {
        this.riskAddr = riskAddr;
    }

    public Dept getRiskMkDept() {
        return riskMkDept;
    }

    public void setRiskMkDept(Dept riskMkDept) {
        this.riskMkDept = riskMkDept;
    }

    public String getRiskValue() {
        return riskValue;
    }

    public void setRiskValue(String riskValue) {
        this.riskValue = riskValue;
    }

    public LocalDateTime getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }
}
