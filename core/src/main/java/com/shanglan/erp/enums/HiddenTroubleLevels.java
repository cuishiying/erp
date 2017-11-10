package com.shanglan.erp.enums;

public enum HiddenTroubleLevels {
    A(0,"A"),B(1,"B"),C(2,"C");

    private Integer id;
    private String name;

    HiddenTroubleLevels(Integer id, String name) {
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
