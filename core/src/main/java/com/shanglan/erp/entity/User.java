package com.shanglan.erp.entity;

import javax.persistence.*;

/**
 * Created by cuishiying on 2017/6/13.
 * 试卷//todo
 */
@Entity
@Table(name = "cnoa_main_user")
public class User {

    private static final long serialVersionUID = -2301740839588119761L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer uid;
    private String username;
    private String truename;
    private String phone;
    private String workphone;
    private String qq;
    @Column(name="deptId")
    private Integer deptId;//所属部门





    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorkphone() {
        return workphone;
    }

    public void setWorkphone(String workphone) {
        this.workphone = workphone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}
