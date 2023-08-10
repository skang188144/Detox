package com.kangsk.detox.homefragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.AppUsageInfo;
import com.kangsk.detox.R;
import com.kangsk.detox.UsageDataManager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppsViewHolder extends RecyclerView.ViewHolder {

    private final UsageDataManager mUsageDataManager;
    private final TextView mAppName;
    private final TextView mAppDailyUsageTime;

    private final ImageView mAppIcon;

    public AppsViewHolder(View itemView) {
        super(itemView);

        mUsageDataManager = new UsageDataManager();
        mAppName = itemView.findViewById(R.id.text_app_name);
        mAppDailyUsageTime = itemView.findViewById(R.id.text_app_usage_hours_today);
        mAppIcon = itemView.findViewById(R.id.image_app_icon);
    }

    public void bindModel() {
        Calendar calendar = Calendar.getInstance();
        HashMap<String, AppUsageInfo> appsUsageInfo = mUsageDataManager.getDailyAppsUsageInfo(calendar.getTime(), Calendar.HOUR, itemView.getContext().getApplicationContext());

        for (Map.Entry<String, AppUsageInfo> appsUsageInfoEntry : appsUsageInfo.entrySet()) {
            AppUsageInfo appUsageInfo = appsUsageInfoEntry.getValue();
            if (appUsageInfo.appName.equals("Notion")) {
                mAppName.setText("Notion");
                mAppDailyUsageTime.setText(appUsageInfo.appUsageTime/60000 + " minutes");
                mAppIcon.setImageDrawable(appUsageInfo.appIcon);
            }
        }
    }
}
