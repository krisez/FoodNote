package app.food.note.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import app.food.note.R;
import app.food.note.adapter.SectionsPagerAdapter;

//冰箱页面，有三个fragment
public class IceBoxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_box);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        QMUITabSegment tabs = findViewById(R.id.tab);
        tabs.setIndicator(new QMUITabIndicator(1,false,false));
        QMUITabBuilder builder = tabs.tabBuilder().setColor(getResources().getColor(R.color.grey),getResources().getColor(R.color.light_blue));
        tabs.addTab(builder.setText(getString(R.string.tab_cold_storage)).build(this));
        tabs.addTab(builder.setText(getString(R.string.tab_soft_freeze)).build(this));
        tabs.addTab(builder.setText(getString(R.string.tab_freezing)).build(this));
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
        QMUITopBar topBar = findViewById(R.id.topbar);
        topBar.setTitle(R.string.ice_box);
        topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
}
