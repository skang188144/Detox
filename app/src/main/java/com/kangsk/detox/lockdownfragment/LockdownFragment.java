package com.kangsk.detox.lockdownfragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kangsk.detox.R;

public class LockdownFragment extends Fragment {

    private RecyclerView mLockdownFragmentRecyclerView;
    private LockdownFragmentAdapter mLockdownFragmentAdapter;
    private Context mApplicationContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationContext = getActivity().getApplicationContext();
        mLockdownFragmentAdapter = new LockdownFragmentAdapter(mApplicationContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lockdown, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mLockdownFragmentRecyclerView = view.findViewById(R.id.recycler_view_lockdown);
        mLockdownFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownFragmentRecyclerView.setAdapter(mLockdownFragmentAdapter);
    }
}