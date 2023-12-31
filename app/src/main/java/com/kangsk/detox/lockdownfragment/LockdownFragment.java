package com.kangsk.detox.lockdownfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.Lockdown;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;
import com.kangsk.detox.lockdownfragment.utility.MonitorService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class LockdownFragment extends Fragment {

    /*
     * PRIVATE FIELDS
     */
    private RecyclerView mLockdownFragmentRecyclerView;
    private LockdownFragmentAdapter mLockdownFragmentAdapter;
    private LockdownManager mLockdownManager;
    private FragmentManager mFragmentManager;
    private Context mApplicationContext;

    /*
     * onCreate: called when the Fragment is created, responsible for instantiating fields.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationContext = getActivity().getApplicationContext();
        mLockdownManager = LockdownManager.getInstance(mApplicationContext);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mLockdownFragmentAdapter = new LockdownFragmentAdapter(mLockdownManager, mFragmentManager, mApplicationContext);

        Intent monitorServiceIntent = new Intent(mApplicationContext, MonitorService.class);
        getActivity().startService(monitorServiceIntent);

        /*
         * TEST ONLY
         */
        ArrayList<String> blacklistedApps = new ArrayList<>();
        blacklistedApps.add("com.chess");
        Lockdown lockdown = new Lockdown("My Coolest Lockdown", true, LocalTime.now(), LocalTime.parse("20:30:00"), new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.THURSDAY}, blacklistedApps);
        mLockdownManager.addLockdown(lockdown);
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