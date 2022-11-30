package com.example.reciteword;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InterfaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //定制配置类，为其指定四个目的地fragment
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recite, R.id.navigation_review, R.id.navigation_fight,
                R.id.navigation_wrong).build();

        //用xml里的fragment容器构建导航控制器
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);

        //为导航控制器设置配置类
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //关联NavigationView和导航控制器
        NavigationUI.setupWithNavController(navView, navController);
        System.out.println("there is Interface");
    }
}