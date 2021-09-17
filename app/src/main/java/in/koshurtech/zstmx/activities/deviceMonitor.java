package in.koshurtech.zstmx.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.services.benchmarkService;
import in.koshurtech.zstmx.splashScreen;

public class deviceMonitor extends AppCompatActivity {
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        finish();
//    }
//    @Override
//    public boolean onSupportNavigateUp() {
//        if (getSupportFragmentManager().popBackStackImmediate()) {
//            return true;
//        }
//        else {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//            finish();
//        }
//        return true;
//    }


    MaterialButton start;
    MaterialButton stop;
    ChipGroup chipGroup;





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
        setContentView(R.layout.activity_device_benchmark);
        getSupportActionBar().setElevation(0);


        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        for (Thread t: threadSet
        ) {

            if (t.getName().equals("cpuThread")){
                t.interrupt();
            }
            if (t.getName().equals("isAllDone")){
                t.interrupt();
            }
            if (t.getName().equals("batteryThread")){
                t.interrupt();
            }
            if (t.getName().equals("networkThread")){
                t.interrupt();
            }
            if (t.getName().equals("ramThread")){
                t.interrupt();
            }

            if (t.getName().equals("storageThread")){
                t.interrupt();
            }

            if (t.getName().equals("thermalThread")){
                t.interrupt();
            }
        }




        start = (MaterialButton) findViewById(R.id.startBenchmarkButton);
        stop = (MaterialButton) findViewById(R.id.stopBenchmarkButton);
        chipGroup = (ChipGroup) findViewById(R.id.genRepChipGroup1);







        stop.setEnabled(false);

        if (isMyServiceRunning(benchmarkService.class)) {
            start.setEnabled(false);
            stop.setEnabled(true);
        } else {
            start.setEnabled(true);
            stop.setEnabled(false);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipids = chipGroup.getCheckedChipIds();
                if(chipids.size()!=0){
                    start.setEnabled(false);
                    stop.setEnabled(true);
                    stopService();
                    startService();
                    finish();
                }
                else {
                    Snackbar.make(v, "No parameters selected, please select at least at least 1 measuring parameter.",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                stop.setEnabled(false);
                stopService();
            }
        });





    }


    List<Integer> chipids;

    public void startService() {
        Intent serviceIntent = new Intent(this, benchmarkService.class);
        serviceIntent.putExtra("serviceId", UUID.randomUUID().toString());
        StringBuilder gg = new StringBuilder();

        for(int i=0; i<chipids.size(); i++){
            gg.append(chipids.get(i)).append(" ");
        }
        serviceIntent.putExtra("chips", gg.toString().trim());
        serviceIntent.putExtra("startTime", String.valueOf(System.currentTimeMillis()));
        ContextCompat.startForegroundService(this, serviceIntent);
    }


    public void stopService() {
        Intent serviceIntent = new Intent(this, benchmarkService.class);
        stopService(serviceIntent);
    }


    public void startBenchmark(View view) {

    }

    public void stopBenchmark(View view) {

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void results(View view) {
        Intent i = new Intent(getApplicationContext(), deviceMonitorResults.class);
        startActivity(i);
    }

    public void CloseTool(View view) {

        startActivity(new Intent(getApplicationContext(), splashScreen.class));
        finishAffinity();
        System.exit(0);
    }
}