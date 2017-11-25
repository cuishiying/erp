package com.shanglan.erp.base;

import com.shanglan.erp.dto.ExcelBean;
import com.shanglan.erp.entity.Collect;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static sun.net.www.protocol.http.HttpURLConnection.userAgent;

/**
 * Created by cuishiying on 2017/6/13.
 */
public class ExcelUtils {
    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel
    /*************************************文件上传****************************/
    public static  List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception{
        List<List<Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if(sheet==null){continue;}

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j < sheet.getPhysicalNumberOfRows(); j++) {
                row = sheet.getRow(j);
                int count = row.getFirstCellNum();

                if(row==null||row.getFirstCellNum()==-1||row.getFirstCellNum()==j||isRowEmpty(row)){continue;}

                //遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y,Row.CREATE_NULL_AS_BLANK);
                    li.add(getCellValue(cell));
                }
                list.add(li);
            }
        }
//        work.close();
        return list;
    }

    public static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
                return false;
        }
        return true;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public static  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    public static  Object getCellValue(Cell cell){
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }
    /****************************************上传结束,导出Excel表开始***************************************/


    /**
     *
     * @param fileName 导出excel名字
     * @param sheetName 表名
     * @param clazz 导出数据实体类
     * @param objs  导出数据列表
     * @param map 表头名称与实体类字段映射关系
     * @param response
     */
    public static void export(String fileName,String sheetName,Class clazz, List objs,LinkedHashMap<String,String> map,HttpServletResponse response){
        try {
            if(org.apache.commons.lang.StringUtils.contains(userAgent, "Firefox") || org.apache.commons.lang.StringUtils.contains(userAgent, "firefox")){//火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            }else{
                fileName = URLEncoder.encode(fileName,"UTF-8");//其他浏览器
            }


            // 指定下载的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            List<ExcelBean> excel = new ArrayList<ExcelBean>();
            Map<Integer, List<ExcelBean>> mapExcel = new LinkedHashMap<Integer, List<ExcelBean>>();
            XSSFWorkbook xssfWorkbook = null;
            //设置标题栏
            for (Map.Entry<String, String> entry : map.entrySet()) {
                excel.add(new ExcelBean(entry.getKey(), entry.getValue(), 0));
            }
            mapExcel.put(0, excel);
            xssfWorkbook = ExcelUtils.createExcelFile(clazz, objs, mapExcel, sheetName);
            OutputStream output;
            try {
                output = response.getOutputStream();
                BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
                bufferedOutPut.flush();
                xssfWorkbook.write(bufferedOutPut);
                bufferedOutPut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /**
     * @param sheetName 工作簿名称
     * @param clazz  数据源model类型
     * @param objs   excel标题列以及对应model字段名
     * @param map  标题列行数以及cell字体样式
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws IntrospectionException
     * @throws ParseException
     */
    public static XSSFWorkbook createExcelFile(Class clazz, List objs, Map<Integer, List<ExcelBean>> map, String sheetName) throws IllegalArgumentException,IllegalAccessException,
            InvocationTargetException, ClassNotFoundException, IntrospectionException, ParseException {
        // 创建新的Excel 工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 以下为excel的字体样式以及excel的标题与内容的创建，下面会具体分析;
        createFont(workbook);//字体样式
        createTableHeader(sheet, map);//创建标题（头）
        createTableRows(sheet, map, objs, clazz);//创建内容
        return workbook;
    }

    private static XSSFCellStyle fontStyle;
    private static XSSFCellStyle fontStyle2;
    private static XSSFCellStyle fontStyle3;
    private static XSSFCellStyle fontStyle4;
    public static void createFont(XSSFWorkbook workbook) {
        // 表头
        fontStyle = workbook.createCellStyle();
        XSSFFont font1 = workbook.createFont();
        font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font1.setFontName("黑体");
        font1.setFontHeightInPoints((short) 14);// 设置字体大小
        fontStyle.setFont(font1);
        fontStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
        fontStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
        fontStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
        fontStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
        fontStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中

        // 内容
        fontStyle2=workbook.createCellStyle();
        XSSFFont font2 = workbook.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 10);// 设置字体大小
        fontStyle2.setFont(font2);
        fontStyle2.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
        fontStyle2.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
        fontStyle2.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
        fontStyle2.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
        fontStyle2.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中

        fontStyle3 = workbook.createCellStyle();
        XSSFFont font3 = workbook.createFont();
        font3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font3.setFontName("黑体");
        font3.setFontHeightInPoints((short) 14);// 设置字体大小
        fontStyle3.setFont(font3);
        fontStyle3.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中

        fontStyle4 = workbook.createCellStyle();
        XSSFFont font4 = workbook.createFont();
        font4.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font4.setFontName("宋体");
        font4.setFontHeightInPoints((short) 12);// 设置字体大小
        fontStyle4.setFont(font4);
        fontStyle4.setAlignment(XSSFCellStyle.ALIGN_RIGHT); // 居中
    }

    /**
     * 根据ExcelMapping 生成列头（多行列头）
     *
     * @param sheet
     *            工作簿
     * @param map
     *            每行每个单元格对应的列头信息
     */
    public static final void createTableHeader(XSSFSheet sheet, Map<Integer, List<ExcelBean>> map) {

        for (Map.Entry<Integer, List<ExcelBean>> entry : map.entrySet()) {

            int firstRow = 0;   //行起始位置
            int lastRow = 0;    //行终止位置
            int firstCol = 0;   //列起始位置
            int lastCol = 0;    //列终止位置

            XSSFRow row = sheet.createRow(entry.getKey());  //根据行号创建行
            List<ExcelBean> excels = entry.getValue();  //获取行数据
            for (int x = 0; x < excels.size(); x++) {
                //占多列。合并单元格
                if(excels.get(x).getCols()>1){

                    lastCol+=excels.get(x).getCols();

                    sheet.addMergedRegion(new CellRangeAddress(0,0,firstCol,lastCol-1));

                    firstCol+=excels.get(x).getCols();
                    int columnIndex = firstCol-excels.get(x).getCols();
                    XSSFCell cell = row.createCell(columnIndex);
                    cell.setCellValue(excels.get(x).getHeadTextName());// 设置内容
                    if (excels.get(x).getCellStyle() != null) {
                        cell.setCellStyle(excels.get(x).getCellStyle());// 设置格式
                    }
                    cell.setCellStyle(fontStyle);
                }else{
                    //占一列
                    XSSFCell cell = row.createCell(firstCol);
                    cell.setCellValue(excels.get(x).getHeadTextName());// 设置内容
                    if (excels.get(x).getCellStyle() != null) {
                        cell.setCellStyle(excels.get(x).getCellStyle());// 设置格式
                    }
                    cell.setCellStyle(fontStyle);
                    firstCol++;
                    lastCol++;
                }
            }

        }
    }



    /**
     *
     * @param sheet
     * @param map
     * @param objs
     * @param clazz
     */
    @SuppressWarnings("rawtypes")
    public static void createTableRows(XSSFSheet sheet, Map<Integer, List<ExcelBean>> map, List objs, Class clazz)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException,
            ClassNotFoundException, ParseException {

        int rowindex = map.size();
        int maxKey = 0;
        List<ExcelBean> ems = new ArrayList<>();
        for (Map.Entry<Integer, List<ExcelBean>> entry : map.entrySet()) {
            if (entry.getKey() > maxKey) {
                maxKey = entry.getKey();
            }
        }
        ems = map.get(maxKey);

        List<Integer> widths = new ArrayList<Integer>(ems.size());
        for (Object obj : objs) {
            XSSFRow row = sheet.createRow(rowindex);
            for (int i = 0; i < ems.size(); i++) {
                ExcelBean em = (ExcelBean) ems.get(i);
                // 获得get方法
                PropertyDescriptor pd = new PropertyDescriptor(em.getPropertyName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object rtn = getMethod.invoke(obj);
                String value = "";
                // 如果是日期类型 进行 转换
                if (rtn != null) {
                    if (rtn instanceof Date) {
                        value = DateUtils.date2String((Date) rtn,"yyyy-MM-dd");
                    } else if(rtn instanceof BigDecimal){
                        NumberFormat nf = new DecimalFormat("#,##0.00");
                        value=nf.format((BigDecimal)rtn).toString();
                    } else if((rtn instanceof Integer) && (Integer.valueOf(rtn.toString())<0 )){
                        value="--";
                    }else {
                        value = rtn.toString();
                    }
                }
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(value);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(fontStyle2);
                // 获得最大列宽
                int width = value.getBytes().length * 300;
                // 还未设置，设置当前
                if (widths.size() <= i) {
                    widths.add(width);
                    continue;
                }
                // 比原来大，更新数据
                if (width > widths.get(i)) {
                    widths.set(i, width);
                }
            }
            rowindex++;
        }
        // 设置列宽
        for (int index = 0; index < widths.size(); index++) {
            Integer width = widths.get(index);
            width = width < 2500 ? 2500 : width + 300;
            width = width > 10000 ? 10000 + 300 : width + 300;
            sheet.setColumnWidth(index, width);
        }
    }

    /****************************************隐患排查定制***************************************/

    public static void exportHiddenTrouble(String fileName,String sheetName,Class clazz, List objs,LinkedHashMap<String,String> map,HttpServletResponse response){
        try {
            if(org.apache.commons.lang.StringUtils.contains(userAgent, "Firefox") || org.apache.commons.lang.StringUtils.contains(userAgent, "firefox")){//火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            }else{
                fileName = URLEncoder.encode(fileName,"UTF-8");//其他浏览器
            }


            // 指定下载的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            List<ExcelBean> excel = new ArrayList<ExcelBean>();
            Map<Integer, List<ExcelBean>> mapExcel = new LinkedHashMap<Integer, List<ExcelBean>>();
            XSSFWorkbook xssfWorkbook = null;
            //设置标题栏
            for (Map.Entry<String, String> entry : map.entrySet()) {
                excel.add(new ExcelBean(entry.getKey(), entry.getValue(), 0));
            }
            mapExcel.put(0, excel);
            mapExcel.put(1, excel);
            mapExcel.put(3, excel);
            mapExcel.put(4, excel);
            xssfWorkbook = ExcelUtils.createMergeExcelFile(clazz, objs, mapExcel, sheetName);
            OutputStream output;
            try {
                output = response.getOutputStream();
                BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
                bufferedOutPut.flush();
                xssfWorkbook.write(bufferedOutPut);
                bufferedOutPut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static XSSFWorkbook createMergeExcelFile(Class clazz, List objs, Map<Integer, List<ExcelBean>> map, String sheetName) throws IllegalArgumentException,IllegalAccessException,
            InvocationTargetException, ClassNotFoundException, IntrospectionException, ParseException {
        // 创建新的Excel 工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 以下为excel的字体样式以及excel的标题与内容的创建，下面会具体分析;
        createFont(workbook);//字体样式
        createMergeTableHeader(sheet, map);//创建标题（头）
        createTableRows(sheet, map, objs, clazz);//创建内容
        return workbook;
    }

    /**
     * 复杂表头（合并单元格）
     * @param sheet
     * @param map
     */
    public static final void createMergeTableHeader(XSSFSheet sheet, Map<Integer, List<ExcelBean>> map) {

        //创建第1行
        XSSFRow row = sheet.createRow((short) 0);
        XSSFCell cell ;
        cell = row.createCell(0);
        cell.setCellValue("事故隐患排查治理登记台账");
        cell.setCellStyle(fontStyle3);
        sheet.autoSizeColumn(0);//自动设宽
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,15));//横向：合并第1行



        //创建第2行
        row = sheet.createRow((short) 1);
        cell = row.createCell(0);
        cell.setCellValue("20    年    月");
        cell.setCellStyle(fontStyle4);
        sheet.autoSizeColumn(0);//自动设宽
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,15));//横向：合并第1行


        //创建第3行
        row = sheet.createRow((short) 2);

        String[] excelHeader = { "序号", "排查地点", "排查时间","排查人员","存在的隐患及问题","类别","等级","隐患整改“五落实”","隐患整改“五落实”","隐患整改“五落实”","隐患整改“五落实”","隐患整改“五落实”","督办部门/人员","验收时间","验收部门/人员","整改结果"};
        String[] excelSubHeader = { "序号", "排查地点", "排查时间","排查人员","存在的隐患及问题","类别","等级","整改措施", "责任单位/责任人", "资金","时限","预案","督办部门/人员","验收时间","验收部门/人员","整改结果"};
        for (int i = 0; i < excelHeader.length; i++) {//设置表头-标题
            cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(fontStyle);
            sheet.autoSizeColumn(i);//自动设宽
        }
        //设置合并的标题头(注意：横向合并的时候，标题头单元格必须长度和内容单元格一致否则合并时会把其他标题头单元格内容挤掉)
        sheet.addMergedRegion(new CellRangeAddress(2,2,7,11));//横向：合并第3行的第7列到第11列
        for(int i=0;i<7;i++){
            sheet.addMergedRegion(new CellRangeAddress(2,3,i,i));//纵向：合并第一列的第3行和第4行第
        }
        for(int j=12;j<16;j++){
            sheet.addMergedRegion(new CellRangeAddress(2,3,j,j));
        }


        //创建第4行
        row = sheet.createRow((short)3);
        for (int i = 0; i < excelSubHeader.length; i++) {//设置表头-标题
            cell = row.createCell(i);
            cell.setCellValue(excelSubHeader[i]);
            cell.setCellStyle(fontStyle);
            sheet.autoSizeColumn(i);//自动设宽
        }
    }
}
