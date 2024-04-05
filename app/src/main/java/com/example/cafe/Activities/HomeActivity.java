package com.example.cafe.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;

import com.example.cafe.Fragments.DisplayCategoryFragment;
import com.example.cafe.Fragments.DisplayHomeFragment;
import com.example.cafe.Fragments.DisplayStaffFragment;
import com.example.cafe.Fragments.DisplayStatisticFragment;
import com.example.cafe.Fragments.DisplayTableFragment;
import com.example.cafe.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MenuItem selectedFeature, selectedManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;//thanh ngang trên
    FragmentManager fragmentManager;
    TextView TXT_menu_tennv;
    int maquyen = 0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_trangchu);
        toolbar = findViewById(R.id.toolbar);
        View view = navigationView.getHeaderView(0);
        TXT_menu_tennv = view.findViewById(R.id.txt_menu_tennv);

        //xử lý toolbar và navigation
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        setSupportActionBar(toolbar); //tạo toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //tạo nút mở navigation
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.opentoggle, R.string.closetoggle) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Tụ động gán tên nv đăng nhập qua Extras
        Intent intent = getIntent();
        String tennv = intent.getStringExtra("tennv");
        TXT_menu_tennv.setText("Xin chào " + tennv + " !!");

        //lấy file sharepreference từ trang đăng nhập
        sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);

        //hiện thị fragment home mặc định
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
        DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
        tranDisplayHome.replace(R.id.contentView, displayHomeFragment);
        tranDisplayHome.commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {// hàm này thực hiện chuyển menu
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();//     bắt đầu transaction
            tranDisplayHome.replace(R.id.contentView, new DisplayHomeFragment());//              thay nội dung fragment này thành trang hiển thị home
            tranDisplayHome.commit();                                               //              kết thúc

            navigationView.setCheckedItem(id);
            drawerLayout.closeDrawers();// đóng cái menu bên cạnh
        } else if (id == R.id.nav_statistic) {
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayStatistic = fragmentManager.beginTransaction();
            tranDisplayStatistic.replace(R.id.contentView, new DisplayStatisticFragment());
            tranDisplayStatistic.commit();

            navigationView.setCheckedItem(id);
            drawerLayout.closeDrawers();
        } else if (id == R.id.nav_table) {
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayTable = fragmentManager.beginTransaction();
            tranDisplayTable.replace(R.id.contentView, new DisplayTableFragment());
            tranDisplayTable.commit();

            navigationView.setCheckedItem(id);
            drawerLayout.closeDrawers();
        } else if (id == R.id.nav_category) {
            //hiển thị tương ứng trên navigation
            FragmentTransaction tranDisplayMenu = fragmentManager.beginTransaction();
            tranDisplayMenu.replace(R.id.contentView, new DisplayCategoryFragment());
            tranDisplayMenu.commit();

            navigationView.setCheckedItem(id);
            drawerLayout.closeDrawers();
        } else if (id == R.id.nav_staff) {
            if (maquyen == 1) {//nếu quyền là admin (1=admin; 2=nhân viên)
                //hiển thị tương ứng trên navigation
                FragmentTransaction tranDisplayStaff = fragmentManager.beginTransaction();
                tranDisplayStaff.replace(R.id.contentView, new DisplayStaffFragment());
                tranDisplayStaff.commit();

                navigationView.setCheckedItem(id);
                drawerLayout.closeDrawers();
            } else {
                Toast.makeText(getApplicationContext(), "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_logout) {
            //gọi activity ra trang welcome
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        }


        return false;
    }

}