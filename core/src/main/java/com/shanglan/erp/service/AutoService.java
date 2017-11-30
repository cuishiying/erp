package com.shanglan.erp.service;

import com.shanglan.erp.repository.RiskRepository;
import com.shanglan.erp.utils.CmdExecuter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class AutoService {

    String cmd = "ffmpeg -i rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8";


    @Autowired
    private RiskRepository riskRepository;
    @Autowired
    private RiskService riskService;
    @Autowired
    private VideoService videoService;

    /**
     * 初始化风险管控配置项
     */
    @PostConstruct
    public void initRiskConditions(){
//        riskRepository.truncateTable();
        riskService.initRiskConditions();
    }

    /**
     * 初始化监控配置
     */
    @PostConstruct
    public void initVideoConfig(){
        videoService.initVideoConfig();
    }

}
