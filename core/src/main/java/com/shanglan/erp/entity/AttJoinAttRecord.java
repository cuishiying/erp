package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 左连接考勤记录对象
 */
@Entity
@Table(name = "cnoa_att_mgr_joinatt")
public class AttJoinAttRecord extends BaseEntity {

    private Integer uid;
    private Integer cnum;
    private String  cname;
    private String truename;
    private String importKey;
    private String timeStr;
    private String date;
    private String time;
    private String oneStime;
    private String oneEtime;


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCnum() {
        return cnum;
    }

    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getOneStime() {
        return oneStime;
    }

    public void setOneStime(String oneStime) {
        this.oneStime = oneStime;
    }

    public String getOneEtime() {
        return oneEtime;
    }

    public void setOneEtime(String oneEtime) {
        this.oneEtime = oneEtime;
    }
}
