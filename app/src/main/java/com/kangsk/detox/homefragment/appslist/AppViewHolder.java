package com.kangsk.detox.homefragment.appslist;

import static java.lang.Math.round;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kangsk.detox.R;
import com.kangsk.detox.utility.AppUsageData;

import java.time.Duration;
import java.util.ArrayList;

public class AppViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final ArrayList<AppUsageData> mAppUsageDataList;
    private final ImageView mAppIcon;
    private final TextView mAppNameText;
    private final TextView mAppUsageTimeText;
    private final TextView mAppUsageTimePercentageText;
    private final LinearProgressIndicator mAppUsageTimePercentageBar;
    private final long mAppUsageTimeSum;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public AppViewHolder(View itemView, ArrayList<AppUsageData> appUsageDataList, long appTotalUsageTimeSum) {
        super(itemView);

        mAppUsageDataList = appUsageDataList;
        mAppIcon = itemView.findViewById(R.id.drawable_app_list_app_icon);
        mAppNameText = itemView.findViewById(R.id.text_app_list_app_name);
        mAppUsageTimeText = itemView.findViewById(R.id.text_app_list_app_usage_time);
        mAppUsageTimePercentageText = itemView.findViewById(R.id.text_app_list_app_usage_percentage);
        mAppUsageTimePercentageBar = itemView.findViewById(R.id.progressbar_app_list_app_usage_percentage);
        mAppUsageTimeSum = appTotalUsageTimeSum;
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel(int position) {
        AppUsageData appUsageData = mAppUsageDataList.get(position);

        mAppIcon.setImageDrawable(appUsageData.appIcon);
        mAppNameText.setText(appUsageData.appName);
        setmAppUsageTimeText(appUsageData);
        setmAppUsageTimePercentageText(appUsageData);
        setmAppUsageTimePercentageBar(appUsageData);
    }

    /*
     * setmAppUsageTimeText: retrieve today's usage time for a single app,
     * and transform it into a "x hrs y min" format.
     */
    private void setmAppUsageTimeText(AppUsageData appUsageData) {
        Duration appUsageTime = Duration.ofMillis(appUsageData.appUsageTime);
        long hours = appUsageTime.toHoursPart();
        long minutes = appUsageTime.toMinutesPart();

        if (hours != 0) {
            mAppUsageTimeText.setText(hours + "hr " + minutes + "m");
        } else {
            mAppUsageTimeText.setText(minutes + "m");
        }
    }

    /*
     * setmAppUsageTimePercentageText: calculates the percentage of total usage
     * time that this app has used, and set the corresponding TextView
     */
    private void setmAppUsageTimePercentageText(AppUsageData appUsageData) {
        int appTotalUsageTimePercentage = Math.round(((float) (appUsageData.appUsageTime) / mAppUsageTimeSum) * 100);
        mAppUsageTimePercentageText.setText(appTotalUsageTimePercentage + "%");
    }

    /*
     * setmAppUsageTimePercentageBar: calculates the percentage of total usage
     * time that this app has used, and set the corresponding ProgressBar position
     */
    private void setmAppUsageTimePercentageBar(AppUsageData appUsageData) {
        int appTotalUsageTimePercentage = Math.round(((float) (appUsageData.appUsageTime) / mAppUsageTimeSum) * 100);
        mAppUsageTimePercentageBar.setMin(0);
        mAppUsageTimePercentageBar.setMax(100);
        mAppUsageTimePercentageBar.setProgress(appTotalUsageTimePercentage);
    }
}
