package app.food.note.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.Utils;
import app.food.note.db.RxDbManager;
import io.reactivex.disposables.Disposable;

//所有的食物适配器
public class FoodAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> implements DraggableModule {
    private FoodBean mConsumeBean;

    public FoodAdapter(List<FoodBean> data, Context context) {
        super(R.layout.item_food, data);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(QMUIDisplayHelper.sp2px(context, 24));
        paint.setAntiAlias(true);
        //开启侧滑功能
        getDraggableModule().setSwipeEnabled(true);
        getDraggableModule().setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                mConsumeBean = getData().get(pos);//暂存避免删除错误
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                //删除后处理数据库
                if (mConsumeBean != null) {
                    Disposable d = RxDbManager.getInstance().consumeFood(mConsumeBean).subscribe(consume -> {
                        Log.d("FoodAdapter", "食用:" + consume);
                    });
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(Color.RED);
                canvas.drawText("已食用/丢弃", 50, 100, paint);
            }
        });
        getDraggableModule().getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.START);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, FoodBean foodBean) {
        baseViewHolder.setText(R.id.item_name, foodBean.name);
        String time = foodBean.createTime + "购买，保质期至" + foodBean.period.split("/")[0];
        baseViewHolder.setText(R.id.item_time, time);
        if (foodBean.getLeftDays() <= 0) {
            baseViewHolder.setText(R.id.item_left_days, "已过期！");
        } else {
            baseViewHolder.setText(R.id.item_left_days, "剩余" + foodBean.getLeftDays() + "天");
        }
        if (foodBean.getLeftDays() <= 3) {
            baseViewHolder.setTextColor(R.id.item_left_days, Color.RED);
        }
        Glide.with(getContext()).load(foodBean.photo).placeholder(R.mipmap.logo).into((ImageView) baseViewHolder.getView(R.id.item_photo));
    }
}
