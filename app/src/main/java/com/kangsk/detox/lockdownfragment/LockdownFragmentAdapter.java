package com.kangsk.detox.lockdownfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.currentlockdown.CurrentLockdownViewHolder;
import com.kangsk.detox.lockdownfragment.title.LockdownTitleViewHolder;

public class LockdownFragmentAdapter extends RecyclerView.Adapter {

    /*
     * CONSTANTS
     */
    private static final int ITEM_TYPE_TITLE = 13;
    private static final int ITEM_TYPE_CURRENT_LOCKDOWN = 14;

    /*
     * PRIVATE FIELDS
     */
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields
     */
    public LockdownFragmentAdapter(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    /*
     * onCreateViewHolder: when the LockdownFragmentAdapter is instantiated and begins to
     * create a ViewHolder, this method will be called to retrieve a custom ViewHolder object.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // create a different type of ViewHolder object based on this RecyclerView position's item viewType.
        switch (viewType) {
            case ITEM_TYPE_TITLE:
                return new LockdownTitleViewHolder(inflater.inflate(R.layout.item_lockdown_fragment_lockdown_title, parent, false));
            case ITEM_TYPE_CURRENT_LOCKDOWN:
                return new CurrentLockdownViewHolder(inflater.inflate(R.layout.item_lockdown_fragment_current_lockdown, parent, false), mApplicationContext);
            default:
                throw new RuntimeException("LockdownFragmentAdapter.java encountered an exception while building its ViewHolders. This ViewHolder type does not exist.");
        }
    }

    /*
     * onBindViewHolder: this method is called when the system needs to retrieve data
     * for this adapter's ViewHolders. It calls the ViewHolder objects' bindModel()
     * method, which is responsible for supplying that data.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LockdownTitleViewHolder) {
            ((LockdownTitleViewHolder) holder).bindModel();
        } else if (holder instanceof CurrentLockdownViewHolder) {
            ((CurrentLockdownViewHolder) holder).bindModel();
        }
    }

    /*
     * getItemCount: returns the number of RecyclerView items. This method must return
     * a non-zero positive int, or else the adapter will not run its appropriate methods.
     */
    @Override
    public int getItemCount() {
        return 2;
    }

    /*
     * getItemViewType: returns an int itemView, representing the RecyclerView item type
     * constant for every position in the RecyclerView list.
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_TYPE_TITLE;
            case 1:
                return ITEM_TYPE_CURRENT_LOCKDOWN;
            default:
                throw new RuntimeException("HomeFragmentAdapter.java encountered an exception while retrieving the itemView type. This ViewHolder type does not exist.");
        }
    }
}