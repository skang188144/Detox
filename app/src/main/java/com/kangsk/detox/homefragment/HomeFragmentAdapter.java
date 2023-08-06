package com.kangsk.detox.homefragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;

public class HomeFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // CONSTANTS
    private static final int ITEM_TYPE_GREETING = 10;   // RecyclerView item viewType constants
    private static final int ITEM_TYPE_GRAPH_OVERVIEW = 11;

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
            case ITEM_TYPE_GRAPH_OVERVIEW:
                return new GraphOverviewViewHolder(inflater.inflate(R.layout.row_overview_graph_fragment_home, parent, false));
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
        } else if (holder instanceof GraphOverviewViewHolder) {
            ((GraphOverviewViewHolder) holder).bindModel();
        }
    }

    /*
     * the number of RecyclerView items. This method must return a positive int, or
     * else the adapter will not run its appropriate methods.
     */
    @Override
    public int getItemCount() {
        return 2;
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
            case 1:
                return ITEM_TYPE_GRAPH_OVERVIEW;
            default:
                throw new RuntimeException();
        }
    }

}
