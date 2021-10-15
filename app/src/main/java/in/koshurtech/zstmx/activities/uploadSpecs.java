package in.koshurtech.zstmx.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.workers.grabInfoForUpload;
import in.koshurtech.zstmx.workers.reportGenerator;

public class uploadSpecs extends AppCompatActivity {


    ChipGroup chipGroup;
    ProgressBar progressBar;
    ExtendedFloatingActionButton floatingActionButton;



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
        setContentView(R.layout.activity_upload_specs);

        getSupportActionBar().setElevation(0);



        chipGroup = (ChipGroup) findViewById(R.id.genRepChipGroup2);



        progressBar = (ProgressBar) findViewById(R.id.LPIUploadSpecs);

        progressBar.setVisibility(View.INVISIBLE);

        floatingActionButton = (ExtendedFloatingActionButton) findViewById(R.id.fabupspec);



    }

    public void uploadSpecs(View view) {
        try {
            floatingActionButton.setEnabled(false);
            List<Integer> chips = chipGroup.getCheckedChipIds();
            if(chips.size()!=0){
                progressBar.setVisibility(View.VISIBLE);
                int[] arr = new int[chips.size()];
                int j=0;
                for (int i: chips
                ) {
                    arr[j] = i;
                    j++;
                }
                Data myData = new Data.Builder()
                        .putIntArray("configuration",arr)
                        .build();
                work = new OneTimeWorkRequest.Builder(grabInfoForUpload.class)
                        .setInputData(myData)
                        .addTag("generator")
                        .build();
                WorkManager.getInstance(getApplicationContext()).enqueue(work);
                WorkManager.getInstance(getApplicationContext())
                        .getWorkInfoByIdLiveData(work.getId())
                        .observe(uploadSpecs.this, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {

                                i++;

                                if(i>2){

                                    progressBar.setVisibility(View.INVISIBLE);
                                    floatingActionButton.setEnabled(true);

                                    System.out.println(workInfo.getOutputData().getString("output"));
                                    Intent i = new Intent(getApplicationContext(),reviewInformation.class);

                                    i.putExtra("fileName",workInfo.getOutputData().getString("output"));
                                    startActivity(i);

                                    finish();
                                }



                            }
                        });
            }
            else {
                floatingActionButton.setEnabled(true);
               showSnack2("Please select at least one parameter.",view);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        i = 0;
    }



    int i=0;

    public void showSnack2(String mm,View v){
        Snackbar.make(v, mm,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }


    OneTimeWorkRequest work;
}