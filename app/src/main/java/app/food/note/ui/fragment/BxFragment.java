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
import app.food.note.ui.activity.DryBoxActivity;
import app.food.note.ui.activity.IceBoxActivity;
import app.food.note.ui.activity.NormalActivity;
import app.food.note.ui.activity.SeasonActivity;

public class BxFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bingxiang,container,false);
        view.findViewById(R.id.ice_box).setOnClickListener(v->{startActivity(new Intent(getContext(), IceBoxActivity.class));});
        view.findViewById(R.id.dry_box).setOnClickListener(v->{startActivity(new Intent(getContext(), DryBoxActivity.class));});
        view.findViewById(R.id.season_box).setOnClickListener(v->{startActivity(new Intent(getContext(), SeasonActivity.class));});
        view.findViewById(R.id.normal_box).setOnClickListener(v->{startActivity(new Intent(getContext(), NormalActivity.class));});
        return view;
    }
}
