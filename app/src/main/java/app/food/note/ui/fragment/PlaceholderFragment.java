package app.food.note.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import app.food.note.FoodBean;
import app.food.note.R;
import app.food.note.adapter.FoodAdapter;
import app.food.note.db.RxDbManager;
import app.food.note.ui.activity.FoodDetailActivity;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_AREA = "section_area";
    private String zone = "";
    private FoodAdapter mAdapter;
    private List<FoodBean> mFoodBeanList = new ArrayList<>();
    private SwipeRefreshLayout mRefreshLayout;

    public static PlaceholderFragment newInstance(String zone) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_AREA, zone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        assert getArguments() != null;
        zone = getArguments().getString(ARG_SECTION_AREA);
        return inflater.inflate(R.layout.fragment_ice_box, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rv_food);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter = new FoodAdapter(mFoodBeanList, getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setEmptyView(R.layout.empty_view);
        mAdapter.setOnItemClickListener((adapter, v, position) -> startActivityForResult(new Intent(getActivity(), FoodDetailActivity.class).putExtra("bean", mFoodBeanList.get(position)), 100));
        mRefreshLayout = view.findViewById(R.id.srl_refresh);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.qmui_config_color_red),
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.qmui_config_color_blue));
        mRefreshLayout.setOnRefreshListener(this::refreshData);
        refreshData();
    }

    public void refreshData() {
        Disposable d = RxDbManager.getInstance().queryIcebox(zone)
                .subscribe(foodBeans -> {
                    mFoodBeanList.clear();
                    mFoodBeanList.addAll(foodBeans);
                    mAdapter.notifyDataSetChanged();
                    Log.d("PlaceholderFragment", "  onViewCreated:" + foodBeans.toString());
                    mRefreshLayout.setRefreshing(false);
                }, throwable -> mRefreshLayout.setRefreshing(false));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            mRefreshLayout.setRefreshing(true);
            refreshData();
        }
    }
}