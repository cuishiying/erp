package com.shanglan.erp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class HiddenTroubleItemDTO {

    private String keyword;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date queryDate;

    private String type;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getQueryDate() {
        if(queryDate==null){
            queryDate = new Date();
        }
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
