package com.kangsk.detox.lockdownfragment.lockdownlist;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

public class LockdownListViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final RecyclerView mLockdownListRecyclerView;
    private final RecyclerView.Adapter mLockdownListAdapter;
    private final LockdownManager mLockdownManager;
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public LockdownListViewHolder(View itemView, LockdownManager lockdownManager, Context applicationContext) {
        super(itemView);

        mLockdownManager = lockdownManager;
        mApplicationContext = applicationContext;
        mLockdownListAdapter = new LockdownListAdapter(mLockdownManager, mApplicationContext);

        mLockdownListRecyclerView = itemView.findViewById(R.id.recycler_view_lockdown_fragment_lockdown_list);
        mLockdownListRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownListRecyclerView.setAdapter(mLockdownListAdapter);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {

    }
}
