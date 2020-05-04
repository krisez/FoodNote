package app.food.note.ui;

import android.content.Intent;
import android.os.Bundle;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.adapter.FoodAdapter;

public class WillPeriodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_will_period);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        QMUITopBar topBar = findViewById(R.id.topbar);
        topBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        topBar.setTitle("过期提醒");
        List<FoodBean> list = getIntent().getParcelableArrayListExtra("list");
        RecyclerView recyclerView = findViewById(R.id.rv_period);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        FoodAdapter adapter = new FoodAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((a, view, position) -> {
            Intent intent = new Intent(this, FoodDetailActivity.class);
            if (list != null && !list.isEmpty()) {
                intent.putExtra("bean", list.get(position));
            }
            startActivity(intent);
        });
        adapter.setEmptyView(R.layout.empty_view);
    }
}
