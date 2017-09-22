package com.shanglan.erp.entity;

import javax.persistence.*;

/**
 * Created by cuishiying on 2017/7/17.
 * 货品分类
 */
@Entity
@Table(name = "cnoa_jxc_sort")
public class Sort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    private Integer pid;
    private String name;
    private Integer order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
