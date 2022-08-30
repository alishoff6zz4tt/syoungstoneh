package com.example.kidstodoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.Observable;

public class MainActivity extends AppCompatActivity implements java.util.Observer, NavigationView.OnNavigationItemSelectedListener {

    private ParentModeUtility parentModeUtility;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Fragment FAQ;
    private Fragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggleDrawer = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggleDrawer);
        toggleDrawer.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.home);
        navigationView.getMenu().findItem(R.id.ConfirmPassword).setTitle("Parent Mode");
        navigationView.getMenu().findItem(R.id.ConfirmCompleted).setVisible(false);

        NightMode.defaultMode(this);

        parentModeUtility = ParentModeUtility.getInstance();
        parentModeUtility.addObserver(this);
        parentModeUtility.initializeTimeout();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ToDoListFragment(), "TO_DO_LIST")
                .addToBackStack("TO_DO_LIST")                      //addToBackStack, so when any fragments replace it, and the user presses the back button
                .commit();                                         //they return to this fragment
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();                                 //Whenever the user interacts with the screen, it resets the handler
        parentModeUtility.resetTimeout();
    }

    public void onParentModeChanged() {                              //When parent mode is changed
        if(parentModeUtility.isInParentMode()) {                               //Set the visibility and views accordingly
            navigationView.getMenu().findItem(R.id.ConfirmPassword).setTitle("Child Mode");
            navigationView.getMenu().findItem(R.id.ConfirmCompleted).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.ConfirmPassword).setTitle("Parent Mode");
            navigationView.getMenu().findItem(R.id.ConfirmCompleted).setVisible(false);
            removeParentOnlyFragments();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            removeCurrentFragment();        //Removes the current fragment as long as it is not ToDoListFragment
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        switch (item.getItemId()) {
            case R.id.ToDoListFragment:
                if(!(fragment instanceof ToDoListFragment)) {
                    removeCurrentFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ToDoListFragment(),"TO_DO_LIST")
                            .addToBackStack("TO_DO_LIST")
                            .commit();
                }
                break;
            case R.id.TrophyCase:
                if(!(fragment instanceof TrophyCaseFragment)) {
                    removeCurrentFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new TrophyCaseFragment(),"TROPHY_CASE")
                            .addToBackStack("TROPHY_CASE")
                            .commit();
                }
                break;
            case R.id.ConfirmPassword:
                if(parentModeUtility.isInParentMode()) {               //If the user is in parent mode, logs out and makes the appropriate changes
                    parentModeUtility.setInParentMode(false);
                    Toast.makeText(MainActivity.this,
                            "Exiting parent mode",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!(fragment instanceof ConfirmPassword)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ConfirmPassword(), "CONFIRM_PASSWORD")
                                .addToBackStack("CONFIRM_PASSWORD")
                                .commit();
                    }
                }
                break;
            case R.id.ConfirmCompleted:
                if(!(fragment instanceof CompletedListFragment)) {
                    removeCurrentFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new CompletedListFragment(),"CONFIRM_COMPLETED")
                            .addToBackStack("CONFIRM_COMPLETED")
                            .commit();
                }
                break;
            case R.id.FAQ:
                if(!(fragment instanceof FAQ)) {
                    FAQ = new FAQ();
                    removeCurrentFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, FAQ, "FAQ")
                            .addToBackStack("FAQ")
                            .commit();
                }
                break;
            case R.id.settings:
                if(!(fragment instanceof SettingsFragment)) {
                    settingsFragment = new SettingsFragment();
                    removeCurrentFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, settingsFragment, "SETTINGS")
                            .addToBackStack("SETTINGS")
                            .commit();
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void removeCurrentFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && !(fragment instanceof ToDoListFragment)) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getSupportFragmentManager().popBackStack();
        }
    }

    public void removeParentOnlyFragments() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof CompletedListFragment ||
                fragment instanceof ToDoEntryFragment ||
                fragment instanceof CreateToDoEntryFragment ||
                fragment instanceof CreateTrophyFragment) {
            removeCurrentFragment();
        }
    }

    public void toggleVisibilityFAQ(View view) {
        ((com.example.kidstodoapp.FAQ) FAQ).toggleVisibility(view);
    }
    public void toggleVisibilitySettings(View view) {
        ((com.example.kidstodoapp.SettingsFragment) settingsFragment).toggleVisibility(view);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof ParentModeUtility) {
            onParentModeChanged();
        }
    }
}