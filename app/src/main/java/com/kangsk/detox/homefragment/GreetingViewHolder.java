package com.kangsk.detox.homefragment;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;

/*
 * a ViewHolder object responsible for integrating the model data into
 * an individual RecyclerView item. This particular ViewHolder is for
 * the "Good Morning" / "Good Afternoon" view at the very top of the
 * home fragment.
 */
class GreetingViewHolder extends RecyclerView.ViewHolder {

    // PRIVATE NON-STATIC FIELDS
    private final TextView greetingText;    // either "Good Morning" or "Good Afternoon"

    /*
     * the constructor, with parameter itemView. This is the View that
     * represents a RecyclerView item. This ViewHolder is responsible
     * for integrating the appropriate model data into this itemView.
     */
    public GreetingViewHolder(View itemView) {
        super(itemView);

        // get a reference of the TextView widget
        greetingText = itemView.findViewById(R.id.text_greeting);
    }

    // method to bind the model data into the itemView.
    public void bindModel() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        int AM_PM = calendar.get(Calendar.AM_PM);

        if (AM_PM == Calendar.AM) {
            greetingText.setText(R.string.greeting_morning);
        } else {
            greetingText.setText(R.string.greeting_afternoon);
        }
    }
}
