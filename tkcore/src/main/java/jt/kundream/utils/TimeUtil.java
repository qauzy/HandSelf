package jt.kundream.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Company: YunBeiTeac
 * Copyright: Copyright (c) 2017-2018
 *
 * @author YangTianKun
 * @Version 1.0
 * @Since 2018/4/9/009 and 14:33
 * @Email 245086168@qq.com
 * Desc:获取或者转化时间的一些工具类
 */
public class TimeUtil {

    /**
     * 获取精确到秒的时间戳  10位数
     *
     * @return
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static int getNowTimestamp() {
        return (int) (Calendar.getInstance().getTimeInMillis() / 1000);
    }

    /***
     * 将时间转换为时间戳
     *
     * @param s    时间
     * @param type 样式
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s, String type) throws ParseException {
        String res;
        if (type == null) {
            type = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /***
     * 将时间转换为时间戳
     *
     * @param s    时间
     * @param type 样式
     * @return
     * @throws ParseException
     */
    public static long dateToStampMill(String s, String type) throws ParseException {
        String res;
        if (type == null) {
            type = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }


    public static long getTodayStamp() {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(String.format("%s-%s-%s", year, month, day));
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();

            return -1;
        }
    }

    /***
     * 将时间戳转换为时间
     *
     * @param timeStamp    时间戳
     * @param type 样式
     * @return
     */
    public static String stampToDate(Long timeStamp, String type) {
        String res;
        if (type == null) {
            type = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
        long lt = new Long(timeStamp * 1000);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * date2比date1多的天数（24h内算一天）
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) //闰年
                {
                    timeDistance += 366;
                } else //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔（24h内不算一天）
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * Company: YunBeiTeac
     * Copyright: Copyright (c) 2017-2018
     *
     * @author Created by YangTianKun at 2018/4/9/009 and 14:36
     * @Email 245086168@qq.com
     * describe:获取当前系统前一天日期
     */
    public static Date getPreDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    /**
     * Company: YunBeiTeac
     * Copyright: Copyright (c) 2017-2018
     *
     * @author Created by YangTianKun at 2018/4/9/009 and 14:42
     * @Email 245086168@qq.com
     * describe:获取当前日期
     */
    public static String getToday() {
        // 获得当前时间，声明时间变量
        Calendar calendar = Calendar.getInstance();
        // 得到年
        int year = calendar.get(Calendar.YEAR);
        // 得到月，但是，月份要加上1
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        // 获得日期
        int date = calendar.get(Calendar.DATE);
//        String today = "" + year + "-" + month + "月" + date + "日";
        String today = month + "月" + date + "日";

        return today;
    }

    /**
     * @param n 传进来的参数代表的是当前日期往前或者往后几天  正数为后  负数为前
     * @return 返回当前时间往前或者往后推n天之后的时间
     */
    public static String getAnyDate(int n) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);//把日期往后增加一天.正数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        return dateString.substring(5, 7) + "月" + dateString.substring(8, dateString.length()) + "日";
    }

    /**
     * 获取当前时
     */
    public static String getHour() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        return String.valueOf(hour);
    }

    /**
     * 获取当前分
     */
    public static String getMinute() {
        Calendar now = Calendar.getInstance();
        int minute = now.get(Calendar.MINUTE);
        return String.valueOf(minute);
    }


}
