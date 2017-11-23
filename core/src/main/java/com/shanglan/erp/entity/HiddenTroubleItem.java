package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cnoa_hiddentrouble_item")
public class HiddenTroubleItem extends BaseEntity{
    private static final long serialVersionUID = -8388601211574305265L;

    private Integer orderNumber;    //序号
    private String checkAddress;     //排查地点
    private LocalDate checkTime;     //排查时间
    private String checkPerson;     //排查人员
    @Column(length = 3000)
    private String content;     //存在的隐患及问题
    private String type;     //类别
    private String level;     //等级
    @Column(length = 3000)
    private String actions;     //整改措施
    private String responsiblePerson;     //责任单位/责任人
    private String controllingMoney;     //资金
    private String timeLimit;     //时限
    @Column(length = 3000)
    private String managementPlan;     //预案
    private String handlePerson;     //督办部门/人员
    private LocalDate acceptanceTime;     //验收时间
    private String acceptancePeople;     //验收部门/人员
    private String result;     //整改结果


    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(String checkAddress) {
        this.checkAddress = checkAddress;
    }

    public LocalDate getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalDate checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getControllingMoney() {
        return controllingMoney;
    }

    public void setControllingMoney(String controllingMoney) {
        this.controllingMoney = controllingMoney;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getManagementPlan() {
        return managementPlan;
    }

    public void setManagementPlan(String managementPlan) {
        this.managementPlan = managementPlan;
    }

    public String getHandlePerson() {
        return handlePerson;
    }

    public void setHandlePerson(String handlePerson) {
        this.handlePerson = handlePerson;
    }

    public LocalDate getAcceptanceTime() {
        return acceptanceTime;
    }

    public void setAcceptanceTime(LocalDate acceptanceTime) {
        this.acceptanceTime = acceptanceTime;
    }

    public String getAcceptancePeople() {
        return acceptancePeople;
    }

    public void setAcceptancePeople(String acceptancePeople) {
        this.acceptancePeople = acceptancePeople;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
