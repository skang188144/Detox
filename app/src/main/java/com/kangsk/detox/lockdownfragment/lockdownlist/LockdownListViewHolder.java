package com.kangsk.detox.lockdownfragment.lockdownlist;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

public class LockdownListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /*
     * PRIVATE FIELDS
     */
    private final RecyclerView mLockdownListRecyclerView;
    private final RecyclerView.Adapter mLockdownListAdapter;
    private final MaterialButton mLockdownCreationButton;
    private final LockdownManager mLockdownManager;
    private final FragmentManager mFragmentManager;
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public LockdownListViewHolder(View itemView, LockdownManager lockdownManager, FragmentManager fragmentManager, Context applicationContext) {
        super(itemView);

        mLockdownManager = lockdownManager;
        mFragmentManager = fragmentManager;
        mApplicationContext = applicationContext;
        mLockdownListAdapter = new LockdownListAdapter(mLockdownManager, mApplicationContext);

        mLockdownListRecyclerView = itemView.findViewById(R.id.recycler_view_lockdown_fragment_lockdown_list);
        mLockdownListRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mLockdownListRecyclerView.setAdapter(mLockdownListAdapter);

        mLockdownCreationButton = itemView.findViewById(R.id.button_lockdown_creation);
        mLockdownCreationButton.setOnClickListener(this);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {
    }

    @Override
    public void onClick(View view) {
        showLockdownEditFragment(mFragmentManager);
    }

    private void showLockdownEditFragment(FragmentManager fragmentManager) {
        fragmentManager
                .beginTransaction()
                .hide(fragmentManager.findFragmentByTag(String.valueOf(R.id.menu_lockdown)))
                .show(fragmentManager.findFragmentByTag("LOCKDOWNCREATIONDIALOGFRAGMENT"))
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
    }
}
