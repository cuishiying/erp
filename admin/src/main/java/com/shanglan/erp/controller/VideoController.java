package com.shanglan.erp.controller;

import com.shanglan.erp.service.VideoService;
import com.shanglan.erp.utils.CmdExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 视频监控
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;


    @RequestMapping(path = "/hls",method = RequestMethod.GET)
    public ModelAndView hls(){
        ModelAndView model = new ModelAndView("video_detail");
        String rtspPath = "rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/sub/av_stream";
        String nginxPath = "/usr/local/Cellar/nginx/1.12.2_1/html";
        String nginxIp = "http://192.168.0.102:20000";
        String ipcPath = "/hls/slkj.m3u8";
        videoService.start();
//        videoService.startFFmpeg(rtspPath,nginxPath+ipcPath);
        model.addObject("ipcPath",nginxIp+ipcPath);
        return model;
    }

    @RequestMapping(path = "/test",method = RequestMethod.GET)
    public void test(){
        CmdExecuter.execRuntime("");
    }
}
