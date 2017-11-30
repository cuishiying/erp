package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cnoa_video_config")
public class VideoConfig extends BaseEntity{

    private static final long serialVersionUID = -4543810941696667137L;

    private String nginxPath;   //视频流发布路径   /usr/local/Cellar/nginx/1.12.2_1/html
    private String nginxIp;     //视频访问路径    http://192.168.0.102:20000

    public String getNginxPath() {
        return nginxPath;
    }

    public void setNginxPath(String nginxPath) {
        this.nginxPath = nginxPath;
    }

    public String getNginxIp() {
        return nginxIp;
    }

    public void setNginxIp(String nginxIp) {
        this.nginxIp = nginxIp;
    }
}
