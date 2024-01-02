package com.kangsk.detox.home.applist;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.utility.UsageDataManager;

public class AppListViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final UsageDataManager mUsageDataManager;
    private final Context mApplicationContext;
    private final RecyclerView mAppListRecyclerView;
    private final RecyclerView.Adapter mAppListAdapter;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public AppListViewHolder(View itemView, UsageDataManager usageDataManager, Context applicationContext) {
        super(itemView);

        mUsageDataManager = usageDataManager;
        mApplicationContext = applicationContext;
        mAppListAdapter = new AppListAdapter(mUsageDataManager, mApplicationContext);

        mAppListRecyclerView = itemView.findViewById(R.id.recycler_app_list_item); // inject the nested RecyclerView, and set its LayoutManager and Adapter.
        mAppListRecyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
        mAppListRecyclerView.setAdapter(mAppListAdapter);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {
    }
}
