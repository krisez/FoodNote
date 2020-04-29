package app.food.note;

public class FoodBean {
    public int id;
    public String name;
    public String period;//保质期
    public String photo;
    public String area;
    public String createTime;
    public String updateTime;

    public FoodBean(String name, String period, String photo, String area) {
        this.name = name;
        this.period = period;
        this.photo = photo;
        this.area = area;
    }

    public FoodBean(int id, String name, String period, String photo, String area, String createTime, String updateTime) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.photo = photo;
        this.area = area;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

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
                '}';
    }
}
