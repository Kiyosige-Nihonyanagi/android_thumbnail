package com.example.myapplication.thumbnail.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.thumbnail.MainActivity;
import com.example.myapplication.thumbnail.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> items = new ArrayList<>();
        createData(items);
        TextView textView = view.findViewById(R.id.volume_text);
        Spinner spinner = view.findViewById(R.id.volume_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.requireContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(10);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                textView.setText(item);
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        view.findViewById(R.id.backButton).setOnClickListener(v -> deleteFragment());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void deleteFragment() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().hide(this).commit();
        }
    }

    private void createData(List<String> items) {
        for (int value = 10; value <= 200; value += 10) {
            items.add(value + "%");
        }
    }
}
