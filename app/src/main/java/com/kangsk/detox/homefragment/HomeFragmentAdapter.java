package com.kangsk.detox.homefragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kangsk.detox.R;
import com.kangsk.detox.utility.UsageDataManager;
import com.kangsk.detox.homefragment.applist.AppListViewHolder;
import com.kangsk.detox.homefragment.graph.GraphViewHolder;
import com.kangsk.detox.homefragment.overview.OverviewViewHolder;

class HomeFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /*
     * CONSTANTS
     */
    private static final int ITEM_TYPE_OVERVIEW = 10;   // viewType constants used to differentiate each ViewHolder for each RecyclerView position
    private static final int ITEM_TYPE_GRAPH = 11;
    private static final int ITEM_TYPE_APP_LIST = 12;

    /*
     * PRIVATE FIELDS
     */
    private UsageDataManager mUsageDataManager;     // this field is not final because it must be updated when the HomeFragment enters a resumed state
    private final Context mApplicationContext;

    /*
     * CONSTRUCTOR: responsible for injecting and instantiating fields
     */
    public HomeFragmentAdapter(Context applicationContext) {
        mApplicationContext = applicationContext;
        mUsageDataManager = new UsageDataManager(mApplicationContext);
    }

    /*
     * onCreateViewHolder: when the HomeFragmentAdapter is instantiated and begins to
     * create a ViewHolder, this method will be called to retrieve a custom ViewHolder object.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // create a different type of ViewHolder object based on this RecyclerView position's item viewType.
        switch (viewType) {
            case ITEM_TYPE_OVERVIEW:
                return new OverviewViewHolder(inflater.inflate(R.layout.item_home_fragment_overview, parent, false), mUsageDataManager, mApplicationContext);
            case ITEM_TYPE_GRAPH:
                return new GraphViewHolder(inflater.inflate(R.layout.item_home_fragment_graph, parent, false), mUsageDataManager, mApplicationContext);
            case ITEM_TYPE_APP_LIST:
                return new AppListViewHolder(inflater.inflate(R.layout.item_home_fragment_app_list, parent, false), mUsageDataManager, mApplicationContext);
            default:
                throw new RuntimeException("HomeFragmentAdapter.java encountered an exception while building its ViewHolders. This ViewHolder type does not exist.");
        }
    }

    /*
     * onBindViewHolder: this method is called when the system needs to retrieve data
     * for this adapter's ViewHolders. It calls the ViewHolder objects' bindModel()
     * method, which is responsible for supplying that data.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // checks the ViewHolder type, and calls the appropriate bindModel() method
        if (holder instanceof OverviewViewHolder) {
            ((OverviewViewHolder) holder).bindModel();
        } else if (holder instanceof GraphViewHolder) {
            ((GraphViewHolder) holder).bindModel();
        } else if (holder instanceof AppListViewHolder) {
            ((AppListViewHolder) holder).bindModel();
        }
    }

    /*
     * getItemCount: returns the number of RecyclerView items. This method must return
     * a non-zero positive int, or else the adapter will not run its appropriate methods.
     */
    @Override
    public int getItemCount() {
        return 3;
    }

    /*
     * getItemViewType: returns an int itemView, representing the RecyclerView item type
     * constant for every position in the RecyclerView list.
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_TYPE_OVERVIEW;
            case 1:
                return ITEM_TYPE_GRAPH;
            case 2:
                return ITEM_TYPE_APP_LIST;
            default:
                throw new RuntimeException("HomeFragmentAdapter.java encountered an exception while retrieving the itemView type. This ViewHolder type does not exist.");
        }
    }

    /*
     * updateUsageDataManager: refreshes the data source (UsageDataManager) of this adapter,
     * for instances where the user has resumed the app from a paused state. Without this
     * method, the user would see outdated data retrieved when the user initially opened
     * the app.
     */
    public void updateUsageDataManager(UsageDataManager usageDataManager) {
        mUsageDataManager = usageDataManager;
    }

}
