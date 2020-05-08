package app.food.note.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.Utils;

public class FoodAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
    public FoodAdapter(List<FoodBean> data) {
        super(R.layout.item_food, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FoodBean foodBean) {
        baseViewHolder.setText(R.id.item_name, foodBean.name);
        String time = foodBean.createTime + "购买，保质期至" + foodBean.period.split("/")[0];
        baseViewHolder.setText(R.id.item_time, time);
        baseViewHolder.setText(R.id.item_left_days, "剩余" + foodBean.getLeftDays() + "天");
        if (Utils.willPeriod(foodBean.createTime, foodBean.period)) {
            baseViewHolder.setTextColor(R.id.item_left_days, Color.RED);
        }
        Glide.with(getContext()).load(foodBean.photo).placeholder(R.mipmap.foodnote).into((ImageView) baseViewHolder.getView(R.id.item_photo));
    }
}
