package com.shanglan.erp.enums;

public enum AttRecordStatus {
    normal(0,"正常"),mAbsence(1,"上午旷工"),aAbsence(2,"下午旷工"),absence(3,"缺勤");

    private Integer id;
    private String name;

    AttRecordStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
