package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;
import com.shanglan.erp.enums.AttRecordStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 考勤统计
 */
@Entity
@Table(name = "cnoa_att_record")
public class AttRecord extends BaseEntity{
    private static final long serialVersionUID = 8732909452714747351L;

    private Integer uid;
    private String dept;//部门
    private String truename;//姓名
    private String date;//年月 2017-6-8
    @Enumerated(EnumType.STRING)
    private AttRecordStatus attRecordStatus;//出勤状态
    private Double attendanceDay;//出勤天数：0天,0.5天，1天
    private String machineData;//考勤原始数据
    private LocalDateTime updateTime;
    private String weekday;


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AttRecordStatus getAttRecordStatus() {
        return attRecordStatus;
    }

    public void setAttRecordStatus(AttRecordStatus attRecordStatus) {
        this.attRecordStatus = attRecordStatus;
    }

    public Double getAttendanceDay() {
        return attendanceDay;
    }

    public void setAttendanceDay(Double attendanceDay) {
        this.attendanceDay = attendanceDay;
    }

    public String getMachineData() {
        return machineData;
    }

    public void setMachineData(String machineData) {
        this.machineData = machineData;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
