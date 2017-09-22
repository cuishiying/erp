package com.shanglan.erp.entity;

import javax.persistence.*;

/**
 * Created by cuishiying on 2017/7/14.
 */
@Entity
@Table(name = "cnoa_jxc_storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    private String storagename;
    private Integer off;
    private Integer manager;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoragename() {
        return storagename;
    }

    public void setStoragename(String storagename) {
        this.storagename = storagename;
    }

    public Integer getOff() {
        return off;
    }

    public void setOff(Integer off) {
        this.off = off;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }
}
