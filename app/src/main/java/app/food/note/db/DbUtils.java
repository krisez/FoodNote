package app.food.note.db;

import android.app.Application;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import io.reactivex.schedulers.Schedulers;

public class DbUtils {
    private static BriteDatabase db;
    private static Application context;

    public static void init(Application application) {
        context = application;
    }

    static BriteDatabase getInstance(){
        if(db == null) {
            synchronized (DbUtils.class) {
                if (db == null) {
                    SqlBrite sqlBrite = new SqlBrite.Builder().build();
                    SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration.builder(context)
                            .name(DBHelper.TABLE_NAME)
                            .callback(new DBHelper())
                            .build();
                    SupportSQLiteOpenHelper.Factory factory = new FrameworkSQLiteOpenHelperFactory();
                    SupportSQLiteOpenHelper helper = factory.create(configuration);
                    db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
                }
            }
        }
        return  db;
    }
}
