package com.kangsk.detox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    // private non-static fields
    private BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * remove top action bar:
         * makes the decorview to stretch throughout the entire system window, ignoring system widgets
         */
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // bottom navigation bar
        mBottomNav = findViewById(R.id.bottom_nav);
        mBottomNav.setOnItemSelectedListener(this);

        // attach home fragment to activity, plus some other fragment configuration changes
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .add(R.id.fragment_container, HomeFragment.class, null)
                .commit();
    }

    // bottom navigation bar item selection listener
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // home item selected
        if (id == R.id.menu_home) {
            replaceFragment(HomeFragment.class);
            return true;
        // lockdown item selected
        } else if (id == R.id.menu_lockdown) {
            replaceFragment(LockdownFragment.class);
            return true;
        // settings item selected
        } else if (id == R.id.menu_settings) {
            replaceFragment(SettingsFragment.class);
            return true;
        }

        return false;
    }

    // fragment transition/replace helper method
    public void replaceFragment(Class<? extends Fragment> newFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.fragment_container, newFragment, null)
                .commit();
    }

}