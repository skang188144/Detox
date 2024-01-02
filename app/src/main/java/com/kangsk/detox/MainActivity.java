package com.kangsk.detox;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kangsk.detox.home.HomeFragment;
import com.kangsk.detox.lockdown.LockdownFragment;
import com.kangsk.detox.lockdown.lockdowneditor.LockdownEditorFragment;
import com.kangsk.detox.settings.SettingsFragment;
import com.kangsk.detox.utility.LockdownManager;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    /*
     * CONSTANTS
     */
    // FRAGMENT TAGS
    public static final String HOME_TAG = "HOME_TAG";
    public static final String LOCKDOWN_TAG = "LOCKDOWN_TAG";
    public static final String LOCKDOWN_EDITOR_TAG = "EDIT_LOCKDOWN_TAG";
    public static final String SETTINGS_TAG = "SETTINGS_TAG";

    /*
     * PRIVATE FIELDS
     */
    private FragmentManager mFragmentManager;
    private BottomNavigationView mBottomNavigationView;

    /*
     * onCreate: called when the activity is created, responsible for instantiating fields.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remove top action bar by making the decorview stretch through the entire system window, ignoring system widgets
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        mFragmentManager = getSupportFragmentManager();
        mBottomNavigationView = findViewById(R.id.navigation_bar_main_activity_bottom);
        mBottomNavigationView.setOnItemSelectedListener(this);

        /*
         * at the creation of the activity, the user should land on the home fragment. The following
         * code creates the home fragment, attaches it to this activity, and adds it to the BackStack.
         */
        createFragment(HomeFragment.class, HOME_TAG, true, mFragmentManager);
        createFragment(LockdownFragment.class, LOCKDOWN_TAG, false, mFragmentManager);
        createFragment(SettingsFragment.class, SETTINGS_TAG, false, mFragmentManager);
        createFragment(LockdownEditorFragment.class, LOCKDOWN_EDITOR_TAG, false, mFragmentManager);
    }

    /*
     * onStart: called after onCreate(). This method is guaranteed to be called even when the user navigates
     * out of the activity, and then returns to it.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // when onStart() is called in the early stages of the activity lifecycle, LockdownManager should be instantiated
        LockdownManager.getInstance(this.getApplicationContext());
    }

    /*
     * onStart: called before onDestroy(). This method has a high chance of being called, even when
     * the app process is forcibly killed.
     */
    @Override
    protected void onStop() {
        super.onStop();

        // when onStop() is called in the last stages of the activity lifecycle, LockdownManager should write everything to storage and be destroyed
        LockdownManager.endInstance();
    }

    /*
     * onNavigationItemSelected: listener triggered when an item in the bottom
     * navigation bar is pressed. This method is responsible for switching/creating
     * the fragments based on the user's input in the navigation bar.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        Fragment visibleFragment = getVisibleFragment(mFragmentManager);

        // home item selected
        if (menuItemId == R.id.menu_home && !visibleFragment.getTag().equals(HOME_TAG)) {
            switchVisibleFragment(HOME_TAG, mFragmentManager);
            return true;
        // lockdown item selected
        } else if (menuItemId == R.id.menu_lockdown && !visibleFragment.getTag().equals(LOCKDOWN_TAG)) {
            switchVisibleFragment(LOCKDOWN_TAG, mFragmentManager);
            return true;
        // settings item selected
        } else if (menuItemId == R.id.menu_settings && !visibleFragment.getTag().equals(SETTINGS_TAG)) {
            switchVisibleFragment(SETTINGS_TAG, mFragmentManager);
            return true;
        }

        return false;
    }

    /*
     * createFragment: creates the specified Fragment subclass, adds an identifier
     * tag to the fragment, and hides it.
     */
    private void createFragment(Class<? extends Fragment> newFragmentClass, String newFragmentTag, boolean visible, FragmentManager fragmentManager) {
        // create new fragment
        fragmentManager
                .beginTransaction()
                .add(R.id.container_main_activity_fragment, newFragmentClass, null, newFragmentTag)
                .commit();
        fragmentManager.executePendingTransactions();

        // if the variable 'visible' is set to false, hide the new fragment upon creation
        if (!visible) {
            fragmentManager
                    .beginTransaction()
                    .hide(fragmentManager.findFragmentByTag(newFragmentTag))
                    .commit();
            fragmentManager.executePendingTransactions();
        }
    }

    /*
     * switchVisibleFragment: grabs the currently visible fragment, hides it, and displays
     * the requested replacement fragment.
     */
    private void switchVisibleFragment(String replacementFragmentTag, FragmentManager fragmentManager) {
        // hide the currently visible fragment and show the replacement fragment
        fragmentManager
                .beginTransaction()
                .hide(getVisibleFragment(fragmentManager))
                .show(fragmentManager.findFragmentByTag(replacementFragmentTag))
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
    }

    /*
     * getVisibleFragment: grabs the currently visible fragment.
     */
    private Fragment getVisibleFragment(FragmentManager fragmentManager) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}