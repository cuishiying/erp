package com.shanglan.test;

import com.shanglan.erp.repository.GoodsRepository;
import com.shanglan.erp.service.VideoService;
import com.shanglan.erp.utils.JavaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

/**
 * Created by cuishiying on 2017/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-context.xml"})
public class TestController {

    String cmd = "ffmpeg -i rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8";

    @Autowired
    private VideoService videoService;
    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * ok
     * @throws Exception
     */
    @Test
    public void test5() throws Exception{
        Integer integer = goodsRepository.valiCategory("0904");
        System.err.println(integer);

    }

}
