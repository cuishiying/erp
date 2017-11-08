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
    private String riskAddr;//风险点
    private String riskDesc;//风险描述
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private RiskItem riskType;//风险类型
    private String riskValue;//风险值
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private RiskItem riskLevel;//风险分级
    private String precaution;//管控措施
    private String riskAnalysis;//风险分析
    private String riskDept;//责任部门
    private String responsible;//责任人
    private String handleTimeLimit;//治理时限
    private String handleMoney;//治理资金
    private String handleResult;//管控措施执行情况

    private LocalDateTime publishTime;//发布时间
    private LocalDateTime updateTime;//发布时间
//    private LocalDateTime checkTime;//检查时间
//    private LocalDateTime handleTime;//处理时间

    private boolean deleted;//逻辑删除，默认false

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User publishUser;//发布人

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User handleUser;//处理人

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User deleteUser;//删除人

    public String getRiskAddr() {
        return riskAddr;
    }

    public void setRiskAddr(String riskAddr) {
        this.riskAddr = riskAddr;
    }

    public String getRiskDesc() {
        return riskDesc;
    }

    public void setRiskDesc(String riskDesc) {
        this.riskDesc = riskDesc;
    }

    public RiskItem getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskItem riskType) {
        this.riskType = riskType;
    }

    public String getRiskValue() {
        return riskValue;
    }

    public void setRiskValue(String riskValue) {
        this.riskValue = riskValue;
    }

    public RiskItem getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskItem riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getPrecaution() {
        return precaution;
    }

    public void setPrecaution(String precaution) {
        this.precaution = precaution;
    }

    public String getRiskAnalysis() {
        return riskAnalysis;
    }

    public void setRiskAnalysis(String riskAnalysis) {
        this.riskAnalysis = riskAnalysis;
    }

    public String getRiskDept() {
        return riskDept;
    }

    public void setRiskDept(String riskDept) {
        this.riskDept = riskDept;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getHandleTimeLimit() {
        return handleTimeLimit;
    }

    public void setHandleTimeLimit(String handleTimeLimit) {
        this.handleTimeLimit = handleTimeLimit;
    }

    public String getHandleMoney() {
        return handleMoney;
    }

    public void setHandleMoney(String handleMoney) {
        this.handleMoney = handleMoney;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public User getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(User deleteUser) {
        this.deleteUser = deleteUser;
    }
}
