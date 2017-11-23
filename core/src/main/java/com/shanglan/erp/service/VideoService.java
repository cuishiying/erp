package com.shanglan.erp.service;


import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.entity.Video;
import com.shanglan.erp.interf.IStringGetter;
import com.shanglan.erp.utils.CmdExecuter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 监控
 */
@Service
@Transactional
public class VideoService{


    /**
     * ffmpeg转码推流
     * @param rtspPath  rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream
     * @param hlsPath   D:/app/nginx-1.12.2/html/hls/slkj.m3u8
     */
    public void startFFmpeg(String rtspPath,String hlsPath){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> cmd = new ArrayList<String>();
                cmd.add("ffmpeg");
                cmd.add("-i");
                cmd.add(rtspPath);
                cmd.add("-vcodec");
                cmd.add("copy");
                cmd.add("-acodec");
                cmd.add("aac");
                cmd.add("-ar");
                cmd.add("44100");
                cmd.add("-strict");
                cmd.add("-2");
                cmd.add("-ac");
                cmd.add("1");
                cmd.add("-f");
                cmd.add("hls");
                cmd.add("-s");
                cmd.add("1280x720");
                cmd.add("-q");
                cmd.add("10");
                cmd.add("-hls_wrap");
                cmd.add("15");
                cmd.add(hlsPath);

                CmdExecuter.exec(cmd,null);
            }
        }).start();
    }

    public AjaxResponse setConfig(){
        Video vidio = new Video();

        return AjaxResponse.success();
    }

}
