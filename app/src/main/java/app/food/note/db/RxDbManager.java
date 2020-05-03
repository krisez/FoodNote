package app.food.note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite3.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.food.note.FoodBean;
import app.food.note.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxDbManager {
    private BriteDatabase db;

    private RxDbManager() {
        db = DbUtils.getInstance();
    }

    public static RxDbManager getInstance() {
        return new RxDbManager();
    }

    //服务器返回有新的消息
    //自己发送的消息
    public Observable<Boolean> insert(final FoodBean food) {
        return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            ContentValues values = new ContentValues();
            values.put("name", food.name);
            values.put("period", food.period);
            values.put("area", food.area);
            values.put("photo", food.photo);
            values.put("createTime", Utils.getTime());
            values.put("updateTime", Utils.getTime());
            db.insert(DBHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values);
            e.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //查询语句
    public Observable<List<FoodBean>> query(String zone) {
        return Observable.create((ObservableOnSubscribe<List<FoodBean>>) emitter -> {
            Cursor cursor = db.query("select * from " + DBHelper.TABLE_NAME + " where area=\"" + zone + "\"");
            List<FoodBean> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String period = cursor.getString(cursor.getColumnIndex("period"));
                String area = cursor.getString(cursor.getColumnIndex("area"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
                String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
                list.add(new FoodBean(_id, name, period, area, photo, createTime, updateTime));
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //删除消息记录，服务器不做操作
    public void deleteMsg(ArrayList<Integer> _id) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (int i = 0; i < _id.size(); i++) {
                db.delete(DBHelper.TABLE_NAME, "_id = ?", _id.get(i) + "");
            }
        } finally {
            transaction.markSuccessful();
        }
    }

    //清空数据库
    public void clear() {
        db.execute("delete from " + DBHelper.TABLE_NAME + ";");
    }

    public Observable<Boolean> update(FoodBean bean) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            ContentValues values = new ContentValues();
            values.put("name", bean.name);
            values.put("period", bean.period);
            values.put("area", bean.area);
            values.put("photo", bean.photo);
            values.put("updateTime", Utils.getTime());
            db.update(DBHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values, "_id=?", bean.id + "");
            emitter.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
