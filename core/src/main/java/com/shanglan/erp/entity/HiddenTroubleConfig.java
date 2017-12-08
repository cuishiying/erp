package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cnoa_hiddentrouble_config")
public class HiddenTroubleConfig extends BaseEntity{
    private static final long serialVersionUID = -5461251976151102979L;

    public HiddenTroubleConfig() {
    }

    public HiddenTroubleConfig(String name, String type) {
        this.name = name;
        this.type = type;
    }

    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
