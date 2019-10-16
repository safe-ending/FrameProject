package common.util;


import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间样式处理
 *
 * @author kongdq
 */
public class TimeStyleUtil {

    public final static String DATE_TYPE0 = "yyyy-MM-dd/HH:mm:ss";
    public final static String DATE_TYPE1 = "yyyy-MM-dd HH:mm";
    public final static String DATE_TYPE2 = "yyyy-MM-dd";
    public final static String DATE_TYPE14 = "yyyy/MM/dd HH:mm";
    public final static String DATE_TYPE3 = "MM-dd";
    public final static String DATE_TYPE4 = "HH:mm";
    public final static String DATE_TYPE5 = "yyyyMMddHHmmssSS";
    public final static String DATE_TYPE6 = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TYPE7 = "yyyyMMddHHmmss";
    public final static String DATE_TYPE8 = "yyyyMMddHHmm";
    public final static String DATE_TYPE9 = "yyyyMMdd";
    public final static String DATE_TYPE10 = "MM-dd HH:mm:ss";
    public final static String DATE_TYPE11 = "HH:mm:ss";
    public final static String DATE_TYPE12 = "yyyy年MM月dd日";
    public final static String DATE_TYPE13 = "yyyy.MM";
    /**
     * 获取时间显示样式
     *
     * @param timeStr
     * @return
     */
    public static String getDateShowStyle(String timeStr) {
        String dateStr = null;
        Calendar nowCal = Calendar.getInstance();
        Calendar myCal = getCalendar(timeStr, DATE_TYPE0);
        int myYear = myCal.get(Calendar.YEAR);
        int nowYear = nowCal.get(Calendar.YEAR);
        // 与当前时间不是同一年
        if (nowYear != myYear) {
            dateStr = getTimeInfo(myCal.getTimeInMillis(), DATE_TYPE2);
        } else {

            String mydate = getTimeInfo(myCal.getTimeInMillis(), DATE_TYPE2);
            String nowdate = getTimeInfo(nowCal.getTimeInMillis(), DATE_TYPE2);
            int hourOfDay = myCal.get(Calendar.HOUR_OF_DAY);
            int minute = myCal.get(Calendar.MINUTE);

            long daySub = getTimeDifference(mydate, nowdate);
            if (daySub == 0) {
                dateStr = "今天 " + hourOfDay + ":" + getMinuteV(minute);
            } else if (daySub == 1) {
                dateStr = "昨天 " + hourOfDay + ":" + getMinuteV(minute);
            } else if (daySub == 2) {
                dateStr = "前天 " + hourOfDay + ":" + getMinuteV(minute);
            } else {
                dateStr = getTimeInfo(myCal.getTimeInMillis(), DATE_TYPE3);
            }
        }

        return dateStr;
    }

    /**
     * 获取时间显示样式
     *
     * @param timeStr
     * @return
     */
    public static String getDateShowStyle(String timeStr, String dateType) {
        // tiemStr="20150326125529000";
        String dateStr = null;
        Calendar nowCal = Calendar.getInstance();
        Calendar myCal = getCalendar(timeStr, dateType);
        int myYear = myCal.get(Calendar.YEAR);
        int nowYear = nowCal.get(Calendar.YEAR);
        // 与当前时间不是同一年
        if (nowYear != myYear) {
            dateStr = getTimeInfo(myCal.getTimeInMillis(), DATE_TYPE1);
        } else {

            String mydate = getTimeInfo(myCal.getTimeInMillis(), DATE_TYPE2);
            String nowdate = getTimeInfo(nowCal.getTimeInMillis(), DATE_TYPE2);
            int hourOfDay = myCal.get(Calendar.HOUR_OF_DAY);
            int minute = myCal.get(Calendar.MINUTE);

            long daySub = getTimeDifference(mydate, nowdate);
            if (daySub == 0) {
                dateStr = getTodayInfo(hourOfDay) + hourOfDay + ":" + getMinuteV(minute);
            } else if (daySub == 1) {
                dateStr = "昨天 " + hourOfDay + ":" + getMinuteV(minute);
            } else if (daySub == 2) {
                dateStr = "前天 " + hourOfDay + ":" + getMinuteV(minute);
            } else {
                dateStr = getTimeInfo(myCal.getTimeInMillis(), DATE_TYPE3) + " " + hourOfDay + ":" + getMinuteV(minute);
            }
        }

        return dateStr;
    }

    private static String getMinuteV(int minute) {
        if (minute < 10)
            return "0" + minute;
        else
            return String.valueOf(minute);
    }

    private static String getTodayInfo(int hour) {
        if (hour < 9)
            return "早上 ";
        else if (9 <= hour && hour < 11)
            return "上午 ";
        else if (11 <= hour && hour < 13)
            return "中午 ";
        else if (13 <= hour && hour < 18)
            return "下午 ";
        else
            return "晚上 ";
    }

