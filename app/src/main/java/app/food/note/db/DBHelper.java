package app.food.note.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class DBHelper extends SupportSQLiteOpenHelper.Callback {
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "food";
    public static final String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,period TEXT,area TEXT,photo TEXT,createTime TEXT,updateTime TEXT);";

    public DBHelper() {
        super(VERSION);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
