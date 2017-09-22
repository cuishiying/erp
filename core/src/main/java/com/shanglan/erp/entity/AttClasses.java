package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 考勤班制
 */
@Entity
@Table(name = "cnoa_att_new_classses")
public class AttClasses extends BaseEntity {

    private static final long serialVersionUID = -1084759654171798006L;
    private Integer cid;
    private String name;
    private String t1;
    private String t2;
    private String t3;
    private String t4;
    @ElementCollection
    private List<String> attClassTimes;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAttClassTimes() {
        return attClassTimes;
    }

    public void setAttClassTimes(List<String> attClassTimes) {
        this.attClassTimes = attClassTimes;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }
}
