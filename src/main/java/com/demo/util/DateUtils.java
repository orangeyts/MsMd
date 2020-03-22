package com.demo.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {

    public static DateVO getNowDateVO() {

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        //获取当前时间的DateTime对象，这个对象类似于java.util.Date的方法，用于封装当前时间对象
        DateTime now = DateTime.now();

//设置当前时间所在的周的周一,withDayOfWeek(int day)类似于Calendar.set(MONDAY)，但比Calandar好用多了，Calandar
//的时间是西方时间，他们的周日相当于我们的周一，用起来很别扭，DateTime就不会这样
        DateTime monday = now.withDayOfWeek(1)
//设置日期为当天的某个小时，这里设置为0点
                .withHourOfDay(0)
//设置日期为当天的某个分钟，设置为0分
                .withMinuteOfHour(0)
//设置为当天的某秒。设置为0秒
                .withSecondOfMinute(0);

//输出时间
        System.out.println(monday);//2016-09-26T00:00:00.418+08:00 9月26日 0点0分0秒，刚好是当周周一


//将DateTime转换为java.util.Date，以便各种api调用
        String mon = monday.toString(fmt);

//如果要获取本周日的时间也很简单，将withDayOfWeek设置为7就行了
        now = now.withDayOfWeek(7).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

        String sun = now.toString(fmt);
//本周日

        DateVO build = DateVO.builder().mon(mon).sun(sun).build();
        return build;
    }

    public static void main(String[] args) {
        System.out.println(getNowDateVO());
    }
}
