package com.kangsk.detox.homefragment.appslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.utility.AppUsageData;
import com.kangsk.detox.R;
import com.kangsk.detox.utility.UsageDataManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /*
     * PRIVATE FIELDS
     */
    private final Context mApplicationContext;
    private final UsageDataManager mUsageDataManager;
    private final ArrayList<AppUsageData> mAppUsageDataList;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public AppListAdapter(UsageDataManager usageDataManager, Context applicationContext) {
        mApplicationContext = applicationContext;
        mUsageDataManager = usageDataManager;
        mAppUsageDataList = getAppUsageDataList();
    }

    /*
     * onCreateViewHolder: when the AppListAdapter is instantiated and begins to
     * create a ViewHolder, this method will be called to retrieve a custom ViewHolder object.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AppViewHolder(inflater.inflate(R.layout.item_home_fragment_app_list_app, parent, false), mAppUsageDataList); // return a AppViewHolder instance with an inflated View for a single app/row
    }

    /*
     * onBindViewHolder: responsible for updating a ViewHolder based upon the model
     * data for a certain position.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AppViewHolder) holder).bindModel(position);
    }

    /*
     * getItemCount: returns the number of RecyclerView items. This method must return
     * a non-zero positive int, or else the adapter will not run its appropriate methods.
     */
    @Override
    public int getItemCount() {
        return mAppUsageDataList.size();
    }

    /*
     * getAppUsageDataList: retrieves the list of app usage data from an instance
     * of UsageDataManager.
     */
    private ArrayList<AppUsageData> getAppUsageDataList() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        Date date = calendar.getTime();
        return mUsageDataManager.getUsageTimePerApp(date);
    }
}
