package com.kangsk.detox.lockdown.lockdownlist;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.kangsk.detox.MainActivity;
import com.kangsk.detox.R;
import com.kangsk.detox.utility.LockdownManager;

public class LockdownListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /*
     * PRIVATE FIELDS
     */
    private final Context mApplicationContext;
    private final FragmentManager mFragmentManager;
    private final LockdownManager mLockdownManager;

    private final RecyclerView mLockdownListRecyclerView;
    private final RecyclerView.Adapter mLockdownListRecyclerViewAdapter;
    private final MaterialButton mLockdownEditorButton;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public LockdownListViewHolder(View itemView, Context applicationContext, FragmentManager fragmentManager, LockdownManager lockdownManager) {
        super(itemView);

        mApplicationContext = applicationContext;
        mLockdownManager = lockdownManager;
        mFragmentManager = fragmentManager;

        mLockdownListRecyclerView = itemView.findViewById(R.id.recycler_lockdown_list_item);
        mLockdownListRecyclerViewAdapter = new LockdownListAdapter(mLockdownManager);

        mLockdownListRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownListRecyclerView.setAdapter(mLockdownListRecyclerViewAdapter);

        mLockdownEditorButton = itemView.findViewById(R.id.button_lockdown_list_item_add);
        mLockdownEditorButton.setOnClickListener(this);
    }

    /*
     * onClick: listener called when the 'add lockdown' button is clicked.
     */
    @Override
    public void onClick(View view) {
        switchToLockdownEditorFragment();
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {
    }

    /*
     * switchToLockdownEditorFragment: hides the lockdown fragment and displays the lockdown editor fragment.
     */
    private void switchToLockdownEditorFragment() {
        mFragmentManager
                .beginTransaction()
                .hide(mFragmentManager.findFragmentByTag(MainActivity.LOCKDOWN_TAG))
                .show(mFragmentManager.findFragmentByTag(MainActivity.LOCKDOWN_EDITOR_TAG))
                .addToBackStack(null)
                .commit();
        mFragmentManager.executePendingTransactions();
    }
}
