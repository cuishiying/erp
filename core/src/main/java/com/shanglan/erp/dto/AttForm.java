package com.shanglan.erp.dto;

public class AttForm {
    private String truename;
    private String dept;
    private Integer fullWorkAttendance;//全勤天数
    private Float workAttendance;//总出勤天数
    private Float absence;//缺勤天数

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

    public Float getWorkAttendance() {
        return workAttendance;
    }

    public void setWorkAttendance(Float workAttendance) {
        this.workAttendance = workAttendance;
    }

    public Float getAbsence() {
        return absence;
    }

    public void setAbsence(Float absence) {
        this.absence = absence;
    }
}