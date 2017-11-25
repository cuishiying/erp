package com.shanglan.test;

import com.shanglan.erp.base.ExcelUtils;
import com.shanglan.erp.dto.ExcelBean;
import com.shanglan.erp.entity.Collect;
import com.shanglan.erp.interf.StreamGobbler;
import com.shanglan.erp.repository.AttMachineRepository;
import com.shanglan.erp.repository.GoodsRepository;
import com.shanglan.erp.service.AttService;
import com.shanglan.erp.service.ErpService;
import com.shanglan.erp.utils.JavaUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testExcel() throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFRow row = sheet.createRow(1);
        HSSFCell cell = row.createCell((short)1);
        cell.setCellValue("This is a test of merging");

        //1.生成字体对象
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("新宋体");
        font.setColor(HSSFColor.BLUE.index);
        font.setBoldweight((short) 0.8);
        //2.生成样式对象
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFont(font); //调用字体样式对象
        style.setWrapText(true);
        //增加表格边框的样式 例子
        style.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        style.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
        style.setTopBorderColor(HSSFColor.GOLD.index);
        style.setLeftBorderColor(HSSFColor.PLUM.index);

        //3.单元格应用样式
        cell.setCellStyle(style);



        //新版用法 3.8版
//             sheet.addMergedRegion(new CellRangeAddress(
//                     1, //first row (0-based)  from 行
//                     2, //last row  (0-based)  to 行
//                     1, //first column (0-based) from 列
//                     1  //last column  (0-based)  to 列
//             ));
        //表示合并B2,B3
        sheet.addMergedRegion(new CellRangeAddress(
                1, //first row (0-based)
                (short)1, //first column  (0-based)
                2, //last row (0-based)
                (short)1  //last column  (0-based)
        ));

        //合并叠加  表示合并B3 B4。但是B3已经和B2合并了，所以，变成B2:B4合并了
        sheet.addMergedRegion(new CellRangeAddress(
                2, //first row (0-based)
                (short)1, //first column  (0-based)
                3, //last row (0-based)
                (short)1  //last column  (0-based)
        ));

        //一下代码表示在D4 cell 插入一段字符串
        HSSFRow row2 = sheet.createRow(3);
        HSSFCell cell2 = row2.createCell((short)3);
        cell2.setCellValue("this is a very very very long string , please check me out.");
        //cell2.setCellValue(new HSSFRichTextString("我是单元格！"));


        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

}
