package app.food.note.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.db.RxDbManager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class FoodDetailActivity extends AppCompatActivity {

    private FoodBean mFoodBean;
    private String photo = "";
    private ImageView mPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        QMUITopBar topBar = findViewById(R.id.topbar);
        topBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        mFoodBean = getIntent().getParcelableExtra("bean");

        EditText name = findViewById(R.id.detail_name);
        EditText period = findViewById(R.id.detail_period);
        EditText note = findViewById(R.id.detail_note);
        TextView area = findViewById(R.id.detail_area);
        mPhoto = findViewById(R.id.detail_photo);

        if (mFoodBean != null) {
            topBar.setTitle("详情");
            name.setText(mFoodBean.name);
            period.setText(mFoodBean.period+"");
            note.setText(mFoodBean.note);
            if (!mFoodBean.photo.isEmpty()) {
                Glide.with(this).load(mFoodBean.photo).into(mPhoto);
            }
            area.setText(mFoodBean.area);

        } else {
            topBar.setTitle("新增");
        }

        area.setOnClickListener(v -> new QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem(getString(R.string.tab_ice_box))
                .addItem(getString(R.string.tab_dry_box))
                .addItem(getString(R.string.tab_seasoning_box))
                .addItem(getString(R.string.tab_normal_zone))
                .setGravityCenter(true)
                .setTitle("存放区域")
                .setAddCancelBtn(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    area.setText(tag);
                    dialog.dismiss();
                })
                .build().show());

        findViewById(R.id.detail_sure).setOnClickListener(v -> {
            if (name.getText().toString().isEmpty()) {
                Toast.makeText(this, "名称为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (period.getText().toString().isEmpty()) {
                Toast.makeText(this, "保质期为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (area.getText().toString().equals(getString(R.string.click_area))) {
                Toast.makeText(this, "区域为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            FoodBean bean;
            if (mFoodBean != null) {
                bean = mFoodBean;
            } else {
                bean = new FoodBean();
            }
            bean.name = name.getText().toString();
            bean.period = Integer.parseInt(period.getText().toString());
            bean.photo = photo;
            bean.note = note.getText().toString();
            bean.area = area.getText().toString();
            Observable<Boolean> observable;
            if (mFoodBean != null) {
                observable = RxDbManager.getInstance().update(bean);
            } else {
                observable = RxDbManager.getInstance().insert(bean);
            }
            Disposable d = observable.subscribe(aBoolean -> {
                if (aBoolean) {
                    setResult(RESULT_OK, new Intent().putExtra("area", bean.area));
                    finish();
                }
            },throwable -> Toast.makeText(this, "增加/修改失败！", Toast.LENGTH_SHORT).show());
        });

        mPhoto.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                } else {
                    matisse();
                }
            }
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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                matisse();
            } else {
                Toast.makeText(this, "授权失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
