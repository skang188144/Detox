package com.kangsk.detox.home.overview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.utility.UsageDataManager;

import java.time.Duration;
import java.util.Calendar;
import java.util.TimeZone;

public class OverviewViewHolder extends RecyclerView.ViewHolder {

    /*
     * PRIVATE FIELDS
     */
    private final UsageDataManager mUsageDataManager;
    private final TextView mGreetingText;
    private final TextView mUsageTimeForTodayText;
    private final Calendar mCalendar;
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields.
     */
    public OverviewViewHolder(View itemView, UsageDataManager usageDataManager, Context applicationContext) {
        super(itemView);

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeZone(TimeZone.getDefault());

        mUsageDataManager = usageDataManager;
        mApplicationContext = applicationContext;
        mGreetingText = itemView.findViewById(R.id.text_overview_item_greeting);
        mUsageTimeForTodayText = itemView.findViewById(R.id.text_overview_item_usage_time);
    }

    /*
     * bindModel: method responsible for retrieving the necessary data and supplying it
     * to the appropriate Views.
     */
    public void bindModel() {
        setmGreetingText();
        setmUsageTimeForTodayText();
    }

    /*
     * setmGreetingText: set mGreetingText's text based on the current time
     */
    private void setmGreetingText() {
        int hourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 5 && hourOfDay <= 11) {    // 5AM - 11AM: "Good Morning"
            mGreetingText.setText(R.string.text_overview_greeting_morning);
        } else if (hourOfDay >= 12 && hourOfDay <= 17) {    // 12PM - 5PM: "Good Afternoon"
            mGreetingText.setText(R.string.text_overview_greeting_afternoon);
        } else if ((hourOfDay >= 18 && hourOfDay <= 23) || (hourOfDay >= 0 && hourOfDay <= 4)) {    // 6PM - 4AM: "Good Evening"
            mGreetingText.setText(R.string.text_overview_greeting_evening);
        }
    }

    /*
     *  setmUsageTimeForTodayText: retrieve today's total usage hours, and transform
     *  it into a "x hrs y min" format
     */
    private void setmUsageTimeForTodayText() {
        Duration usageTimeForToday = Duration.ofMillis(mUsageDataManager.getUsageTimeForDay(mCalendar.getTime()));
        long hours = usageTimeForToday.toHoursPart();
        long minutes = usageTimeForToday.toMinutesPart();

        if (hours != 0) {
            mUsageTimeForTodayText.setText(hours + " hr " + minutes + " min");
        } else {
            mUsageTimeForTodayText.setText(minutes + " min");
        }
    }
}
