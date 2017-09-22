package com.shanglan.erp.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cuishiying on 2017/6/13.
 */
public class DateUtils {
    public static String date2String(Date data,String format){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
