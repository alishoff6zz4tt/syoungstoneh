package com.example.kidstodoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Handler parentModeTimeOut;
    private Runnable runnable;

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
//        navigationView.getMenu().findItem(R.id.PhoneNumber).setVisible(false);
        navigationView.getMenu().findItem(R.id.ConfirmCompleted).setVisible(false);

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {                               //This is what is done every x milliseconds unless the user
            @Override                                             //interacts with the screen
            public void run() {
                if (ParentModeUtility.isInParentMode() && !ParentModeUtility.isParentDevice()) {
                    ParentModeUtility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(MainActivity.this,
                            "Exiting parent mode due to inactivity",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);         //Starts the countdown to running the runnable

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ToDoListFragment(), "TO_DO_LIST")
                .addToBackStack("TO_DO_LIST")                      //addToBackStack, so when any fragments replace it, and the user presses the back button
                .commit();                                         //they return to this fragment
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();                                 //Whenever the user interacts with the screen, it resets the handler
        ParentModeUtility.stopHandler(parentModeTimeOut, runnable);
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();                                           //Whenever the user leaves MainActivity, it stops the handler
        ParentModeUtility.stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();                                          //When the user returns to MainActivity, resumes the handler
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);
        onParentModeChanged();
    }

    public void onParentModeChanged() {                              //When parent mode is changed
        if(ParentModeUtility.isInParentMode()) {                               //Set the visibility and views accordingly
            navigationView.getMenu().findItem(R.id.ConfirmPassword).setTitle("Child Mode");
            navigationView.getMenu().findItem(R.id.ConfirmCompleted).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.ConfirmPassword).setTitle("Parent Mode");
            navigationView.getMenu().findItem(R.id.ConfirmCompleted).setVisible(false);
            removeCurrentFragment(null);
        }
        Fragment toDoListFragment = (ToDoListFragment) getSupportFragmentManager().findFragmentByTag("TO_DO_LIST");
        if(toDoListFragment != null && toDoListFragment.isVisible()) {
            ((com.example.kidstodoapp.ToDoListFragment) toDoListFragment).onParentModeChanged();
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
        switch (item.getItemId()) {
            case R.id.ToDoListFragment:
                removeCurrentFragment();
                break;
            case R.id.TrophyCase:
                Intent intent = new Intent(MainActivity.this, TrophyCase.class);
                startActivity(intent);
                break;
            case R.id.ConfirmPassword:
                if(ParentModeUtility.isInParentMode()) {               //If the user is in parent mode, logs out and makes the appropriate changes
                    ParentModeUtility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(MainActivity.this,
                            "Exiting parent mode",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    ConfirmPassword confirmPassword = (ConfirmPassword) getSupportFragmentManager().findFragmentByTag("CONFIRM_PASSWORD");
                    if(confirmPassword == null) {
                        removeCurrentFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ConfirmPassword(), "CONFIRM_PASSWORD")
                                .addToBackStack("CONFIRM_PASSWORD")
                                .commit();
                    }
                }
                break;
            case R.id.ConfirmCompleted:
                CompletedListFragment completedList = (CompletedListFragment) getSupportFragmentManager().findFragmentByTag("CONFIRM_COMPLETED");
                if(completedList == null) {
                    removeCurrentFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new CompletedListFragment(),"CONFIRM_COMPLETED")
                            .addToBackStack("CONFIRM_COMPLETED")
                            .commit();
                }
                break;
            case R.id.FAQ:
                FAQ faq = (FAQ) getSupportFragmentManager().findFragmentByTag("FAQ");
                if(faq == null) {
                    FAQ = new FAQ();
                    removeCurrentFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, FAQ, "FAQ")
                            .addToBackStack("FAQ")
                            .commit();
                }
                break;
            case R.id.settings:
                SettingsFragment settings = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("SETTINGS");
                if(settings == null) {
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
        ToDoListFragment toDoListFragment = (ToDoListFragment) getSupportFragmentManager().findFragmentByTag("TO_DO_LIST");
        Fragment fragment = null;
        if(toDoListFragment == null || !toDoListFragment.isVisible()) {
            ConfirmPassword confirmPassword = (ConfirmPassword) getSupportFragmentManager().findFragmentByTag("CONFIRM_PASSWORD");
            CompletedListFragment completedList = (CompletedListFragment) getSupportFragmentManager().findFragmentByTag("CONFIRM_COMPLETED");
            FAQ faq = (FAQ) getSupportFragmentManager().findFragmentByTag("FAQ");
            ToDoEntryFragment toDoEntryFragment = (ToDoEntryFragment) getSupportFragmentManager().findFragmentByTag("TO_DO_ENTRY");
            CreateToDoEntryFragment createToDoEntryFragment = (CreateToDoEntryFragment) getSupportFragmentManager().findFragmentByTag("CREATE_TO_DO_ENTRY");
            SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("SETTINGS");

            if(confirmPassword != null) {fragment = confirmPassword;}
            else if(completedList != null) {fragment = completedList;}
            else if(faq != null) {fragment = faq;}
            else if(toDoEntryFragment != null) {fragment = toDoEntryFragment;}
            else if(createToDoEntryFragment != null) {fragment = createToDoEntryFragment;}
            else if(settingsFragment != null) {fragment = settingsFragment;}

            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            getSupportFragmentManager().popBackStack();
        }
    }

    public void removeCurrentFragment(String s) {
        ToDoListFragment toDoListFragment = (ToDoListFragment) getSupportFragmentManager().findFragmentByTag("TO_DO_LIST");
        Fragment fragment = null;
        if (toDoListFragment == null || !toDoListFragment.isVisible()) {
            CompletedListFragment completedList = (CompletedListFragment) getSupportFragmentManager().findFragmentByTag("CONFIRM_COMPLETED");
            ToDoEntryFragment toDoEntryFragment = (ToDoEntryFragment) getSupportFragmentManager().findFragmentByTag("TO_DO_ENTRY");
            CreateToDoEntryFragment createToDoEntryFragment = (CreateToDoEntryFragment) getSupportFragmentManager().findFragmentByTag("CREATE_TO_DO_ENTRY");

            if (completedList != null) {
                fragment = completedList;
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                getSupportFragmentManager().popBackStack();
            } else if (toDoEntryFragment != null) {
                fragment = toDoEntryFragment;
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                getSupportFragmentManager().popBackStack();
            } else if (createToDoEntryFragment != null) {
                fragment = createToDoEntryFragment;
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    public void toggleVisibilityFAQ(View view) {
        ((com.example.kidstodoapp.FAQ) FAQ).toggleVisibility(view);
    }
    public void toggleVisibilitySettings(View view) {
        ((com.example.kidstodoapp.SettingsFragment) settingsFragment).toggleVisibility(view);
    }
}