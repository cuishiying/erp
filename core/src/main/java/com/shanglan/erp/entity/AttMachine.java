package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cnoa_att_machine")
public class AttMachine extends BaseEntity{
    private static final long serialVersionUID = 1671914237852680146L;

    private Integer uid;
    @Column(name="enrollNum")
    private Integer enrollNum;
    private Long time;
    @Column(name="timeStr")
    private String timeStr;
    @Column(name="machineId")
    private Integer machineId;
    @Column(name="upToOA")
    private Integer upToOA;
    @Column(name="rSyncTime")
    private Long rSyncTime;
    @Column(name="isNew")
    private Integer isNew;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getEnrollNum() {
        return enrollNum;
    }

    public void setEnrollNum(Integer enrollNum) {
        this.enrollNum = enrollNum;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Integer getUpToOA() {
        return upToOA;
    }

    public void setUpToOA(Integer upToOA) {
        this.upToOA = upToOA;
    }

    public Long getrSyncTime() {
        return rSyncTime;
    }

    public void setrSyncTime(Long rSyncTime) {
        this.rSyncTime = rSyncTime;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }
}
