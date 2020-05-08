package app.food.note.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.food.note.R;
import app.food.note.ui.activity.FoodDetailActivity;
import app.food.note.ui.activity.SearchFoodActivity;

public class InsertFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert, container, false);
        view.findViewById(R.id.insert_cold_freeze).setOnClickListener(v -> startActivity(new Intent(getContext(), SearchFoodActivity.class).putExtra("type", 0)));
        view.findViewById(R.id.insert_dry).setOnClickListener(v -> startActivity(new Intent(getContext(), SearchFoodActivity.class).putExtra("type", 1)));
        view.findViewById(R.id.insert_meat).setOnClickListener(v -> startActivity(new Intent(getContext(), SearchFoodActivity.class).putExtra("type", 2)));
        view.findViewById(R.id.insert_vegetables).setOnClickListener(v -> startActivity(new Intent(getContext(), SearchFoodActivity.class).putExtra("type", 3)));
        view.findViewById(R.id.insert_tiaoliao).setOnClickListener(v -> startActivity(new Intent(getContext(), SearchFoodActivity.class).putExtra("type", 4)));
        view.findViewById(R.id.insert_more).setOnClickListener(v -> startActivity(new Intent(getContext(), FoodDetailActivity.class)));
        return view;
    }
}
