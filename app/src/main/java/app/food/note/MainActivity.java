package app.food.note;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import app.food.note.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private QMUITopBar mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        QMUITabSegment tabs = findViewById(R.id.tab);
        tabs.setIndicator(new QMUITabIndicator(1,false,false));
        tabs.setupWithViewPager(viewPager);
        tabs.setMode(QMUITabSegment.MODE_FIXED);
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
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle(R.string.app_name);
        setupNotify();
    }

    //过期时间提醒
    private void setupNotify() {
        ConstraintLayout layout = (ConstraintLayout) View.inflate(this,R.layout.view_notify_time,null);
        mTopBar.addRightView(layout,R.id.topbar_right_view,mTopBar.generateTopBarImageButtonLayoutParams());
        ImageView notify = layout.findViewById(R.id.notify_time);
        ImageView redPoint = layout.findViewById(R.id.notify_point);
        notify.setOnClickListener(v->{
            startActivity(new Intent(this,WillPeriodActivity.class));
            redPoint.setVisibility(View.GONE);
        });
        layout.findViewById(R.id.notify_add).setOnClickListener(v->{
            startActivity(new Intent(this,FoodDetailActivity.class));
        });
    }
}