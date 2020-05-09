package app.food.note.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import app.food.note.FoodBean;
import app.food.note.R;

//搜索出来的结果适配器
public class SearchAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {

    public SearchAdapter(List<FoodBean> list) {
        super(R.layout.item_search,list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FoodBean foodBean) {
        baseViewHolder.setText(R.id.item_name, foodBean.name);
        Glide.with(getContext()).load(foodBean.photo).placeholder(R.mipmap.logo).into((ImageView) baseViewHolder.getView(R.id.item_photo));
    }
}
