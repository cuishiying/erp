package com.shanglan.erp.entity;


import com.shanglan.erp.base.BaseEntity;
import com.shanglan.erp.enums.StreamType;

import javax.persistence.Entity;

/**
 * 通道配置
 *    ffmpeg -i "rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream" -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8
 */
@Entity
public class Video extends BaseEntity{
    private static final long serialVersionUID = -830285805653993047L;

    private String username;   //帐号 admin
    private String password;    //密码 slkj0520
    private String rtspIp;  //视频源ip或者NVR-ip 192.168.0.100:554
    private String channel; //通道号 ch1
    private StreamType streamType;  //码流类型
    private String rtspPath;   //rtsp视频源 rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream

    private String nginxPath;   //nginx发布路径     /usr/local/Cellar/nginx/1.12.2_1/html
    private String nginxIp; //nginx http发布路径    http://192.168.0.102:20000
    private String ipcPath; //转码后的m3u8在nginx中的相对路径  /hls/slkj.m3u8
    private String m3u8Path;    //m3u8推流地址  /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8

    private String vidioPath;    //完整m3u8路径     http://192.168.0.102:20000/hls/slkj.m3u8

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRtspIp() {
        return rtspIp;
    }

    public void setRtspIp(String rtspIp) {
        this.rtspIp = rtspIp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public StreamType getStreamType() {
        return streamType;
    }

    public void setStreamType(StreamType streamType) {
        this.streamType = streamType;
    }

    public String getRtspPath() {
        return rtspPath;
    }

    public void setRtspPath(String rtspPath) {
        this.rtspPath = rtspPath;
    }

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

    public String getIpcPath() {
        return ipcPath;
    }

    public void setIpcPath(String ipcPath) {
        this.ipcPath = ipcPath;
    }

    public String getM3u8Path() {
        return m3u8Path;
    }

    public void setM3u8Path(String m3u8Path) {
        this.m3u8Path = m3u8Path;
    }

    public String getVidioPath() {
        return vidioPath;
    }

    public void setVidioPath(String vidioPath) {
        this.vidioPath = vidioPath;
    }
}
