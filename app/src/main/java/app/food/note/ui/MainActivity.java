package app.food.note.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import app.food.note.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BxFragment mBxFragment;
    private InsertFragment mInsertFragment;
    private PeriodFragment mPeriodFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mBxFragment = new BxFragment();
        mInsertFragment = new InsertFragment();
        mPeriodFragment = new PeriodFragment();
        findViewById(R.id.iv_save).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        findViewById(R.id.insert_food).setOnClickListener(this);
        findViewById(R.id.iv_guoqi).setOnClickListener(this);
        findViewById(R.id.tv_guoqi).setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, mBxFragment, BxFragment.class.getSimpleName()).commit();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mBxFragment).hide(mInsertFragment).hide(mPeriodFragment);
        switch (v.getId()) {
            case R.id.iv_save:
            case R.id.tv_save:
                mBxFragment = (BxFragment) getSupportFragmentManager().findFragmentByTag(BxFragment.class.getSimpleName());
                if (mBxFragment == null) {
                    mBxFragment = new BxFragment();
                }
                transaction.show(mBxFragment);
                break;
            case R.id.iv_guoqi:
            case R.id.tv_guoqi:
                mPeriodFragment = (PeriodFragment) getSupportFragmentManager().findFragmentByTag(PeriodFragment.class.getSimpleName());
                if (mPeriodFragment == null) {
                    mPeriodFragment = new PeriodFragment();
                }
                if(mPeriodFragment.isAdded()){
                    transaction.show(mPeriodFragment);
                }else{
                    transaction.add(R.id.frame_container,mPeriodFragment,PeriodFragment.class.getSimpleName());
                }
                break;
            case R.id.insert_food:
                mInsertFragment = (InsertFragment) getSupportFragmentManager().findFragmentByTag(InsertFragment.class.getSimpleName());
                if (mInsertFragment == null) {
                    mInsertFragment = new InsertFragment();
                }
                if(mInsertFragment.isAdded()){
                    transaction.show(mInsertFragment);
                }else{
                    transaction.add(R.id.frame_container,mInsertFragment,InsertFragment.class.getSimpleName());
                }
                break;
        }
        transaction.commit();

    }
}