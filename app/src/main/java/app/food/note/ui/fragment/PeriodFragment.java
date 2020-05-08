package app.food.note.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.adapter.FoodAdapter;
import app.food.note.db.RxDbManager;
import app.food.note.ui.activity.FoodDetailActivity;
import io.reactivex.disposables.Disposable;

public class PeriodFragment extends Fragment {

    private SwipeRefreshLayout mRefreshLayout;
    private FoodAdapter mAdapter;
    private List<FoodBean> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragemtn_period, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRefreshLayout = view.findViewById(R.id.srl_period);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.qmui_config_color_red),
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.qmui_config_color_blue));
        RecyclerView recyclerView = view.findViewById(R.id.rv_period);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new FoodAdapter(list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((a, v, position) -> {
            Intent intent = new Intent(getContext(), FoodDetailActivity.class);
            if (list != null && !list.isEmpty()) {
                intent.putExtra("bean", list.get(position));
            }
            startActivity(intent);
        });
        mAdapter.setEmptyView(R.layout.empty_view);
        mRefreshLayout.setOnRefreshListener(this::getList);
        getList();
    }

    private void getList() {
        Disposable d = RxDbManager.getInstance().queryPeriod().subscribe(foodBeans -> {
            mAdapter.setNewInstance(foodBeans);
            mRefreshLayout.setRefreshing(false);
        }, throwable -> {
            Toast.makeText(getContext(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
            mRefreshLayout.setRefreshing(false);
        });
    }
}
