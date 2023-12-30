package com.kangsk.detox.lockdownfragment.lockdownlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

public class LockdownListAdapter extends RecyclerView.Adapter{

    /*
     * PRIVATE FIELDS
     */
    private final LockdownManager mLockdownManager;
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public LockdownListAdapter(LockdownManager lockdownManager, Context applicationContext) {
        mLockdownManager = lockdownManager;
        mApplicationContext = applicationContext;
    }

    /*
     * onCreateViewHolder: when the LockdownListAdapter is instantiated and begins to
     * create a ViewHolder, this method will be called to retrieve a custom ViewHolder object.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_lockdown_fragment_lockdown_list_lockdown, parent, false);
        return new LockdownViewHolder(itemView, mLockdownManager);
    }

    /*
     * onBindViewHolder: responsible for updating a ViewHolder based upon the model
     * data for a certain position.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((LockdownViewHolder) holder).bindModel(position);
    }

    /*
     * getItemCount: returns the number of RecyclerView items. This method must return
     * a non-zero positive int, or else the adapter will not run its appropriate methods.
     */
    @Override
    public int getItemCount() {
        return mLockdownManager.getLockdownList().size();
    }
}