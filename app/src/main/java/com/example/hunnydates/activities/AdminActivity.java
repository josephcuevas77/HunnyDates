package com.example.hunnydates.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hunnydates.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_display);

        bottomNavigationView = findViewById(R.id.ad_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_nav_host_fragment);
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
                case R.id.ab_nav_home:
//                    navController.navigate(R.id.clientHomeFragment);
                    break;
                case R.id.ab_nav_list:
//                    navController.navigate(R.id.matchesFragment);
                    break;
            }
            return true;
        }
    };
}