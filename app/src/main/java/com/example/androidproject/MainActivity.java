package com.example.androidproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidproject.fragments.ChatFragment;
import com.example.androidproject.fragments.HistoryFragment;
import com.example.androidproject.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;

//LLM


public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        tv = findViewById(R.id.tv);
        setSupportActionBar(toolbar);






        HomeFragment homeFragment = new HomeFragment();
//        homeFragment.setLlmInference(llmInference); // Pass the LLM instance
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, homeFragment)
                .addToBackStack(null)
                .commit();


        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.nav_model_training){
                    Fragment fragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                } else if (item.getItemId()==R.id.nav_private_chat) {
                    Fragment fragment = new ChatFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                } else if (item.getItemId()==R.id.nav_history) {
                    Fragment fragment = new HistoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                } else if (item.getItemId()==R.id.nav_settings) {
                    Intent next = new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(next);
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://f-droid.org/packages/"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}