package com.shanglan.test;

import com.shanglan.erp.entity.Collect;
import com.shanglan.erp.interf.StreamGobbler;
import com.shanglan.erp.repository.AttMachineRepository;
import com.shanglan.erp.repository.GoodsRepository;
import com.shanglan.erp.service.AttService;
import com.shanglan.erp.service.ErpService;
import com.shanglan.erp.utils.JavaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuishiying on 2017/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-context.xml"})
public class TestController {
    @Autowired
    private AttMachineRepository attMachineRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private AttService attService;
    @Autowired
    private ErpService erpService;

    @Test
    public void test() throws Exception{
        String storage = goodsRepository.findStorage(1);
        System.out.println(storage);

    }

    @Test
    public void test1() throws Exception{
        String time = "1496806250";
        String s = JavaUtils.convert2String(time);
        System.out.println(s);
    }

    @Test
    public void test2() throws Exception{
        String dayOfWeekByDate = JavaUtils.getDayOfWeekByDate("2017-9-4");
        Long aLong = JavaUtils.convert2Timelong("2017-09-01 06:50:00");
        System.out.println(aLong);
    }
    @Test
    public void test3() throws Exception{
//        List<String> currentDayListOfMonth = JavaUtils.getDayListOfMonth(2017,9);
//        for(int i=0;i<currentDayListOfMonth.size();i++){
//            attService.saveTodayAttendances(currentDayListOfMonth.get(i));
//        }
//        Long start = JavaUtils.convert2Timelong("2017-09-01 13:50:00");
//        Long end = JavaUtils.convert2Timelong("2017-09-01 14:20:00");
//        Integer attendance = attMachineRepository.isAttendance(241, start, end);
//        List<AttMachine> att = attService.findAtt(241, "2017-09-01");
//        String test = JavaUtils.convertLong2Time("1504245354");
//        System.out.println(test);
        attService.saveTodayAttendances("2017-09-25");
    }
    @Test
    public void test4() throws Exception{
//        List<AttMachine> att = attService.findAtt(538, "2017-09-01");
//        attMachineRepository.isAttendance()
//        System.out.println(att);
        long l = JavaUtils.compareDate("2017-09-07", "2017-09-02");
        System.out.println(l);
    }

    @Test
    public void test5() throws Exception{
        Page<Collect> collectByPage = erpService.findCollectByPage("", null);
    }

    @Test
    public void hls() throws Exception{
        String command = "ffmpeg -i rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8";

        String[] commandSplit = command.split(" ");
        List<String> lcommand = new ArrayList<String>();
        for (int i = 0; i < commandSplit.length; i++) {
            lcommand.add(commandSplit[i]);
        }

        Process process = null;
        ProcessBuilder processBuilder = new ProcessBuilder(lcommand);
        processBuilder.redirectErrorStream(true);
        try{
            process = processBuilder.start();
            StreamGobbler  errorGobbler  =  new StreamGobbler(process.getErrorStream(),  "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler  outGobbler  =  new  StreamGobbler(process.getInputStream(),  "STDOUT");
            outGobbler.start();//  kick  off  stdout

            process.waitFor();
        }catch (Exception e){
            e.printStackTrace();
            process.destroy();
        }


//        InputStream is = p.getInputStream();
//        BufferedReader bs = new BufferedReader(new InputStreamReader(is));
//
//        p.waitFor();
//        if (p.exitValue() != 0) {
//            //说明命令执行失败
//            //可以进入到错误处理步骤中
//        }
//        String line = null;
//        while ((line = bs.readLine()) != null) {
//            System.out.println(line);
//        }
    }

    @Test
    public void hls2() throws Exception{
        String command = "ffmpeg -i rtsp://admin:slkj0520@192.168.0.100:554/h264/ch1/main/av_stream -vcodec copy -acodec aac -ar 44100 -strict -2 -ac 1 -f hls -s 1280x720 -q 10 -hls_wrap 15 /usr/local/Cellar/nginx/1.12.2_1/html/hls/slkj.m3u8";
        Process process = null;
        try{
            process = Runtime.getRuntime().exec(new String[]{"sh","-c",command});

            StreamGobbler  errorGobbler  =  new StreamGobbler(process.getErrorStream(),  "ERROR");
            errorGobbler.start();//  kick  off  stderr
            StreamGobbler  outGobbler  =  new  StreamGobbler(process.getInputStream(),  "STDOUT");
            outGobbler.start();//  kick  off  stdout

            process.waitFor();
        }catch (Exception e){
            e.printStackTrace();
            process.destroy();
        }

    }

}
