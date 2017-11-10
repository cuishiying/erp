package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cnoa_hiddentrouble_result")
public class HiddenTroubleResult extends BaseEntity{
    private static final long serialVersionUID = -8388601211574305265L;

    private String name;
    @Lob
    private String content;
    private LocalDateTime publicTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(LocalDateTime publicTime) {
        this.publicTime = publicTime;
    }
}
