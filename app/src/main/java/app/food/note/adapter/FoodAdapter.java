package app.food.note.adapter;

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
        baseViewHolder.setText(R.id.item_period, "保质期：" + foodBean.period + "天");
        baseViewHolder.setText(R.id.item_left_days, "剩余天数：" + (foodBean.period - Utils.leftDays(foodBean.updateTime)));
        baseViewHolder.setText(R.id.item_create_time, "创建时间：" + foodBean.createTime);
        baseViewHolder.setText(R.id.item_update_time, "更新时间：" + foodBean.updateTime);
        Glide.with(getContext()).load(foodBean.photo).placeholder(R.mipmap.foodnote).into((ImageView) baseViewHolder.getView(R.id.item_photo));
    }
}
