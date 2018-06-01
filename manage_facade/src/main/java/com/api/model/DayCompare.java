package com.api.model;

import java.util.Calendar;
import java.util.Date;

/**
 *计算2个日期之间相差的  相差多少年月日
 * @author 陈清玉
 */
public class DayCompare {
    private int year;
    private int month;
    private int day;

    public DayCompare(){
    }

    public DayCompare(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    /**
     * 计算2个日期之间相差的  相差多少年月日
     * 比如：2011-02-02 到  2017-03-02 相差 6年，1个月，0天
     * @param fromDate
     * @param toDate
     * @return
     */
    public static DayCompare dayComparePrecise(Date fromDate, Date toDate){
        Calendar from  =  Calendar.getInstance();
        from.setTime(fromDate);
        Calendar  to  =  Calendar.getInstance();
        to.setTime(toDate);
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int fromDay = from.get(Calendar.DAY_OF_MONTH);
        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int toDay = to.get(Calendar.DAY_OF_MONTH);
        int year = toYear  -  fromYear;
        int month = toMonth  - fromMonth;
        int day = toDay  - fromDay;
        return new DayCompare(year,month,day);
    }
}
