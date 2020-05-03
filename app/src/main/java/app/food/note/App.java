package app.food.note;

import android.app.Application;

import app.food.note.db.DbUtils;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DbUtils.init(this);
    }
}
