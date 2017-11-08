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
    private String pitName; //矿井名称
    private LocalDate finishTime;   //完成时间
    private String leader;   //分矿领导签字
    private String rummager;   //检查人
    private String organizationMan;   //组织人
    private String entryPerson;   //填表人


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<HiddenTroubleItem> hiddentroubles; //隐患项

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPitName() {
        return pitName;
    }

    public void setPitName(String pitName) {
        this.pitName = pitName;
    }

    public LocalDate getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDate finishTime) {
        this.finishTime = finishTime;
    }

    public List<HiddenTroubleItem> getHiddentroubles() {
        return hiddentroubles;
    }

    public void setHiddentroubles(List<HiddenTroubleItem> hiddentroubles) {
        this.hiddentroubles = hiddentroubles;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getRummager() {
        return rummager;
    }

    public void setRummager(String rummager) {
        this.rummager = rummager;
    }

    public String getOrganizationMan() {
        return organizationMan;
    }

    public void setOrganizationMan(String organizationMan) {
        this.organizationMan = organizationMan;
    }

    public String getEntryPerson() {
        return entryPerson;
    }

    public void setEntryPerson(String entryPerson) {
        this.entryPerson = entryPerson;
    }
}
