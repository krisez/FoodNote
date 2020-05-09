package app.food.note.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.adapter.FoodAdapter;
import app.food.note.db.RxDbManager;
import io.reactivex.disposables.Disposable;

//调料区
public class SeasonActivity extends AppCompatActivity {

    private FoodAdapter mAdapter;
    private List<FoodBean> mFoodBeanList = new ArrayList<>();
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_area);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        QMUITopBar topBar = findViewById(R.id.topbar);
        topBar.setTitle(R.string.seasoning_box);
        topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        RecyclerView recyclerView = findViewById(R.id.rv_food_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter = new FoodAdapter(mFoodBeanList,this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setEmptyView(R.layout.empty_view);
        mAdapter.setOnItemClickListener((adapter, v, position) -> startActivityForResult(new Intent(this, FoodDetailActivity.class).putExtra("bean", mFoodBeanList.get(position)), 100));
        mRefreshLayout = findViewById(R.id.srl_refresh);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.qmui_config_color_red),
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.qmui_config_color_blue));
        mRefreshLayout.setOnRefreshListener(this::refreshData);
        refreshData();

        findViewById(R.id.other_ll).setBackgroundResource(R.mipmap.bg_tiaoliao);
    }

    private void refreshData() {
        Disposable d = RxDbManager.getInstance().query(getString(R.string.seasoning_box))
                .subscribe(foodBeans -> {
                    mFoodBeanList.clear();
                    mFoodBeanList.addAll(foodBeans);
                    mAdapter.notifyDataSetChanged();
                    Log.d("SeasonActivity", "refreshData: " + foodBeans.toString());
                    mRefreshLayout.setRefreshing(false);
                }, throwable -> mRefreshLayout.setRefreshing(false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mRefreshLayout.setRefreshing(true);
            refreshData();
        }
    }
}
