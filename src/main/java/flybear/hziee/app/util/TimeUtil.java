package flybear.hziee.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Mango
 * @Date: 2021/3/22 19:41:30
 */
public class TimeUtil {

    public static String formatTime(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String time = format.format(new Date());
        return time;
    }

    public static String defaultFormatTime() {
        return formatTime("yyyy-MM-dd HH:mm:ss");
    }

    public static Date getTimeStamp(String timeStr, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(timeStr);
    }

    public static Date getDefaultTimeStamp(String timeStr) throws ParseException {
        return getTimeStamp(timeStr, "yyyy-MM-dd HH:mm:ss");
    }
}
