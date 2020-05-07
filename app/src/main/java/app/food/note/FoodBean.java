package app.food.note;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodBean implements Parcelable {
    public int id;
    public String name;
    public int period;//保质期
    public String photo;
    public String area;
    public String createTime;
    public String updateTime;
    public String note;
    public String zone = "";//冰箱的区域
    public int type; //食物类型，冷冻冷藏，粮油干货.....

    public FoodBean() {
    }

    public FoodBean(String name, int period, String photo, String area, String note, String zone, int type) {
        this.name = name;
        this.period = period;
        this.photo = photo;
        this.area = area;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.note = note;
        this.zone = zone;
        this.type = type;
    }

    public FoodBean(int id, String name, int period, String photo, String area, String createTime, String updateTime, String note, String zone, int type) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.photo = photo;
        this.area = area;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.note = note;
        this.zone = zone;
        this.type = type;
    }

    private FoodBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        period = in.readInt();
        photo = in.readString();
        area = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
        note = in.readString();
        zone = in.readString();
        type = in.readInt();
    }

    public static final Creator<FoodBean> CREATOR = new Creator<FoodBean>() {
        @Override
        public FoodBean createFromParcel(Parcel in) {
            return new FoodBean(in);
        }

        @Override
        public FoodBean[] newArray(int size) {
            return new FoodBean[size];
        }
    };

    @Override
    public String toString() {
        return "FoodBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", period='" + period + '\'' +
                ", photo='" + photo + '\'' +
                ", area='" + area + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", note='" + note + '\'' +
                ", zone='" + zone + "\'" +
                ", type='" + type + "\'" +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(period);
        parcel.writeString(photo);
        parcel.writeString(area);
        parcel.writeString(createTime);
        parcel.writeString(updateTime);
        parcel.writeString(note);
        parcel.writeString(zone);
        parcel.writeInt(type);
    }
}
