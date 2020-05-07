package app.food.note.db;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class DBHelper extends SupportSQLiteOpenHelper.Callback {
    private static final int VERSION = 1;
    static final String TABLE_NAME = "food";//存储各种食物信息
    public static final String SEARCH_TABLE_NAME = "search_history"; //搜索历史
//    static final String NOTE_TABLE = "food_note";//食物备忘录(暂时不要）
    private static final String CREATE_FOOD_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,period INTEGER,area TEXT,iceZone TEXT,photo TEXT,note TEXT,createTime TEXT,updateTime TEXT,type INTEGER,consume INTEGER);";

    private static final String CREATE_FOOD_SEARCH_TABLE = "CREATE TABLE IF NOT EXISTS " + SEARCH_TABLE_NAME
            + "(type INTEGER,foodId INTEGER);";


    DBHelper() {
        super(VERSION);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_FOOD_SEARCH_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SupportSQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
