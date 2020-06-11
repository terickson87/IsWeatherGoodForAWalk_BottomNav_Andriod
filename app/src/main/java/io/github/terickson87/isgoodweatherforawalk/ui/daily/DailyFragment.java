package io.github.terickson87.isgoodweatherforawalk.ui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import io.github.terickson87.isgoodweatherforawalk.R;

public class DailyFragment extends Fragment {

    private DailyViewModel mDailyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDailyViewModel = new ViewModelProvider(this).get(DailyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_daily, container, false);

        final TextView textView = root.findViewById(R.id.text_notifications);

        mDailyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}