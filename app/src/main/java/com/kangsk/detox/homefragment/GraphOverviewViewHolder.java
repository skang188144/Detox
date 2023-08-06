package com.kangsk.detox.homefragment;

import android.annotation.SuppressLint;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.kangsk.detox.R;
import com.kangsk.detox.ScreenOnTimeManager;

import java.util.Calendar;

class GraphOverviewViewHolder extends RecyclerView.ViewHolder {

    // PRIVATE NON-STATIC FIELDS
    private final ScreenOnTimeManager mScreenOnTimeManager;
    private final BarChart mScreenOnHoursBarChart;
    private final TextView mScreenOnHoursText;

    /*
     * the constructor, with parameter itemView. This is the View that
     * represents a RecyclerView item. This ViewHolder is responsible
     * for integrating the appropriate model data into this itemView.
     */
    public GraphOverviewViewHolder(View itemView) {
        super(itemView);

        // instantiate fields using the RecyclerView item View
        mScreenOnTimeManager = new ScreenOnTimeManager();

        mScreenOnHoursBarChart = itemView.findViewById(R.id.barchart_usage_hours_today);
        mScreenOnHoursText = itemView.findViewById(R.id.text_usage_hours_today);
    }

    // method to bind the model data into the itemView.
    @SuppressLint("SetTextI18n")
    public void bindModel() {
        mScreenOnHoursText.setText(mScreenOnTimeManager.getScreenOnTimeOfToday(Calendar.MINUTE, mScreenOnHoursText.getContext()) + " minutes");
    }
}
