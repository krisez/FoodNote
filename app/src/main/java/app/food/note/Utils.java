package app.food.note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//工具类，用于处理时间
public class Utils {
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        return format.format(new Date());
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

    public static int getLeftDays(String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", new Locale("zh", "CN"));
        try {
            Date date = format.parse(endTime);
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            if (date != null) {
                calendar.setTime(date);
                long l = calendar.getTimeInMillis() - now;
                return (int) (l / 1000 / 60 / 60 / 24) + 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String notifyTime(FoodBean bean) {
        String[] p = bean.period.split("/");
        int left = getLeftDays(p[0]);
        return p[0] + "/" + left + "天";
    }
}
