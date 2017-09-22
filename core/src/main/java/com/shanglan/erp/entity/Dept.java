package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 部门
 */
@Entity
@Table(name = "cnoa_main_struct")
public class Dept extends BaseEntity{

    private static final long serialVersionUID = 8506001235418856368L;

    @Column(unique = false, nullable = false)
    private String name;// 类目名称

    private Integer fid;//组织机构层级
    private String path;
    private String about;
    private Integer order;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
