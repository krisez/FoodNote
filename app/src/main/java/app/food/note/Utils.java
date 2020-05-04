package app.food.note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("zh","CN"));
        return format.format(new Date());
    }

    public static long leftDays(String updateTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("zh","CN"));
        try {
            Date date = format.parse(updateTime);
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            if(date != null){
                calendar.setTime(date);
            }
            long left = now - calendar.getTimeInMillis();
            return left / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
