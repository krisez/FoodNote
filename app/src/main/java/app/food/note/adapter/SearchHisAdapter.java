package app.food.note.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import app.food.note.FoodBean;
import app.food.note.R;

public class SearchHisAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
    public SearchHisAdapter(List<FoodBean> data) {
        super(R.layout.item_search_his, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FoodBean foodBean) {
        baseViewHolder.setText(R.id.item_tv_his, foodBean.name);
    }
}
