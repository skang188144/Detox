package com.kangsk.detox.lockdownfragment.lockdownlist.lockdowneditfragment.option_blacklisted_apps;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;

public class LockdownBlacklistedAppsEditOptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public LockdownBlacklistedAppsEditOptionViewHolder(View itemView) {
        super(itemView);

        itemView.findViewById(R.id.card_view_lockdown_edit_options_blacklisted_apps_option).setOnClickListener(this);
    }

    public void bindModel() {

    }

    @Override
    public void onClick(View view) {
        Log.d("LockdownBlacklistedAppsEditOptionViewHolder", "Listener: onClickListener Triggered");
    }
}
