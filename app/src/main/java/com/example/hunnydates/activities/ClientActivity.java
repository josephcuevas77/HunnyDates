package com.example.hunnydates.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.hunnydates.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClientActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_display);

        bottomNavigationView = findViewById(R.id.cd_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.client_nav_host_fragment);
        navController = navHostFragment.getNavController();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // By using switch we can easily get
            // the selected fragment
            // by using there id.
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.cb_nav_home:
                    navController.navigate(R.id.clientHomeFragment);
                    break;
                case R.id.cb_nav_matches:
                    navController.navigate(R.id.matchesFragment);
                    break;
                case R.id.cb_nav_search:
                    navController.navigate(R.id.searchFragment);
                    break;
                case R.id.cb_nav_create:
                    navController.navigate(R.id.createDateFragment);
                    break;
                case R.id.cb_nav_list:
                    navController.navigate(R.id.viewDatesFragment);
                    break;
            }
            return true;
        }
    };
}