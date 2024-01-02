package com.kangsk.detox.home.graph;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.kangsk.detox.R;
import com.kangsk.detox.utility.RoundedBarChart;
import com.kangsk.detox.utility.UsageDataManager;

import java.util.Calendar;
import java.util.TimeZone;

public class GraphViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final UsageDataManager mUsageDataManager;
    private final RoundedBarChart mBarChart;
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public GraphViewHolder(View itemView, UsageDataManager usageDataManager, Context applicationContext) {
        super(itemView);

        mUsageDataManager = usageDataManager;
        mApplicationContext = applicationContext;
        mBarChart = itemView.findViewById(R.id.barchart_graph_item);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {
        configureBarChart();
    }

    /*
     * configureBarChart: method responsible for retrieving/applying the appropriate
     * graph data, and for styling the graph.
     */
    public void configureBarChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        Typeface font = ResourcesCompat.getFont(mApplicationContext, R.font.basic_sans_thin);

        BarData data = mUsageDataManager.getUsageBarData(calendar.getTime());   // grab the data and supply it to the BarChart object
        mBarChart.setData(data);

        LimitLine limitLine = new LimitLine(0.5f);  // add another x-axis line
        limitLine.setLineWidth(0.32f);
        limitLine.setLineColor(mBarChart.getResources().getColor(R.color.neutral_color_off, mApplicationContext.getTheme()));

        // GENERAL CONFIGURATION
        mBarChart.setRadius(15);
        mBarChart.setDrawBorders(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setBackgroundColor(mBarChart.getResources().getColor(R.color.neutral_color, mApplicationContext.getTheme()));
        mBarChart.setDoubleTapToZoomEnabled(false);
        mBarChart.setScaleEnabled(false);

        // X-AXIS CONFIGURATION
        mBarChart.getXAxis().setTypeface(font);
        mBarChart.getXAxis().setAxisMaximum(23.0f);
        mBarChart.getXAxis().setLabelCount(6);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return (int) value + "hr";
            }
        });

        // RIGHT Y-AXIS CONFIGURATION
        mBarChart.getAxisRight().setEnabled(false);

        // LEFT X-AXIS CONFIGURATION
        mBarChart.getAxisLeft().setTypeface(font);
        mBarChart.getAxisLeft().setDrawAxisLine(false);
        mBarChart.getAxisLeft().addLimitLine(limitLine);
        mBarChart.getAxisLeft().setDrawLimitLinesBehindData(true);
        mBarChart.getAxisLeft().setAxisMaximum(60.0f);
        mBarChart.getAxisLeft().setAxisMinimum(0.0f);
        mBarChart.getAxisLeft().setLabelCount(2, true);
        mBarChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return (int) value + "m";
            }
        });
    }
}