    /**
     * 获取时间显示样式
     *
     * @param dateTime 12月15日
     * @return
     */
    public static String getDateShowStyle1(String dateTime) {
        final Calendar calendar = Calendar.getInstance();
        String dateInfo = null;
        try {
            calendar.setTime(new SimpleDateFormat(DATE_TYPE5).parse(dateTime));
            int hourOfDay = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            dateInfo = hourOfDay + "月" + day + "日";
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateInfo;
    }

    /**
     * 获取时间实例
     */
    public static Calendar getCalendar(String dateTime, String dateType) {
        final Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat(dateType).parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return calendar;
    }


    /**
     * 获取时间的long值
     *
     * @param dateTime
     * @param dateType
     * @return
     */
    public synchronized static long getDateTimeLong(String dateTime, String dateType) {
        final Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(dateType).parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return calendar.getTimeInMillis();
    }

    /**
     * 获取时间信息
     *
     * @param timeLong
     * @param dateType
     * @return
     */
    public synchronized static String getTimeInfo(long timeLong, String dateType) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateType);
            Date dt = new Date(timeLong);
            return sdf.format(dt).toString();
        } catch (Exception e) {
            return timeLong + "";
        }
    }

    /**
     * 时间相减得到天数
     *
     * @param beginDateStr 2014-03-17
     * @param endDateStr   2014-02-17
     * @return long
     */
    public static long getTimeDifference(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 时间相减得到的分数
     *
     * @param beginDateStr 2014-03-17 00:00
     * @param endDateStr   2014-02-17 00:00
     * @return long
     */
    public static long getTimeDif(String beginDateStr, String endDateStr) {
        long min = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            min = (endDate.getTime() - beginDate.getTime()) / (60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return min;
    }

    /**
     * 时间相减返回指定时间单位
     *
     * @return long
     */
    public synchronized static long getTimeDifference(String beginDateStr, String endDateStr, String timeType, long timeUnit) {
        long value = 0;
        SimpleDateFormat format = new SimpleDateFormat(timeType);
        Date beginDate;
        Date endDate;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
            value = (endDate.getTime() - beginDate.getTime()) / timeUnit;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取时间间隔值
     *
     * @param tm long
     * @return 返回分钟单位
     */
    public static long getTimeMinuteDiff(String tm) {
        // 得到时间差
        long timeV = 0, timeDiff = 0, minute = 1000;
        try {
            timeV = Long.parseLong(tm);
            timeDiff = new Date().getTime() - timeV;
        } catch (Exception e) {
            e.printStackTrace();
        }
        minute = (timeDiff) / (60 * 1000);

        return minute;
    }

    /**
     * 得到显示时间样式
     *
     * @param inVal
     * @return
     */
    public static String getStringTime(String inVal) {
        StringBuffer buffer = new StringBuffer();
        String strDate;
        String strTime;
        if (inVal.contains("/")) {
            strDate = inVal.split("/")[0];
            strTime = inVal.split("/")[1];
        } else if (inVal.contains(" ")) {
            strDate = inVal.split(" ")[0];
            strTime = inVal.split(" ")[1];
        } else {
            return inVal;
        }

        if (strTime.contains(":")) {
            buffer.append(strTime.split(":")[0] + ":");
            buffer.append(strTime.split(":")[1] + ", ");
        }

        if (strDate.contains("-")) {
            // 当前日期信息
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            String[] array = strDate.split("-");
            String strYear = array[0];
            String strMon = array[1];
            String strDay = array[2];
            if (!strYear.equals(year + "")) {
                buffer.append(strYear + "年");
            }
            buffer.append(strMon + "月");
            buffer.append(strDay + "日");
        }

        return buffer.toString();
    }

    /**
     * 得到时间信息
     *
     * @param timeStr 20140221102722000 //1390471452673
     */
    public static String getTimeMessage(String timeStr) {
        if (timeStr == null || timeStr.length() < 10)
            return null;
        long timeV = 0, timeDiff = 0;
        try {
            timeV = Long.parseLong(timeStr);
            //timeV = parseDateTime(timeStr);
            timeDiff = new Date().getTime() - timeV;
        } catch (Exception e) {
            return null;
        }

        long minute = (timeDiff) / (60 * 1000);
        long hour = (timeDiff) / (60 * 60 * 1000);
        long day = (timeDiff) / (24 * 60 * 60 * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date(timeV);
        String sDateTime = sdf.format(dt);

        String mes;
        if (minute <= 3) {
            mes = "刚刚...";
        } else if (minute > 3 && hour <= 0) {
            mes = minute + "分钟前";
        } else if (hour > 0 && day <= 0) {
            mes = hour + "小时前";
        } else if (day > 0 && day <= 10) {
            mes = day + "天前";
        } else {
            mes = sDateTime;
        }
        return mes;
    }


    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getCurrentTime(String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        return time;
    }

    /**
     * 将时间类型差值计算
     *
     * @return
     */
    public static String getDateFromLong(long d1, long d2) {
        long[] time = new long[4];
        String result = "";
        long diff = d1 - d2;
        time[0] = diff / (1000 * 60 * 60 * 24);

        time[1] = (diff - time[0] * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        time[2] = (diff - time[0] * (1000 * 60 * 60 * 24) - time[1] * (1000 * 60 * 60)) / (1000 * 60);
        time[3] = (diff - time[0] * (1000 * 60 * 60 * 24) - time[1] * (1000 * 60 * 60) - time[2] * (1000 * 60)) / 1000;

        if (time[0] > 0) {
            result += time[0]+1 + "天";
        }else {
            if (time[1] > 0) {
                result += time[1] + "时";
            }
            if (time[2] > 0) {
                result += time[2] + "分";
            }
//            if (time[3] > 0) {
//                result += time[3] + "秒";
//            }
        }
        return result; // 返回 _天 或 _ 时_分_秒
    }


    /**
     * 将时间类型以长整形 形式输出 单位是毫秒
     *
     * @return
     */
    public static long getDateNowToLong() {
        Date curDate = new Date(System.currentTimeMillis());
        return curDate.getTime(); // 返回毫秒数
    }

    /**
     * 通过日期来确定星座
     *
     * @param mouth
     * @param day
     * @return
     */
    public static String getStarSeat(int mouth, int day) {
        String starSeat = null;

        if ((mouth == 3 && day >= 21) || (mouth == 4 && day <= 19)) {
            starSeat = "白羊座";
        } else if ((mouth == 4 && day >= 20) || (mouth == 5 && day <= 20)) {
            starSeat = "金牛座";
        } else if ((mouth == 5 && day >= 21) || (mouth == 6 && day <= 21)) {
            starSeat = "双子座";
        } else if ((mouth == 6 && day >= 22) || (mouth == 7 && day <= 22)) {
            starSeat = "巨蟹座";
        } else if ((mouth == 7 && day >= 23) || (mouth == 8 && day <= 22)) {
            starSeat = "狮子座";
        } else if ((mouth == 8 && day >= 23) || (mouth == 9 && day <= 22)) {
            starSeat = "处女座";
        } else if ((mouth == 9 && day >= 23) || (mouth == 10 && day <= 23)) {
            starSeat = "天秤座";
        } else if ((mouth == 10 && day >= 24) || (mouth == 11 && day <= 22)) {
            starSeat = "天蝎座";
        } else if ((mouth == 11 && day >= 23) || (mouth == 12 && day <= 21)) {
            starSeat = "射手座";
        } else if ((mouth == 12 && day >= 22) || (mouth == 1 && day <= 19)) {
            starSeat = "摩羯座";
        } else if ((mouth == 1 && day >= 20) || (mouth == 2 && day <= 18)) {
            starSeat = "水瓶座";
        } else {
            starSeat = "双鱼座";
        }
        return starSeat;
    }

    /**
     * 将时间类型以长整形 形式输出 单位是毫秒
     *
     * @return
     */
    public static long getLongFromString(String time , String formType){
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formType);
            date = formatter.parse(time);
        }catch (Exception e){

        }
        return date.getTime();
    }

    /**
     * 通过日期获取前后几天的日期  传正数代表之后   反之之前
     * @return
     */
    public static String getDataFromCurrent( int day){
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str = getCurrentTime("yyyy年MM月dd日");
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, day);
        Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        return out;
    }

    /**
     * 通过日期获取前后几天的日期  传正数代表之后   反之之前
     * @return
     */
    public static String getFromCurrent( int day,String formType){
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat(formType);
        String str = getCurrentTime(formType);
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, day);
        Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        return out;
    }

    /**
     * 通过日期获取前后几月的日期  传正数代表之后   反之之前
     * @return
     */
    public static String getMonthFromCurrent( int num,String formType){
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat(formType);
        String str = getCurrentTime(formType);
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(str, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);//某月第一天
        calendar.set(Calendar.HOUR_OF_DAY,0);//某月第一天
        calendar.set(Calendar.MINUTE,0);//某月第一天
        calendar.set(Calendar.SECOND,0);//某月第一天
        // add方法中的第二个参数n中，正数表示该日期后n月，负数表示该日期的前n月
        calendar.add(Calendar.MONTH, num);
        Date date1 = calendar.getTime();
        String out = sdf.format(date1);
        return out;
    }
}
