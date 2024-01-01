package com.kangsk.detox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kangsk.detox.homefragment.HomeFragment;
import com.kangsk.detox.lockdownfragment.LockdownFragment;
import com.kangsk.detox.lockdowneditfragment.LockdownEditFragment;
import com.kangsk.detox.lockdownfragment.utility.LockdownManager;
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
    private FragmentManager mFragmentManager;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remove top action bar by making the decorview stretch through the entire system window, ignoring system widgets
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // fragment manager
        mFragmentManager = getSupportFragmentManager();

        // bottom navigation bar
        mBottomNavigationView = findViewById(R.id.navigation_bar_main_activity_bottom);
        mBottomNavigationView.setOnItemSelectedListener(this);

        /*
         * at the creation of the activity, the user should land on the home fragment. The following
         * code creates the home fragment, attaches it to this activity, and adds it to the BackStack.
         */
        createFragment(HomeFragment.class, HOME_TAG, true, mFragmentManager);
        createFragment(LockdownFragment.class, LOCKDOWN_TAG, false, mFragmentManager);
        createFragment(SettingsFragment.class, SETTINGS_TAG, false, mFragmentManager);
        createFragment(LockdownEditFragment.class, "LOCKDOWNCREATIONDIALOGFRAGMENT", false, mFragmentManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LockdownManager.getInstance(this.getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
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
     * tag to the fragment, and displays/hides it.
     */
    private void createFragment(Class<? extends Fragment> newFragmentClass, String newFragmentTag, boolean visible, FragmentManager fragmentManager) {
        // create new fragment
        fragmentManager
                .beginTransaction()
                .add(R.id.container_main_activity_fragment, newFragmentClass, null, newFragmentTag)
                .commit();
        fragmentManager.executePendingTransactions();

        // hide new fragment
        if (!visible) {
            fragmentManager
                    .beginTransaction()
                    .hide(fragmentManager.findFragmentByTag(newFragmentTag))
                    .commit();
            fragmentManager.executePendingTransactions();
        }
    }

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

    private Fragment getVisibleFragment(FragmentManager fragmentManager) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}