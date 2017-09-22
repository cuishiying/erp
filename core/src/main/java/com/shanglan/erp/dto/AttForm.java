package com.shanglan.erp.dto;

public class AttForm {
    private String truename;
    private String dept;
    private Integer fullWorkAttendance;//全勤天数
    private Integer workAttendance;//总出勤天数
    private Integer absence;//缺勤天数

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getFullWorkAttendance() {
        return fullWorkAttendance;
    }

    public void setFullWorkAttendance(Integer fullWorkAttendance) {
        this.fullWorkAttendance = fullWorkAttendance;
    }

    public Integer getWorkAttendance() {
        return workAttendance;
    }

    public void setWorkAttendance(Integer workAttendance) {
        this.workAttendance = workAttendance;
    }

    public Integer getAbsence() {
        return absence;
    }

    public void setAbsence(Integer absence) {
        this.absence = absence;
    }
}