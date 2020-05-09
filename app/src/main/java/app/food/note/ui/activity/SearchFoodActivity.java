package app.food.note.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.List;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.adapter.SearchAdapter;
import app.food.note.adapter.SearchHisAdapter;
import app.food.note.db.DBHelper;
import app.food.note.db.RxDbManager;
import io.reactivex.disposables.Disposable;

//搜索内容，根据type（标签）的不同选择不同查询方式
public class SearchFoodActivity extends AppCompatActivity {
    private List<FoodBean> mSearchList = new ArrayList<>();
    private List<FoodBean> mTemp = new ArrayList<>();
    private List<FoodBean> mHisList = new ArrayList<>();
    private SearchAdapter mSearchAdapter;//搜索结果adapter
    private SearchHisAdapter mHisAdapter;//搜索历史adapter,grid列表
    private RecyclerView mSearchRecycler;
    private RecyclerView mHistoryRecycler;
    private EditText mEditText;
    private LinearLayout mTips;

    private int TYPE = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_search);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        findViewById(R.id.back).setOnClickListener(v -> finish());
        TYPE = getIntent().getIntExtra("type", -1);
        mEditText = findViewById(R.id.et_search);
        mTips = findViewById(R.id.search_his_tips);
        //查询该类型下的食物
        Disposable d1 = RxDbManager.getInstance().querySearchType(TYPE).subscribe(beans -> {
            mSearchList.addAll(beans);
        }, t -> Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show());
        //搜索结果
        mSearchAdapter = new SearchAdapter(mTemp);
        mSearchRecycler = findViewById(R.id.rv_search_result);
        mSearchRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mSearchRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSearchRecycler.setAdapter(mSearchAdapter);
        mSearchAdapter.setEmptyView(R.layout.empty_search);
        //搜索历史
        //查询该类型下的历史记录
        Disposable d2 = RxDbManager.getInstance().querySearchHistory(TYPE).subscribe(beans -> {
            mHisList.addAll(beans);
        }, t -> Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show());
        mHisAdapter = new SearchHisAdapter(mHisList);
        mHistoryRecycler = findViewById(R.id.rv_search_history);
        mHistoryRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        mHistoryRecycler.setAdapter(mHisAdapter);
        mHisAdapter.setEmptyView(R.layout.empty_search_history);
        adapterListener();
        initViewListener();
    }

    private void initViewListener() {
        findViewById(R.id.search_his_delete).setOnClickListener(v -> {
            new QMUIDialog.MessageDialogBuilder(this).setMessage("确定清空历史记录么？")
                    .addAction("确定", (dialog, index) -> {
                        RxDbManager.getInstance().clear(DBHelper.SEARCH_TABLE_NAME,TYPE);
                        mHisList.clear();
                        mHisAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    })
                    .addAction("取消", (dialog, index) -> dialog.dismiss())
                    .show();
        });

        findViewById(R.id.search_insert_food).setOnClickListener(v -> {
            startActivityForResult(new Intent(this, FoodDetailActivity.class), 100);
        });
    }

    private void adapterListener() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTips.setVisibility(View.GONE);
                mSearchRecycler.setVisibility(View.VISIBLE);
                mHistoryRecycler.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTemp.clear();
                for (int i = 0; i < mSearchList.size(); i++) {
                    if (mSearchList.get(i).name.contains(s)) {
                        mTemp.add(mSearchList.get(i));
                    }
                }
                mSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mTips.setVisibility(View.VISIBLE);
                    mSearchRecycler.setVisibility(View.GONE);
                    mHistoryRecycler.setVisibility(View.VISIBLE);
                }
            }
        });
        mSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            Disposable d = RxDbManager.getInstance().insertHistory(TYPE, mTemp.get(position).id).subscribe(aBoolean -> {
                if (aBoolean) {
                    startActivityForResult(new Intent(this, FoodDetailActivity.class).putExtra("bean", mTemp.get(position)).putExtra("insert", 1), 200);
                }
            });
        });
        mHisAdapter.setOnItemClickListener((adapter, view, position) -> {
            startActivityForResult(new Intent(this, FoodDetailActivity.class).putExtra("bean", mHisList.get(position)).putExtra("insert", 1), 200);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
