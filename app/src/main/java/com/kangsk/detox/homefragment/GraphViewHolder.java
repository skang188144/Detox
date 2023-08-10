package com.kangsk.detox.homefragment;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.kangsk.detox.R;
import com.kangsk.detox.RoundedBarChart;
import com.kangsk.detox.UsageDataManager;

import java.util.Calendar;

/**
 * TODO:
 * 1. Comment everywhere that need comments
 * 2. Extract graph code from bindModel()
 */

class GraphViewHolder extends RecyclerView.ViewHolder {

    // PRIVATE NON-STATIC FIELDS
    private final UsageDataManager mUsageDataManager;
    private final RoundedBarChart mBarChart;
    private final TextView mScreenOnHoursText;

    /*
     * the constructor, with parameter itemView. This is the View that
     * represents a RecyclerView item. This ViewHolder is responsible
     * for integrating the appropriate model data into this itemView.
     */
    public GraphViewHolder(View itemView) {
        super(itemView);

        // instantiate fields using the RecyclerView item View
        mUsageDataManager = new UsageDataManager();

        mBarChart = itemView.findViewById(R.id.barchart_usage_hours_today);
        mScreenOnHoursText = itemView.findViewById(R.id.text_usage_hours_today);
    }

    // method to bind the model data into the itemView.
    public void bindModel() {
        Calendar calendar = Calendar.getInstance();

        String text = mUsageDataManager.getUsageTime(calendar.getTime(), Calendar.HOUR, mScreenOnHoursText.getContext()) + " hours";
        mScreenOnHoursText.setText(text);

        BarData data = mUsageDataManager.getUsageBarData(calendar.getTime(), mBarChart.getContext());
        mBarChart.setData(data);

        LimitLine limitLine = new LimitLine(0.5f);
        limitLine.setLineWidth(0.32f);
        limitLine.setLineColor(mBarChart.getResources().getColor(R.color.gray, mBarChart.getContext().getTheme()));

        Typeface basicSans = ResourcesCompat.getFont(mBarChart.getContext(), R.font.basic_sans_thin);

        mBarChart.setRadius(15);
        mBarChart.setDrawBorders(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setBackgroundColor(mBarChart.getResources().getColor(R.color.white, mBarChart.getContext().getTheme()));
        mBarChart.getXAxis().setTypeface(basicSans);
        mBarChart.getAxisLeft().setTypeface(basicSans);
        mBarChart.getXAxis().setAxisMaximum(24.0f);
        mBarChart.getXAxis().setLabelCount(6);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getAxisLeft().setDrawAxisLine(false);
        mBarChart.getAxisLeft().addLimitLine(limitLine);
        mBarChart.getAxisLeft().setDrawLimitLinesBehindData(true);
        mBarChart.getAxisLeft().setAxisMaximum(1.0f);
        mBarChart.getAxisLeft().setAxisMinimum(0.0f);
        mBarChart.getAxisLeft().setLabelCount(2, true);
    }
}
