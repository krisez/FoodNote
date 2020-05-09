package app.food.note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        return format.format(new Date());
    }

    public static boolean willPeriod(String createTime, String period) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        try {
            Date date = format.parse(createTime);//创建时间
            Date date1 = p.parse(period.split("/")[0]);//结束时间
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            if (date1 != null) {
                calendar.setTime(date1);
            }
            long end = calendar.getTimeInMillis();
            if (date != null) {
                calendar.setTime(date);
            }
            long start = calendar.getTimeInMillis();
            return ((end - start) / 2 > (end - now));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String period(int year, int month, int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        calendar.set(year, month, day);
        long l = calendar.getTimeInMillis() - now;
        int days = (int) (l / 1000 / 60 / 60 / 24);
        return format.format(calendar.getTime()) + "/" + days + "天";
    }

    static int getLeftDays(String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        try {
            Date date = format.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            if (date != null) {
                calendar.setTime(date);
                return (int) ((calendar.getTimeInMillis() - now) / 1000 / 60 / 60 / 24);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
