package com.shanglan.erp.controller;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        try {
            //reportXls(getListmap());
            reportMergeXls(getListmap(),"test1","sheet1");
            System.out.println("导出完成");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static List<Map> getListmap(){
        List<Map> list=new ArrayList<Map>();
        Map map1=new HashMap();
        map1.put("id",1);
        map1.put("name","a");
        map1.put("value1","1a");
        map1.put("value2","2a");
        map1.put("value3","3a");

        Map map2=new HashMap();
        map2.put("id",2);
        map2.put("name","b");
        map2.put("value1","1b");
        map2.put("value2","2b");
        map2.put("value3","3b");

        Map map3=new HashMap();
        map3.put("id",3);
        map3.put("name","c");
        map3.put("value1","1c");
        map3.put("value2","2c");
        map3.put("value3","3c");

        list.add(map1);
        list.add(map2);
        list.add(map3);

        return list;

    }
    static String[] excelHeader = { "id", "name", "value","value","value"};
    public static void  reportXls(List<Map> list){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("test1");//创建一个sheet-test1

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFRow row = sheet.createRow((int) 0);
        for (int i = 0; i < excelHeader.length; i++) {//设置表头-标题
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);//自动设宽
        }

        for (int i = 0; i < list.size(); i++) {//设置列值-内容
            row = sheet.createRow(i + 1);
            Map map = list.get(i);
            row.createCell(0).setCellValue((int)map.get("id"));
            row.createCell(1).setCellValue((String)map.get("name"));
            row.createCell(2).setCellValue((String)map.get("value1"));
            row.createCell(3).setCellValue((String)map.get("value2"));
            row.createCell(4).setCellValue((String)map.get("value3"));
        }

        try {
            FileOutputStream fileOut = new FileOutputStream("E:/test.xls");
            wb.write(fileOut);
            fileOut.close();
            System.out.println("输出完成");
        } catch (Exception e) {
            System.out.println("文件输出失败!");
            e.printStackTrace();
        }

    }

    /**
     * 导出xls文件(合并单元格)
     * @param list
     * @param xlsFileName
     * @param sheetName
     */
    public static void reportMergeXls(List<Map> list,String xlsFileName,String sheetName) throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);//创建一个sheet-test1

        //设置单元格风格，居中对齐.
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        cs.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cs.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        //设置字体:
        HSSFFont font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示

        cs.setFont(font);//要用到的字体格式

        //sheet.setColumnWidth(0, 3766); //第一个参数代表列下标(从0开始),第2个参数代表宽度值
        //cs.setWrapText(true);//设置字体超出宽度自动换行

        //设置背景颜色
        //cs.setFillBackgroundColor(HSSFColor.BLUE.index);
        //cs.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);


        //创建第一行
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell ;

        for (int i = 0; i < excelHeader.length; i++) {//设置表头-标题
            cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(cs);
            sheet.autoSizeColumn(i);//自动设宽
        }

        //设置合并的标题头(注意：横向合并的时候，标题头单元格必须长度和内容单元格一致否则合并时会把其他标题头单元格内容挤掉)
        sheet.addMergedRegion(new CellRangeAddress(0,0,2,4));//横向：合并第一行的第2列到第4列
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));//纵向：合并第一列的第1行和第2行第
        sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));//纵向：合并第二列的第1行和第2行第

        //设置对应的合并单元格标题
        row = sheet.createRow(1);
        for (int j = 2; j < 5; j++) {
            cell = row.createCell((short)j);
            cell.setCellStyle(cs);
            cell.setCellValue("value" + (j-1));
            sheet.autoSizeColumn(j);//自动设宽
        }
        //设置列值-内容
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 2);
            Map map = list.get(i);
            row.createCell(0).setCellValue((int)map.get("id"));
            row.createCell(1).setCellValue((String)map.get("name"));
            row.createCell(2).setCellValue((String)map.get("value1"));
            row.createCell(3).setCellValue((String)map.get("value2"));
            row.createCell(4).setCellValue((String)map.get("value3"));
        }

        try {
            FileOutputStream fileOut = new FileOutputStream("/Users/cuishiying/Desktop/test/"+xlsFileName+".xls");
            wb.write(fileOut);
            fileOut.close();
            System.out.println("输出完成");
            /*//页面弹出下载或保存
            response.setContentType("application/x-download");
            response.setHeader("Content-disposition","attachment;filename="+ new String("test.xls".getBytes("utf-8"), "iso-8859-1"));
            response.setCharacterEncoding("utf-8");
            OutputStream os=response.getOutputStream();

            wb.write(os);

            os.close();
             */
        } catch (Exception e) {
            System.out.println("文件输出失败!");
            e.printStackTrace();
        }

    }
}
