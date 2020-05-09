package app.food.note.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import app.food.note.R;
import app.food.note.ui.fragment.BxFragment;
import app.food.note.ui.fragment.InsertFragment;
import app.food.note.ui.fragment.PeriodFragment;

//主页面,app的首页
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BxFragment mBxFragment;
    private InsertFragment mInsertFragment;
    private PeriodFragment mPeriodFragment;
    private TextView mTitle;
    private ImageView mIvSave;
    private TextView mTvSave;
    private ImageView mIvPeriod;
    private TextView mTvPeriod;
    private LinearLayout mainBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mBxFragment = new BxFragment();
        mInsertFragment = new InsertFragment();
        mPeriodFragment = new PeriodFragment();
        mainBg = findViewById(R.id.main_ll);
        mIvSave = findViewById(R.id.iv_save);
        mTvSave = findViewById(R.id.tv_save);
        mIvPeriod = findViewById(R.id.iv_guoqi);
        mTvPeriod = findViewById(R.id.tv_guoqi);
        mTitle = findViewById(R.id.main_title);
        mTitle.setText("我的厨房");
        findViewById(R.id.main_save).setOnClickListener(this);
        findViewById(R.id.main_guoqi).setOnClickListener(this);
        findViewById(R.id.insert_food).setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, mBxFragment, BxFragment.class.getSimpleName()).commit();
    }

    @Override
    public void onClick(View v) {
        resetView();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mBxFragment).hide(mInsertFragment).hide(mPeriodFragment);
        switch (v.getId()) {
            case R.id.main_save:
                mBxFragment = (BxFragment) getSupportFragmentManager().findFragmentByTag(BxFragment.class.getSimpleName());
                if (mBxFragment == null) {
                    mBxFragment = new BxFragment();
                }
                if (mBxFragment.isAdded()) {
                    transaction.show(mBxFragment);
                } else {
                    transaction.add(R.id.frame_container, mBxFragment, BxFragment.class.getSimpleName());
                }
                mTitle.setText("我的厨房");
                mTvSave.setTextColor(getResources().getColor(R.color.select_color));
                mIvSave.setImageResource(R.drawable.ic_bingxiang);
                mainBg.setBackgroundResource(R.mipmap.bg_main);
                break;
            case R.id.main_guoqi:
                mPeriodFragment = (PeriodFragment) getSupportFragmentManager().findFragmentByTag(PeriodFragment.class.getSimpleName());
                if (mPeriodFragment == null) {
                    mPeriodFragment = new PeriodFragment();
                }
                if (mPeriodFragment.isAdded()) {
                    transaction.show(mPeriodFragment);
                } else {
                    transaction.add(R.id.frame_container, mPeriodFragment, PeriodFragment.class.getSimpleName());
                }
                mTitle.setText("即将过期");
                mTvPeriod.setTextColor(getResources().getColor(R.color.select_color));
                mIvPeriod.setImageResource(R.drawable.ic_guoqi);
                mainBg.setBackgroundResource(R.mipmap.bg_guoqi);
                break;
            case R.id.insert_food:
                mInsertFragment = (InsertFragment) getSupportFragmentManager().findFragmentByTag(InsertFragment.class.getSimpleName());
                if (mInsertFragment == null) {
                    mInsertFragment = new InsertFragment();
                }
                if (mInsertFragment.isAdded()) {
                    transaction.show(mInsertFragment);
                } else {
                    transaction.add(R.id.frame_container, mInsertFragment, InsertFragment.class.getSimpleName());
                }
                mTitle.setText("添加");
                mainBg.setBackgroundResource(R.mipmap.bg_empty);
                break;
        }
        transaction.commit();
    }

    private void resetView() {
        mIvSave.setImageResource(R.drawable.ic_bingxiang_reset);
        mIvPeriod.setImageResource(R.drawable.ic_guoqi_reset);
        mTvSave.setTextColor(getResources().getColor(R.color.select_color_grey));
        mTvPeriod.setTextColor(getResources().getColor(R.color.select_color_grey));

    }
}