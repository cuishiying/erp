package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 风险值
 */
@Entity
@Table(name = "cnoa_risk_value")
public class RiskValue extends BaseEntity{


    private static final long serialVersionUID = 1211747394022548863L;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private RiskItem riskElement;//风险因素
    private String riskAddr;//风险地点
    private String riskDesc;//风险描述
    private String riskValue;//风险值
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private RiskItem riskLevel;//风险分级
    private String riskAnalysis;//风险分析
    private String riskDept;//责任部门
    private String responsible;//责任人
    private String branchLeader;//矿分管领导
    private String orgLeader;//组织领导
    private String handleTimeLimit;//治理时限
    private String handleMoney;//治理资金

    @OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL, orphanRemoval=true)
    private List<RiskHandle> riskHandles;
    private boolean hasWarmingItem;

    private LocalDateTime publishTime;//发布时间
    private LocalDateTime updateTime;//更新时间

    private boolean deleted;//逻辑删除，默认false

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User publishUser;//发布人

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User handleUser;//处理人

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User deleteUser;//删除人


    public RiskItem getRiskElement() {
        return riskElement;
    }

    public void setRiskElement(RiskItem riskElement) {
        this.riskElement = riskElement;
    }

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

    public List<RiskHandle> getRiskHandles() {
        return riskHandles;
    }

    public void setRiskHandles(List<RiskHandle> riskHandles) {
        this.riskHandles = riskHandles;
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

    public String getBranchLeader() {
        return branchLeader;
    }

    public void setBranchLeader(String branchLeader) {
        this.branchLeader = branchLeader;
    }

    public String getOrgLeader() {
        return orgLeader;
    }

    public void setOrgLeader(String orgLeader) {
        this.orgLeader = orgLeader;
    }

    public boolean isHasWarmingItem() {
        return hasWarmingItem;
    }

    public void setHasWarmingItem(boolean hasWarmingItem) {
        this.hasWarmingItem = hasWarmingItem;
    }
}
