package in.koshurtech.zstmx;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

import in.koshurtech.zstmx.activities.Settings;
import in.koshurtech.zstmx.adapters.tabAdapter;
public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    tabAdapter tabAdapter;
    Menu menu;
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("appTheme","0");
        if(theme.equals("0")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if(theme.equals("1")){
            setTheme(R.style.Theme_ZstmX_day);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            getSupportActionBar().setElevation(0);
        }
        else if(theme.equals("2")){
            setTheme(R.style.Theme_ZstmX_night);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));

        }


        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);




        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager2) findViewById(R.id.viewPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        tabAdapter = new tabAdapter(fragmentManager,getLifecycle(),getApplicationContext());
        viewPager.setAdapter(tabAdapter);

        reduceDragSensitivity();
        tabLayout.addTab(tabLayout.newTab().setText("DashBoard"));
        tabLayout.addTab(tabLayout.newTab().setText("System"));
        tabLayout.addTab(tabLayout.newTab().setText("Software"));
        tabLayout.addTab(tabLayout.newTab().setText("DRM Info"));
        tabLayout.addTab(tabLayout.newTab().setText("CPU"));
        tabLayout.addTab(tabLayout.newTab().setText("RAM"));
        tabLayout.addTab(tabLayout.newTab().setText("Battery"));
        tabLayout.addTab(tabLayout.newTab().setText("Storage"));
        tabLayout.addTab(tabLayout.newTab().setText("Network"));
        tabLayout.addTab(tabLayout.newTab().setText("Display"));
        tabLayout.addTab(tabLayout.newTab().setText("Sensors"));
        tabLayout.addTab(tabLayout.newTab().setText("Camera"));
        tabLayout.addTab(tabLayout.newTab().setText("Thermals"));
        tabLayout.addTab(tabLayout.newTab().setText("Community"));
        tabLayout.addTab(tabLayout.newTab().setText("Benchmark"));




        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        String perf = sp.getString("appPerformance","0");

        if(perf.equals("0")){
            viewPager.setOffscreenPageLimit(1);
        }
        else if(perf.equals("1")){
            viewPager.setOffscreenPageLimit(13);
        }



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showOverflowMenu(false);
            }
        },100);




    }


    //Stackoverflow zindbaad, increase view pager sensitivity
    private void reduceDragSensitivity() {
        try {
            Field ff = ViewPager2.class.getDeclaredField("mRecyclerView") ;
            ff.setAccessible(true);
            RecyclerView recyclerView =  (RecyclerView) ff.get(viewPager);
            Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop") ;
            touchSlopField.setAccessible(true);
            int touchSlop = (int) touchSlopField.get(recyclerView);
            //change vale of this constant to customize the sensitivity
            //https://stackoverflow.com/questions/12135615/how-to-modify-sensitivity-of-viewpager/36412538
            touchSlopField.set(recyclerView,touchSlop*2);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    public void showOverflowMenu(boolean showMenu){
        if(menu == null)
            return;
        menu.setGroupVisible(R.id.grp, showMenu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}