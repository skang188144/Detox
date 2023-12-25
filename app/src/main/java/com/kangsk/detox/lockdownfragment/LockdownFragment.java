package com.kangsk.detox.lockdownfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;
import com.kangsk.detox.lockdownfragment.utility.MonitorService;

public class LockdownFragment extends Fragment {

    /*
     * PRIVATE FIELDS
     */
    private RecyclerView mLockdownFragmentRecyclerView;
    private LockdownFragmentAdapter mLockdownFragmentAdapter;
    private LockdownManager mLockdownManager;
    private Context mApplicationContext;

    /*
     * onCreate: called when the Fragment is created, responsible for instantiating fields.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLockdownManager = LockdownManager.getInstance(mApplicationContext);
        mApplicationContext = getActivity().getApplicationContext();
        mLockdownFragmentAdapter = new LockdownFragmentAdapter(mLockdownManager, mApplicationContext);

        Intent monitorServiceIntent = new Intent(mApplicationContext, MonitorService.class);
        getActivity().startService(monitorServiceIntent);
    }

    /*
     * onCreateView: called when the Fragment's View is created. Responsible for inflating the newly
     * created View with the appropriate XML layout resource.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lockdown, container, false);
    }

    /*
     * onViewCreated: called once the Fragment's View is inflated. Responsible for creating the LockdownFragment's
     * RecyclerView, setting its LayoutManager, and setting its Adapter.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mLockdownFragmentRecyclerView = view.findViewById(R.id.recycler_view_lockdown);
        mLockdownFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownFragmentRecyclerView.setAdapter(mLockdownFragmentAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}