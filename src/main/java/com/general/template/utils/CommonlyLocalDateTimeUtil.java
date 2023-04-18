package com.general.template.utils;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CommonlyLocalDateTimeUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Long convertAllTimeToLong(LocalDateTime parse) {
        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当前标准格式化日期时间
     *
     * @return
     */
    public static String getDateTime() {
        return getDateTime(new Date());
    }

    /**
     * 标准格式化日期时间
     *
     * @param date
     * @return
     */
    public static String getDateTime(Date date) {
        return (new SimpleDateFormat(DATE_FORMAT)).format(date);
    }
}
