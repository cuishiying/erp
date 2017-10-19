package com.shanglan.erp.entity;

import javax.persistence.*;

/**
 * Created by cuishiying on 2017/6/13.
 * 试卷//todo
 */
@Entity
@Table(name = "cnoa_main_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer uid;
    private String username;
    private String truename;
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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}
