package com.shanglan.erp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cuishiying on 2017/6/29.
 */
public class JavaUtils {

    private static DateTimeFormatter LocalDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     *  java去除字符串中的空格、回车、换行符、制表符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String getNowLong(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return convert2Timelong(LocalDateTime.now().format(formatter))+"";
    }

    /**
     * 日期转时间戳
     * @param date
     * @return
     * @throws ParseException
     */
    public static Long convert2long(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = sdf2.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = parse.getTime()/1000;
        return time;
    }
    /**
     * 时间转时间戳
     * @param date
     * @return
     * @throws ParseException
     */
    public static Long convert2Timelong(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = sdf2.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = parse.getTime()/1000;
        return time;
    }

    /**
     * 时间戳转日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static String convert2String(String date){
        if(null==date){
            return "";
        }
        long l = Long.parseLong(date);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf2.format(l*1000);
        return format;
    }

    /**
     * 1504245354--13:55:54
     * @param date
     * @return
     */
    public static String convertLong2Time(String date){
        if(null==date){
            return "";
        }
        long l = Long.parseLong(date);
        SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
        String format = sdf2.format(l*1000);
        return format;
    }
    /**
     * 时间戳转时间
     * @param date
     * @return
     * @throws ParseException
     */
    public static String convert2DateTime(String date){
        if(null==date){
            return "";
        }
        long l = Long.parseLong(date);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf2.format(l*1000);
        return format;
    }

    /**
     * 求时间差
     *
     * @param startDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return long[时，分]
     */
    private static long[] timeDifference(Date startDate,Date endDate){
        long diff = endDate.getTime() - startDate.getTime();
        long hour = (diff / (60 * 60 * 1000));
        long min = ((diff / (60 * 1000)) - hour * 60);

        return new long[] { hour, min };
    }


    /**
     * 获取指定月份每一天的日期
     * @param year
     * @param month
     * @return
     */
    public static List<String> getDayListOfMonth(int year, int month) {
        List<String> list = new ArrayList();
        int monthLastDay = getMonthLastDay(year, month);
        for (int i = 1; i <= monthLastDay; i++) {
            String aDate = String.valueOf(year)+"-"+appendzero(month)+"-"+appendzero(i);
            list.add(aDate);
        }
        return list;
    }

    /**
     * 自动补0
     * @param num
     * @return
     */
    public static String appendzero(Integer num){
        if(num<10){
            return "0"+num;
        }
        return String.valueOf(num);
    }

    /**
     * 获取当月每一天的日期
     * @return
     */
    public static List getCurrentDayListOfMonth() {

        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH)+1;//月份
        List list = getDayListOfMonth(year, month);
        return list;
    }

    /**
     * 获取今天是本月的第几天
     * @return
     */
    public static int getIndexOfCurrentMonth(){
        Date date=new Date();
        Calendar ca=Calendar.getInstance();
        ca.setTime(date);
        int a=ca.get(Calendar.DAY_OF_MONTH);
        return a;
    }

    /**
     * 取得当月天数
     * */
    public static int getCurrentMonthLastDay()
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     * */
    public static int getMonthLastDay(int year, int month)
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    /**
     * 根据日期 找到对应日期的 星期
     */
    public static String getDayOfWeekByDate(String date) {
        String dayOfweek = "-1";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;

        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }

    /**
     * 比较日期大小
     * @return
     */
    public static long compareDate(String big,String little){
        long result = 0;
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse1 = myFormatter.parse(big);
            Date parse2 = myFormatter.parse(little);
            result = parse1.getTime() - parse2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取月份第一天日期
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year,int month){
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(a.getTime());
        return firstDayOfMonth;
    }
    /**
     * 获取月份最后一天日期
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year,int month){
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DAY_OF_MONTH, 1);
        a.add(Calendar.DAY_OF_MONTH,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(a.getTime());
        return lastDayOfMonth;
    }

    public static String getMonth(int year,int month){
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DAY_OF_MONTH, 1);
        a.add(Calendar.DAY_OF_MONTH,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String lastDayOfMonth = sdf.format(a.getTime());
        return lastDayOfMonth;
    }

    /**
     * 字符串日期转换
     * @param date
     * @return
     */
    public static LocalDate convert2LocalDate(String date){
        try{
            LocalDate d = LocalDate.parse(date, LocalDateFormatter);
            return d;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate getFirstDayOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String firstDayOfMonth = getFirstDayOfMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        return convert2LocalDate(firstDayOfMonth);
    }
    public static LocalDate getLastDayOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String lastDayOfMonth = getLastDayOfMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        return convert2LocalDate(lastDayOfMonth);
    }
    public static String getMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
//        String month = getMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
        return c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH) + 1)+"月";
    }
}
