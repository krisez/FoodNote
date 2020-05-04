package app.food.note.ui;

import android.content.Intent;
import android.os.Bundle;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.db.RxDbManager;
import app.food.note.adapter.SectionsPagerAdapter;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private QMUITopBar mTopBar;
    private ArrayList<FoodBean> mPeriodList = new ArrayList<>();
    private boolean isPeriodEntry;//是否进入了过期页面
    private ImageView mRedPoint;
    private SectionsPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(mPagerAdapter);
        QMUITabSegment tabs = findViewById(R.id.tab);
        tabs.setIndicator(new QMUITabIndicator(1,false,false));
        QMUITabBuilder builder = tabs.tabBuilder().setColor(getResources().getColor(R.color.grey),getResources().getColor(R.color.light_blue));
        tabs.addTab(builder.setText(getString(R.string.tab_ice_box)).build(this));
        tabs.addTab(builder.setText(getString(R.string.tab_dry_box)).build(this));
        tabs.addTab(builder.setText(getString(R.string.tab_seasoning_box)).build(this));
        tabs.addTab(builder.setText(getString(R.string.tab_normal_zone)).build(this));
        tabs.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                viewPager.setCurrentItem(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
        tabs.setupWithViewPager(viewPager,false);
        tabs.setMode(QMUITabSegment.MODE_FIXED);
        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle(R.string.app_name);
        setupNotify();
    }

    //过期时间提醒
    private void setupNotify() {
        ImageView notify = findViewById(R.id.notify_time);
        mRedPoint = findViewById(R.id.notify_point);
        notify.setOnClickListener(v->{
            isPeriodEntry = true;
            mRedPoint.setVisibility(View.GONE);
            startActivity(new Intent(this,WillPeriodActivity.class).putParcelableArrayListExtra("list", mPeriodList));
        });
        findViewById(R.id.notify_add).setOnClickListener(v->{
            startActivityForResult(new Intent(this,FoodDetailActivity.class),100);
        });
        refreshPeriod();
    }

    private void refreshPeriod(){
        Disposable d = RxDbManager.getInstance().queryPeriod().subscribe(foodBeans -> {
            if(!foodBeans.isEmpty()){
                mRedPoint.setVisibility(View.VISIBLE);
            }else{
                mRedPoint.setVisibility(View.GONE);
            }
            mPeriodList = foodBeans;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPeriodEntry){
            isPeriodEntry = false;
            refreshPeriod();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String area = data != null ? data.getStringExtra("area") : "";
            mPagerAdapter.refreshData(area);
        }
    }
}