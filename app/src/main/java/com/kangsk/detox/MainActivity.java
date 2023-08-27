package com.kangsk.detox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kangsk.detox.homefragment.HomeFragment;
import com.kangsk.detox.lockdownfragment.LockdownFragment;
import com.kangsk.detox.lockdownfragment.utility.MonitorService;
import com.kangsk.detox.settingsfragment.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    /*
     * CONSTANTS
     */
    // these tags represent each fragment, used to track if a fragment has already been created and is in the backstack.
    private static final String HOME_TAG = String.valueOf(R.id.menu_home);
    private static final String LOCKDOWN_TAG = String.valueOf(R.id.menu_lockdown);
    private static final String SETTINGS_TAG = String.valueOf(R.id.menu_settings);

    /*
     * PRIVATE FIELDS
     */
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remove top action bar by making the decorview stretch through the entire system window, ignoring system widgets
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // bottom navigation bar
        mBottomNavigationView = findViewById(R.id.navigation_bar_main_activity_bottom);
        mBottomNavigationView.setOnItemSelectedListener(this);

        /*
         * at the creation of the activity, the user should land on the home fragment. The following
         * code creates the home fragment, attaches it to this activity, and adds it to the BackStack.
         */
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(HOME_TAG)
                .add(R.id.container_main_activity_fragment, HomeFragment.class, null, HOME_TAG)
                .commit();

        /*
         * start an unbound TestMonitorService to run in the background and watch for blacklisted
         * apps being opened during a specific lockdown period.
         */
        Intent monitorServiceIntent = new Intent(this, MonitorService.class);
        startService(monitorServiceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /*
     * onNavigationItemSelected: listener triggered when an item in the bottom
     * navigation bar is pressed. This method is responsible for switching/creating
     * the fragments based on the user's input in the navigation bar.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int menuItemId = menuItem.getItemId();

        // home item selected
        if (menuItemId == R.id.menu_home) {

            // home fragment has already been created but is not visible
            if (getSupportFragmentManager().findFragmentByTag(HOME_TAG) != null) {
                replaceCurrentFragment(HOME_TAG, fragmentManager);

            // home fragment has not been created
            } else {
                createFragment(HomeFragment.class, HOME_TAG, fragmentManager);
            }

            return true;

        // lockdown item selected
        } else if (menuItemId == R.id.menu_lockdown) {

            // lockdown fragment has already been created but is not visible
            if (getSupportFragmentManager().findFragmentByTag(LOCKDOWN_TAG) != null) {
                replaceCurrentFragment(LOCKDOWN_TAG, fragmentManager);

            // lockdown fragment has not been created
            } else {
                createFragment(LockdownFragment.class, LOCKDOWN_TAG, fragmentManager);
            }

            return true;

        // settings item selected
        } else if (menuItemId == R.id.menu_settings) {

            // settings fragment has already been created but is not visible
            if (getSupportFragmentManager().findFragmentByTag(SETTINGS_TAG) != null) {
                replaceCurrentFragment(SETTINGS_TAG, fragmentManager);

            // settings fragment has not been created
            } else {
                createFragment(SettingsFragment.class, SETTINGS_TAG, fragmentManager);
            }

            return true;
        }

        return false;
    }

    /*
     * createFragment: creates the specified Fragment subclass, adds an identifier
     * tag to the fragment, and displays it.
     */
    private void createFragment(Class<? extends Fragment> newFragment, String newFragmentTag, FragmentManager fragmentManager) {
        String currentFragmentTag = getCurrentFragmentTag(fragmentManager);

        // hide the currently visible fragment
        fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .hide(fragmentManager.findFragmentByTag(currentFragmentTag))
                .commit();

        // create and display the new fragment
        fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(newFragmentTag)
                .add(R.id.container_main_activity_fragment, newFragment, null, newFragmentTag)
                .commit();
    }

    /*
     * replaceCurrentFragment: replace the current fragment with the fragment
     * with the tag replacementFragmentTag. This method only works if the
     * replacement fragment has already been created with the identifying
     * tag.
     */
    private void replaceCurrentFragment(String replacementFragmentTag, FragmentManager fragmentManager) {
        String currentFragmentTag = getCurrentFragmentTag(fragmentManager);

        // hide the currently visible fragment
        fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .hide(fragmentManager.findFragmentByTag(currentFragmentTag))
                .commit();

        // find the background fragment with the replacement fragment tag, and display it
        fragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(replacementFragmentTag)
                .show(fragmentManager.findFragmentByTag(replacementFragmentTag))
                .commit();
    }

    /*
     * getCurrentFragmentTag: get the currently visible fragment
     */
    private String getCurrentFragmentTag(FragmentManager fragmentManager) {
        int currentBackStackEntry = fragmentManager.getBackStackEntryCount() - 1;
        String currentFragmentTag = fragmentManager.getBackStackEntryAt(currentBackStackEntry).getName();

        return currentFragmentTag;
    }
}