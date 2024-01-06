package com.kangsk.detox.lockdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.utility.Lockdown;
import com.kangsk.detox.utility.LockdownManager;
import com.kangsk.detox.utility.MonitorService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class LockdownFragment extends Fragment {

    /*
     * PRIVATE FIELDS
     */
    private Context mApplicationContext;
    private FragmentManager mFragmentManager;
    private LockdownManager mLockdownManager;

    private RecyclerView mLockdownFragmentRecyclerView;
    private LockdownAdapter mLockdownAdapter;

    /*
     * onCreate: called when the Fragment is created, responsible for instantiating fields.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationContext = getActivity().getApplicationContext();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mLockdownManager = LockdownManager.getInstance(mApplicationContext);

        mLockdownAdapter = new LockdownAdapter(mApplicationContext, mFragmentManager, mLockdownManager);

            Intent monitorServiceIntent = new Intent(mApplicationContext, MonitorService.class);
            getActivity().startService(monitorServiceIntent);

            /*
             * TEST ONLY
             */
            ArrayList<String> blacklistedApps = new ArrayList<>();
            blacklistedApps.add("com.chess");
            Lockdown lockdown = new Lockdown("My Coolest Lockdown", true, LocalTime.now(), LocalTime.parse("23:59:00"), new int[]{Calendar.MONDAY, Calendar.TUESDAY, Calendar.THURSDAY}, blacklistedApps);
            mLockdownManager.addLockdown(lockdown);
    }

    /*
     * onCreateView: called when the fragment's view is created. Responsible for inflating the newly
     * created view with the appropriate layout resource.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lockdown, container, false);
    }

    /*
     * onViewCreated: called once the fragment's view is inflated. Responsible for creating the LockdownFragment's
     * RecyclerView, setting its LayoutManager, and setting its adapter.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mLockdownFragmentRecyclerView = view.findViewById(R.id.recycler_lockdown_fragment);
        mLockdownFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownFragmentRecyclerView.setAdapter(mLockdownAdapter);
    }
}