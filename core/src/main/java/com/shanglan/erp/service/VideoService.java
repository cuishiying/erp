package com.shanglan.erp.service;


import com.shanglan.erp.interf.IStringGetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 监控
 */
@Service
@Transactional
public class VideoService implements IStringGetter{


    /**
     * ffmpeg转码推流
     * @param rtspPath  rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream
     * @param hlsPath   D:/app/nginx-1.12.2/html/hls/slkj.m3u8
     */
    public void startFFmpeg(String rtspPath,String hlsPath){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> commend = new ArrayList<String>();
                commend.add("ffmpeg");
                commend.add("-i");
                commend.add(rtspPath);
                commend.add("-vcodec");
                commend.add("copy");
                commend.add("-acodec");
                commend.add("aac");
                commend.add("-ar");
                commend.add("44100");
                commend.add("-strict");
                commend.add("-2");
                commend.add("-ac");
                commend.add("1");
                commend.add("-f");
                commend.add("hls");
                commend.add("-s");
                commend.add("1280x720");
                commend.add("-q");
                commend.add("10");
                commend.add("-hls_wrap");
                commend.add("15");
                commend.add(hlsPath);

                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(commend);
                    processBuilder.redirectErrorStream(true);
                    Process p = processBuilder.start();
                    InputStream is = p.getInputStream();
                    BufferedReader bs = new BufferedReader(new InputStreamReader(is));
                    p.waitFor();
                    if (p.exitValue() != 0) {
                        //说明命令执行失败，此处命令不需要退出
                        //可以进入到错误处理步骤中
                        System.out.println("命令执行失败");
                    }
                    String line = null;
                    while ((line = bs.readLine()) != null) {
                        System.out.println(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void dealString(String str) {

    }
}
