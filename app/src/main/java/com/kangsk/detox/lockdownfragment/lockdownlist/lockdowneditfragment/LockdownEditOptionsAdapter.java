package com.kangsk.detox.lockdownfragment.lockdownlist.lockdowneditfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdownfragment.currentlockdown.CurrentLockdownViewHolder;
import com.kangsk.detox.lockdownfragment.lockdownlist.LockdownListViewHolder;
import com.kangsk.detox.lockdownfragment.lockdownlist.lockdowneditfragment.option_blacklisted_apps.LockdownBlacklistedAppsEditOptionViewHolder;
import com.kangsk.detox.lockdownfragment.lockdownlist.lockdowneditfragment.option_name.LockdownNameEditOptionViewHolder;
import com.kangsk.detox.lockdownfragment.lockdownlist.lockdowneditfragment.option_repeat_days.LockdownRepeatDaysEditOptionViewHolder;
import com.kangsk.detox.lockdownfragment.title.LockdownTitleViewHolder;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;

public class LockdownEditOptionsAdapter extends RecyclerView.Adapter {
    private static final int ITEM_TYPE_REPEAT_DAYS_OPTION = 16;
    private static final int ITEM_TYPE_NAME_OPTION = 17;
    private static final int ITEM_TYPE_BLACKLISTED_APPS_OPTION = 18;

    private final LockdownManager mLockdownManager;
    private final Context mApplicationContext;

    public LockdownEditOptionsAdapter(LockdownManager lockdownManager, Context applicationContext) {
        mLockdownManager = lockdownManager;
        mApplicationContext = applicationContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM_TYPE_REPEAT_DAYS_OPTION:
                return new LockdownRepeatDaysEditOptionViewHolder(inflater.inflate(R.layout.item_lockdown_edit_options_repeat_days_option, parent, false));
            case ITEM_TYPE_NAME_OPTION:
                return new LockdownNameEditOptionViewHolder(inflater.inflate(R.layout.item_lockdown_edit_options_name_option, parent, false));
            case ITEM_TYPE_BLACKLISTED_APPS_OPTION:
                return new LockdownBlacklistedAppsEditOptionViewHolder(inflater.inflate(R.layout.item_lockdown_edit_options_blacklisted_apps_option, parent, false));
            default:
                throw new RuntimeException("LockdownEditOptionsAdapter.java encountered an exception while building its ViewHolders. This ViewHolder type does not exist.");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LockdownRepeatDaysEditOptionViewHolder) {
            ((LockdownRepeatDaysEditOptionViewHolder) holder).bindModel();
        } else if (holder instanceof LockdownNameEditOptionViewHolder) {
            ((LockdownNameEditOptionViewHolder) holder).bindModel();
        } else if (holder instanceof LockdownBlacklistedAppsEditOptionViewHolder) {
            ((LockdownBlacklistedAppsEditOptionViewHolder) holder).bindModel();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_TYPE_REPEAT_DAYS_OPTION;
            case 1:
                return ITEM_TYPE_NAME_OPTION;
            case 2:
                return ITEM_TYPE_BLACKLISTED_APPS_OPTION;
            default:
                throw new RuntimeException("LockdownEditOptionsAdapter.java encountered an exception while retrieving the itemView type. This ViewHolder type does not exist.");
        }
    }
}
