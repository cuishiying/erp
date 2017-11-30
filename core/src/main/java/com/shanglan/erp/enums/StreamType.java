package com.shanglan.erp.enums;

/**
 * 视频码流类型
 */
public enum StreamType {

    main(0,"主码流"),sub(1,"子码流");

    private Integer id;
    private String name;

    StreamType(Integer id, String name) {
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
