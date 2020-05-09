package app.food.note.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite3.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.food.note.FoodBean;
import app.food.note.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
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

    //插入一条新的食物相关内容
    public Observable<Boolean> insert(final FoodBean food) {
        return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            ContentValues values = new ContentValues();
            values.put("name", food.name);
            values.put("period", food.period);
            values.put("area", food.area);
            values.put("photo", food.photo);
            values.put("createTime", Utils.getTime());
            values.put("updateTime", Utils.getTime());
            values.put("note", food.note);
            values.put("iceZone", food.zone);
            values.put("consume", 0);
            values.put("type", food.type);
            db.insert(DBHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values);
            e.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //查询语句，分别查询干货、调料、常温食物
    public Observable<List<FoodBean>> query(String area) {
        return Observable.create((ObservableOnSubscribe<List<FoodBean>>) emitter -> {
            Cursor cursor = db.query("select * from " + DBHelper.TABLE_NAME + " where area=\"" + area + "\" and consume=0");
            List<FoodBean> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String period = cursor.getString(cursor.getColumnIndex("period"));
                String a = cursor.getString(cursor.getColumnIndex("area"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
                String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String iceZone = cursor.getString(cursor.getColumnIndex("iceZone"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                list.add(new FoodBean(_id, name, period, photo, a, createTime, updateTime, note, iceZone, type));
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //查询语句，插叙冰箱不同区域的不同食物
    public Observable<List<FoodBean>> queryIcebox(String iceZone) {
        return Observable.create((ObservableOnSubscribe<List<FoodBean>>) emitter -> {
            Cursor cursor = db.query("select * from " + DBHelper.TABLE_NAME + " where iceZone=\"" + iceZone + "\" and consume=0");
            List<FoodBean> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String period = cursor.getString(cursor.getColumnIndex("period"));
                String area = cursor.getString(cursor.getColumnIndex("area"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
                String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String zone = cursor.getString(cursor.getColumnIndex("iceZone"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                list.add(new FoodBean(_id, name, period, photo, area, createTime, updateTime, note, zone, type));
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //查询语句,查询搜索的时候能查到的食物
    public Observable<List<FoodBean>> querySearchType(int t) {
        return Observable.create((ObservableOnSubscribe<List<FoodBean>>) emitter -> {
            Cursor cursor = db.query("select * from " + DBHelper.TABLE_NAME + " where type=" + t + " group by name");
            List<FoodBean> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String period = cursor.getString(cursor.getColumnIndex("period"));
                String area = cursor.getString(cursor.getColumnIndex("area"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
                String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String zone = cursor.getString(cursor.getColumnIndex("iceZone"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                list.add(new FoodBean(_id, name, period, photo, area, createTime, updateTime, note, zone, type));
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //清空数据库
    public void clear(String tableName, int type) {
        db.execute("delete from " + tableName + " where type=" + type + ";");
    }

    public Observable<Boolean> update(FoodBean bean) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            ContentValues values = new ContentValues();
            values.put("name", bean.name);
            values.put("period", bean.period);
            values.put("area", bean.area);
            values.put("photo", bean.photo);
            values.put("updateTime", Utils.getTime());
            values.put("note", bean.note);
            values.put("iceZone", bean.zone);
            values.put("type", bean.type);
            db.update(DBHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values, "_id=?", bean.id + "");
            emitter.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> consumeFood(FoodBean bean) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            ContentValues values = new ContentValues();
            values.put("consume", 1);
            db.update(DBHelper.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values, "_id=?", bean.id + "");
            emitter.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<FoodBean>> queryPeriod() {
        return Observable.create((ObservableOnSubscribe<ArrayList<FoodBean>>) emitter -> {
            Cursor cursor = db.query("select * from " + DBHelper.TABLE_NAME);
            ArrayList<FoodBean> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String period = cursor.getString(cursor.getColumnIndex("period"));
                String area = cursor.getString(cursor.getColumnIndex("area"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String createTime = cursor.getString(cursor.getColumnIndex("createTime"));
                String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String zone = cursor.getString(cursor.getColumnIndex("iceZone"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                if (Utils.willPeriod(createTime, period)) {
                    list.add(new FoodBean(_id, name, period, photo, area, createTime, updateTime, note, zone, type));
                }
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    //搜索记录
    //插入一条新的搜索记录
    public Observable<Boolean> insertHistory(int type, int foodId) {
        return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            ContentValues values = new ContentValues();
            values.put("type", type);
            values.put("foodId", foodId);
            db.insert(DBHelper.SEARCH_TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values);
            e.onNext(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<FoodBean>> querySearchHistory(int t) {
        return Observable.create((ObservableOnSubscribe<List<FoodBean>>) emitter -> {
            Cursor cursor = db.query("select * from " + DBHelper.TABLE_NAME + " where _id in (select foodId from " + DBHelper.SEARCH_TABLE_NAME + " where type=" + t + ") group by name;");
            List<FoodBean> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String period = cursor.getString(cursor.getColumnIndex("period"));
                String area = cursor.getString(cursor.getColumnIndex("area"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String zone = cursor.getString(cursor.getColumnIndex("iceZone"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                list.add(new FoodBean(_id, name, period, photo, area, note, zone, type));
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
