package com.shanglan.erp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class QueryDTO {

    @DateTimeFormat(pattern = "yyyy-MM")
    private Date queryDate;

    public Date getQueryDate() {
        if(queryDate==null){
            queryDate = new Date();
        }
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }
}
