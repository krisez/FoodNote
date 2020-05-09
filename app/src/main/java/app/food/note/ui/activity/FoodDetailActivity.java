package app.food.note.ui.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.Utils;
import app.food.note.db.RxDbManager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class FoodDetailActivity extends AppCompatActivity {

    private FoodBean mFoodBean;
    private String photo = "";
    private ImageView mPhoto;
    private TextView mTvPeriod;
    private TextView mTvArea;
    private TextView mTvType;
    private EditText mName;
    private EditText mNote;
    private int beanType;
    private boolean INSERT;
    private String zone = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        QMUITopBar topBar = findViewById(R.id.topbar);
        topBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        mFoodBean = getIntent().getParcelableExtra("bean");

        mName = findViewById(R.id.detail_name);
        mNote = findViewById(R.id.detail_note);
        mPhoto = findViewById(R.id.detail_photo);
        mTvPeriod = findViewById(R.id.detail_period);
        mTvArea = findViewById(R.id.detail_area);
        mTvType = findViewById(R.id.detail_type);

        if (mFoodBean != null) {
            if (INSERT = (getIntent().getIntExtra("insert", 0) == 1)) {
                topBar.setTitle("新增");
            } else {
                topBar.setTitle("修改");
                mTvPeriod.setText(mFoodBean.period);
            }
            mName.setText(mFoodBean.name);
            mNote.setText(mFoodBean.note);
            if (!mFoodBean.photo.isEmpty()) {
                Glide.with(this).load(mFoodBean.photo).into(mPhoto);
            }
            photo = mFoodBean.photo;
            mTvType.setText(mFoodBean.getType());
            beanType = mFoodBean.type;
            zone = mFoodBean.zone;
            String s = mFoodBean.area;
            if (!zone.equals("")) {
                s += "," + zone;
            }
            mTvArea.setText(s);
        } else {
            topBar.setTitle("新增");
        }

        findViewById(R.id.detail_area_iv).setOnClickListener(v -> new QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(getString(R.string.ice_box))
                .addItem(getString(R.string.dry_box))
                .addItem(getString(R.string.seasoning_box))
                .addItem(getString(R.string.normal_zone))
                .setGravityCenter(true)
                .setTitle("存放区域")
                .setAddCancelBtn(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    if (position == 0) {
                        new QMUIBottomSheet.BottomListSheetBuilder(this)
                                .addItem(getString(R.string.tab_cold_storage))
                                .addItem(getString(R.string.tab_soft_freeze))
                                .addItem(getString(R.string.tab_freezing))
                                .setGravityCenter(true)
                                .setTitle("冰箱")
                                .setOnSheetItemClickListener((dialog1, itemView1, position1, tag1) -> {
                                    zone = tag1;
                                    mTvArea.setText(tag + "," + zone);
                                    dialog1.dismiss();
                                    dialog.dismiss();
                                }).build().show();
                    } else {
                        mTvArea.setText(tag);
                        dialog.dismiss();
                    }
                })
                .build().show());

        topBar.addRightTextButton("完成", R.id.topbar_detail_sure).setOnClickListener(v -> {
            if (mName.getText().toString().isEmpty()) {
                Toast.makeText(this, "名称为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTvPeriod.getText().toString().isEmpty()) {
                Toast.makeText(this, "保质期为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTvArea.getText().toString().isEmpty()) {
                Toast.makeText(this, "区域为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            FoodBean bean;
            if (mFoodBean != null) {
                bean = mFoodBean;
            } else {
                bean = new FoodBean();
            }
            bean.name = mName.getText().toString();
            bean.period = mTvPeriod.getText().toString();
            bean.photo = photo;
            bean.note = mNote.getText().toString();
            if (mTvArea.getText().toString().contains(",")) {
                bean.area = mTvArea.getText().toString().split(",")[0];
            } else {
                bean.area = mTvArea.getText().toString();
            }
            bean.type = beanType;
            bean.zone = zone;
            Observable<Boolean> observable;
            if (mFoodBean != null && !INSERT) {
                observable = RxDbManager.getInstance().update(bean);
            } else {
                observable = RxDbManager.getInstance().insert(bean);
            }
            Disposable d = observable.subscribe(aBoolean -> {
                if (aBoolean) {
                    setResult(RESULT_OK, new Intent().putExtra("area", bean.area));
                    finish();
                }
            }, throwable -> Toast.makeText(this, "增加/修改失败！", Toast.LENGTH_SHORT).show());
        });

        mPhoto.setOnClickListener(v -> {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            List<String> list = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String permission : permissions) {
                    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        list.add(permission);
                    }
                }
                if (list.isEmpty()) {
                    matisse();
                } else {
                    requestPermissions(list.toArray(new String[0]), 200);
                }
            } else {
                matisse();
            }
        });

        findViewById(R.id.detail_period_iv).setOnClickListener(v -> {
            if (!topBar.getTitle().equals("修改")) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    Calendar c = Calendar.getInstance();
                    c.set(year,month,dayOfMonth);
                    if (calendar.after(c)){
                        Toast.makeText(this, "时间不能早于今日！", Toast.LENGTH_SHORT).show();
                    }else{
                        mTvPeriod.setText(Utils.period(year, month, dayOfMonth));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            } else {
                Toast.makeText(this, "不支持修改~", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.detail_type).setOnClickListener(v -> {
            new QMUIBottomSheet.BottomListSheetBuilder(this)
                    .addItem("冷冻冷藏")
                    .addItem("粮油干货")
                    .addItem("肉类生鲜")
                    .addItem("蔬菜")
                    .addItem("调料酱料")
                    .setGravityCenter(true)
                    .setTitle("标签")
                    .setAddCancelBtn(true)
                    .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                        mTvType.setText(tag);
                        beanType = position;
                        dialog.dismiss();
                    }).build().show();
        });
    }

    private void matisse() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(1)
                .originalEnable(true)
                .maxOriginalSize(10)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, getPackageName() + ".fileprovider", "food"))
                .thumbnailScale(0.85f)
                .autoHideToolbarOnSingleTap(true)
                .imageEngine(new GlideEngine())
                .forResult(100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            List<String> list = Matisse.obtainPathResult(data);
            photo = list.get(0);
            Glide.with(this).load(photo).into(mPhoto);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            int j = 0;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    j++;
                } else {
                    break;
                }
            }
            if (j < grantResults.length) {
                Toast.makeText(this, "授权失败！", Toast.LENGTH_SHORT).show();
            } else {
                matisse();
            }
        }
    }
}
