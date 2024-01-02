package com.kangsk.detox.lockdown.lockdowneditor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.lockdown.lockdowneditor.blacklistedapps.BlacklistedAppsOptionViewHolder;
import com.kangsk.detox.lockdown.lockdowneditor.name.NameOptionViewHolder;
import com.kangsk.detox.lockdown.lockdowneditor.preventchanges.PreventChangesOptionViewHolder;
import com.kangsk.detox.lockdown.lockdowneditor.repeatdays.RepeatDaysOptionViewHolder;
import com.kangsk.detox.lockdown.lockdowneditor.tamper.TamperOptionViewHolder;
import com.kangsk.detox.utility.LockdownManager;

public class LockdownEditorAdapter extends RecyclerView.Adapter {

    /*
     * CONSTANTS
     */
    // RecyclerView Item Types
    private static final int ITEM_TYPE_REPEAT_DAYS_OPTION = 16;
    private static final int ITEM_TYPE_NAME_OPTION = 17;
    private static final int ITEM_TYPE_BLACKLISTED_APPS_OPTION = 18;
    private static final int ITEM_TYPE_TAMPER_OPTION = 19;
    private static final int ITEM_TYPE_PREVENT_CHANGES_OPTION = 20;

    /*
     * PRIVATE FIELDS
     */
    private final Context mApplicationContext;
    private final LockdownManager mLockdownManager;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public LockdownEditorAdapter(Context applicationContext, LockdownManager lockdownManager) {
        mApplicationContext = applicationContext;
        mLockdownManager = lockdownManager;
    }

    /*
     * onCreateViewHolder: when the LockdownEditorAdapter is instantiated and begins to
     * create a ViewHolder, this method will be called to retrieve a custom ViewHolder object.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // create a different type of ViewHolder object based on this RecyclerView position's item viewType.
        switch (viewType) {
            case ITEM_TYPE_REPEAT_DAYS_OPTION:
                return new RepeatDaysOptionViewHolder(inflater.inflate(R.layout.item_lockdown_editor_fragment_repeat_days, parent, false));
            case ITEM_TYPE_NAME_OPTION:
                return new NameOptionViewHolder(inflater.inflate(R.layout.item_lockdown_editor_fragment_name, parent, false));
            case ITEM_TYPE_BLACKLISTED_APPS_OPTION:
                return new BlacklistedAppsOptionViewHolder(inflater.inflate(R.layout.item_lockdown_editor_fragment_blacklisted_apps, parent, false));
            case ITEM_TYPE_TAMPER_OPTION:
                return new TamperOptionViewHolder(inflater.inflate(R.layout.item_lockdown_editor_fragment_tamper, parent, false));
            case ITEM_TYPE_PREVENT_CHANGES_OPTION:
                return new PreventChangesOptionViewHolder(inflater.inflate(R.layout.item_lockdown_editor_fragment_tamper, parent, false));
                // TODO: create XML layout resource for PreventChangesOptionViewHolder
            default:
                throw new RuntimeException("LockdownEditorAdapter.java encountered an exception while building its ViewHolders. This ViewHolder type does not exist.");
        }
    }

    /*
     * onBindViewHolder: this method is called when the system needs to retrieve data
     * for this adapter's ViewHolders. It calls the ViewHolder objects' bindModel()
     * method, which is responsible for supplying that data.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RepeatDaysOptionViewHolder) {
            ((RepeatDaysOptionViewHolder) holder).bindModel();
        } else if (holder instanceof NameOptionViewHolder) {
            ((NameOptionViewHolder) holder).bindModel();
        } else if (holder instanceof BlacklistedAppsOptionViewHolder) {
            ((BlacklistedAppsOptionViewHolder) holder).bindModel();
        } else if (holder instanceof TamperOptionViewHolder) {
            ((TamperOptionViewHolder) holder).bindModel();
        } else if (holder instanceof PreventChangesOptionViewHolder) {
            ((PreventChangesOptionViewHolder) holder).bindModel();
        }
    }

    /*
     * getItemCount: returns the number of RecyclerView items. This method must return
     * a non-zero positive int, or else the adapter will not run its appropriate methods.
     */
    @Override
    public int getItemCount() {
        return 5;
    }

    /*
     * getItemViewType: returns an int itemView, representing the RecyclerView item type
     * constant for every position in the RecyclerView list.
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_TYPE_REPEAT_DAYS_OPTION;
            case 1:
                return ITEM_TYPE_NAME_OPTION;
            case 2:
                return ITEM_TYPE_BLACKLISTED_APPS_OPTION;
            case 3:
                return ITEM_TYPE_TAMPER_OPTION;
            case 4:
                return ITEM_TYPE_PREVENT_CHANGES_OPTION;
            default:
                throw new RuntimeException("LockdownEditorAdapter.java encountered an exception while retrieving the itemView type. This ViewHolder type does not exist.");
        }
    }
}
