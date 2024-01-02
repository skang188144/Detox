package com.kangsk.detox.home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kangsk.detox.R;
import com.kangsk.detox.utility.UsageDataManager;

public class HomeFragment extends Fragment {

    /*
     * PRIVATE FIELDS
     */
    private RecyclerView mHomeFragmentRecyclerView;
    private HomeFragmentAdapter mHomeFragmentAdapter;
    private Context mApplicationContext;
    private boolean mOnCreateCalled = false;    // used to track if onResume() is being called as the app is starting, or is resuming from a paused state

    /*
     * onCreate: called when the Fragment is created. Responsible for instantiating multiple fields.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationContext = getActivity().getApplicationContext();
        mHomeFragmentAdapter = new HomeFragmentAdapter(mApplicationContext);
        mOnCreateCalled = true;     // set mOnCreateCalled to true, to avoid needlessly updating the data in onResume()
    }

    /*
     * onCreateView: called when the Fragment's View is created. Responsible for inflating the newly
     * created View with the appropriate XML layout resource.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /*
     * onViewCreated: called once the Fragment's View is inflated. Responsible for creating the HomeFragment's
     * RecyclerView, setting its LayoutManager, and setting its Adapter.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHomeFragmentRecyclerView = view.findViewById(R.id.recycler_view_home_fragment);
        mHomeFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mHomeFragmentRecyclerView.setAdapter(mHomeFragmentAdapter);
    }

    /*
     * onResume: called after onCreate(), or after the application comes from a paused state to a resumed state.
     * If this is the latter situation, then this method will update the Adapter's data (UsageDataManager).
     */
    @Override
    public void onResume() {
        super.onResume();

        /*
         * only update the adapter data if onResume() was called without onCreate().
         * A data update is not needed as the adapter will receive fresh data if it's
         * being created for the first time.
         */
        if (!mOnCreateCalled) {
            mHomeFragmentAdapter.updateUsageDataManager(new UsageDataManager(mApplicationContext));
            mHomeFragmentRecyclerView.setAdapter(mHomeFragmentAdapter);
        }
    }
}