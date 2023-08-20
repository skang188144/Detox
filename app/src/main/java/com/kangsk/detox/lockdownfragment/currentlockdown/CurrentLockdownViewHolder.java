package com.kangsk.detox.lockdownfragment.currentlockdown;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;

public class CurrentLockdownViewHolder extends RecyclerView.ViewHolder {

    private final TextView timeRemaining;

    public CurrentLockdownViewHolder(View itemView) {
        super(itemView);
        timeRemaining = itemView.findViewById(R.id.text_current_lockdown_time_remaining);
    }

    public void bindModel() {
        timeRemaining.setText("0h 0m");
    }
}
