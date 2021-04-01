package flybear.hziee.app.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间常用工具类
 *
 * @author qiancj
 * @since 2020-01-15 15:32
 */
@SuppressWarnings("Duplicates")
public class DateUtil {

    /**
     * 获取某个时间所处天的起始时间
     *
     * @param date 时间
     * @return 一天的起始时间
     */
    public static Date getTheStartTimeOfTheDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某个时间所处天的起始时间（可以有天数偏移）
     *
     * @param date 时间
     * @return 一天的起始时间
     */
    public static Date getTheStartTimeOfTheDay(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return getTheStartTimeOfTheDay(cal.getTime());
    }

    /**
     * 获取某个时间所处月的起始时间
     *
     * @param date 时间
     * @return 一个月的起始时间
     */
    public static Date getTheStartTimeOfTheMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某个时间所处月的起始时间（可以有月份偏移）
     *
     * @param date   时间
     * @param amount 相差月份
     * @return 某个月的起始时间
     */
    public static Date getTheStartTimeOfTheMonth(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, amount);
        return getTheStartTimeOfTheMonth(cal.getTime());
    }


    /**
     * 获取某个时间所处年的起始时间
     *
     * @param date 时间
     * @return 一年的起始时间
     */
    public static Date getTheStartTimeOfTheYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某个时间所处年的起始时间（可以有天数偏移）
     *
     * @param date 时间
     * @return 一年的起始时间
     */
    public static Date getTheStartTimeOfTheYear(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, amount);
        return getTheStartTimeOfTheDay(cal.getTime());
    }

    /**
     * 获取某个时间所处周的开始的时间
     *
     * @param date           时间
     * @param firstDayOfWeek 一个星期的第一天
     * @return 一周的开始
     */
    public static Date getWeekStartDate(Date date, int firstDayOfWeek) {
        date = getTheStartTimeOfTheDay(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天
        cal.setFirstDayOfWeek(firstDayOfWeek);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 获取两个日期相差天数
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 相差天数
     */
    public static int getDateDiff(Date d1, Date d2) {
        return (int) Math.abs((d1.getTime() - d2.getTime()) / (1000 * 3600 * 24)) + 1;
    }

    /**
     * 获取两个日期相差月数
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 相差月数
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return Math.abs(yearInterval * 12 + monthInterval);
    }

}
