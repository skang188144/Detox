package com.kangsk.detox.homefragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.kangsk.detox.R;
import com.kangsk.detox.UsageDataManager;

import java.util.Calendar;

class GraphViewHolder extends RecyclerView.ViewHolder {

    // PRIVATE NON-STATIC FIELDS
    private final UsageDataManager mUsageDataManager;
    private final BarChart mBarChart;
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

        mBarChart = itemView.findViewById(R.id.barchart_screen_hours_today);
        mScreenOnHoursText = itemView.findViewById(R.id.text_screen_hours_today);
    }

    // method to bind the model data into the itemView.
    public void bindModel() {
        Calendar calendar = Calendar.getInstance();

        String text = mUsageDataManager.getScreenOnTime(calendar.getTime(), Calendar.HOUR, mScreenOnHoursText.getContext()) + " screen hours today";
        mScreenOnHoursText.setText(text);

        BarData data = mUsageDataManager.getBarDataForDay(calendar.getTime(), mBarChart.getContext());
        mBarChart.setData(data);

        LimitLine limitLine = new LimitLine(0.5f);
        limitLine.setLineWidth(0.32f);
        limitLine.setLineColor(mBarChart.getResources().getColor(R.color.gray, mBarChart.getContext().getTheme()));

        mBarChart.setDrawBorders(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.getDescription().setEnabled(false);
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
