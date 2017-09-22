package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by cuishiying on 2017/7/13.
 * 货品分类
 */
@Entity
@Table(name = "cnoa_jxc_category")
public class Category extends BaseEntity{
    private static final long serialVersionUID = 9024570263924853652L;

    private String code;
    private String name;
    private Integer parentId;
    private String parentName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
