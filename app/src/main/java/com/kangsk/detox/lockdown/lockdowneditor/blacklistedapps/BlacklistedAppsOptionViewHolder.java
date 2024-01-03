package com.kangsk.detox.lockdown.lockdowneditor.blacklistedapps;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;

public class BlacklistedAppsOptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public BlacklistedAppsOptionViewHolder(View itemView) {
        super(itemView);

        itemView.findViewById(R.id.card_blacklisted_apps_item).setOnClickListener(this);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {

    }

    @Override
    public void onClick(View view) {
        Log.d("BlacklistedAppsOptionViewHolder", "Listener: onClickListener Triggered");
    }
}
