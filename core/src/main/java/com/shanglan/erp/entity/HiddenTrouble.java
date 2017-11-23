package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cnoa_hiddentrouble_table")
public class HiddenTrouble extends BaseEntity{
    private static final long serialVersionUID = -8388601211574305265L;

    private String name;    //表单名称
    private String entryPerson;   //填表人
    private LocalDate createTime; //建表时间
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<HiddenTroubleItem> hiddentroubles; //隐患项

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntryPerson() {
        return entryPerson;
    }

    public void setEntryPerson(String entryPerson) {
        this.entryPerson = entryPerson;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public List<HiddenTroubleItem> getHiddentroubles() {
        return hiddentroubles;
    }

    public void setHiddentroubles(List<HiddenTroubleItem> hiddentroubles) {
        this.hiddentroubles = hiddentroubles;
    }
}
