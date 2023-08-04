package com.kangsk.detox;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HomeFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // CONSTANT: greeting item viewType
    private static final int ITEM_TYPE_GREETING = 10;

    /*
     * after an instance of this adapter class is instantiated, this adapter will create a
     * ViewHolder object. This ViewHolder object is responsible for integrating the model data
     * into an individual RecyclerView item.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create LayoutInflater instance
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        /*
         * create a different type of ViewHolder object based on viewType. viewType
         * is set by the method below--getItemViewType(). That method will assign a
         * different viewType for each position, determined by what order we want
         * to display our various RecyclerView items.
         */
        switch (viewType) {
            // the "Good Morning" or "Good Afternoon" at the top of the home fragment
            case ITEM_TYPE_GREETING:
                return new GreetingViewHolder(inflater.inflate(R.layout.row_greeting_fragment_home, parent, false));

            default:
                throw new RuntimeException();
        }
    }

    /*
     * method responsible for feeding the appropriate ViewHolder the necessary data,
     * and to call the ViewHolder's bind method. The bind method will integrate the
     * raw data into a properly encapsulated View, which serves as a RecyclerView item.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GreetingViewHolder) {
            ((GreetingViewHolder) holder).bindModel();
        }
    }

    /*
     * the number of RecyclerView items. This method must return a positive int, or
     * else the adapter will not run its appropriate methods.
     */
    @Override
    public int getItemCount() {
        return 1;
    }

    /*
     * returns a different RecyclerView item type constant for every position
     * in the RecyclerView list.
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_TYPE_GREETING;

            default:
                throw new RuntimeException();
        }
    }

    /*
     * a ViewHolder object responsible for integrating the model data into
     * an individual RecyclerView item. This particular ViewHolder is for
     * the "Good Morning" / "Good Afternoon" view at the very top of the
     * home fragment.
     */
    public class GreetingViewHolder extends RecyclerView.ViewHolder {
        // either "Good Morning" or "Good Afternoon"
        private final TextView greetingText;

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
                greetingText.setText("Good Morning,");
            } else {
                greetingText.setText("Good Afternoon,");
            }
        }
    }
}
