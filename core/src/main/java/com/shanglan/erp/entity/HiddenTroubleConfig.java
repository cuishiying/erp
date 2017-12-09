package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cnoa_hiddentrouble_config")
public class HiddenTroubleConfig extends BaseEntity{
    private static final long serialVersionUID = -5461251976151102979L;

    private String name;
    private String classifyName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }
}
