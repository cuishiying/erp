package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cnoa_hiddentrouble_item")
public class HiddenTroubleItem extends BaseEntity{
    private static final long serialVersionUID = -8388601211574305265L;

    private String majors;  //专业
    private String grade;  //隐患等级
    private String content;  //隐患内容
    private String actions;  //整改措施
    private String undertakeUnit;  //承办单位
    private LocalDate finishTime;  //完成时间
    private String responsiblePerson;  //责任人
    private String controllingMoney;  //治理资金
    private String managementPlan;  //治理预案
    private LocalDate acceptanceTime;  //验收时间
    private String acceptancepeople;  //验收人

    private String pitName; //矿井名称
    private String leader;   //分矿领导签字
    private String rummager;   //检查人
    private String organizationMan;   //组织人
    private String entryPerson;   //填表人
    private LocalDate createTime; //建表时间

    private Integer hiddentroubleId;

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getUndertakeUnit() {
        return undertakeUnit;
    }

    public void setUndertakeUnit(String undertakeUnit) {
        this.undertakeUnit = undertakeUnit;
    }

    public LocalDate getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDate finishTime) {
        this.finishTime = finishTime;
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

    public String getManagementPlan() {
        return managementPlan;
    }

    public void setManagementPlan(String managementPlan) {
        this.managementPlan = managementPlan;
    }

    public LocalDate getAcceptanceTime() {
        return acceptanceTime;
    }

    public void setAcceptanceTime(LocalDate acceptanceTime) {
        this.acceptanceTime = acceptanceTime;
    }

    public String getAcceptancepeople() {
        return acceptancepeople;
    }

    public void setAcceptancepeople(String acceptancepeople) {
        this.acceptancepeople = acceptancepeople;
    }

    public String getPitName() {
        return pitName;
    }

    public void setPitName(String pitName) {
        this.pitName = pitName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getRummager() {
        return rummager;
    }

    public void setRummager(String rummager) {
        this.rummager = rummager;
    }

    public String getOrganizationMan() {
        return organizationMan;
    }

    public void setOrganizationMan(String organizationMan) {
        this.organizationMan = organizationMan;
    }

    public String getEntryPerson() {
        return entryPerson;
    }

    public void setEntryPerson(String entryPerson) {
        this.entryPerson = entryPerson;
    }

    public Integer getHiddentroubleId() {
        return hiddentroubleId;
    }

    public void setHiddentroubleId(Integer hiddentroubleId) {
        this.hiddentroubleId = hiddentroubleId;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }
}
