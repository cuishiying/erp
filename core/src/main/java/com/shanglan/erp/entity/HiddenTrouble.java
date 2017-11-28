package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cnoa_hiddentrouble_table")
public class HiddenTrouble extends BaseEntity{
    private static final long serialVersionUID = -8388601211574305265L;

    private String name;    //表单名称
    private String entryPerson;   //填表人
    private LocalDateTime createTime; //建表时间
    private String createMonth;
    private String createMonthStr;

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<HiddenTroubleItem> getHiddentroubles() {
        return hiddentroubles;
    }

    public void setHiddentroubles(List<HiddenTroubleItem> hiddentroubles) {
        this.hiddentroubles = hiddentroubles;
    }

    public String getCreateMonth() {
        return createMonth;
    }

    public void setCreateMonth(String createMonth) {
        this.createMonth = createMonth;
    }

    public String getCreateMonthStr() {
        return createMonthStr;
    }

    public void setCreateMonthStr(String createMonthStr) {
        this.createMonthStr = createMonthStr;
    }
}
