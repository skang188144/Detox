package com.kangsk.detox.lockdownfragment.lockdownlist.lockdowneditfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.kangsk.detox.R;

public class LockdownEditFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lockdown_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialButton button = view.findViewById(R.id.test_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        getParentFragmentManager().popBackStack();
    }
}
