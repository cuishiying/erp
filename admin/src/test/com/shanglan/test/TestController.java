package com.shanglan.test;

import com.shanglan.erp.repository.AttMachineRepository;
import com.shanglan.erp.repository.GoodsRepository;
import com.shanglan.erp.service.AttService;
import com.shanglan.erp.utils.JavaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
//        goodsRepository.test();
    }
}
