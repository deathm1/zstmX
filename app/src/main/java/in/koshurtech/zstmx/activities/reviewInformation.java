package in.koshurtech.zstmx.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import github.nisrulz.easydeviceinfo.base.EasyDeviceMod;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.uploadInfoRV;
import in.koshurtech.zstmx.javaClasses.checkBoxProperties;
import in.koshurtech.zstmx.javaClasses.uploadInfoViewHolder;

public class reviewInformation extends AppCompatActivity {


    File getFile;
    ExtendedFloatingActionButton extendedFloatingActionButton;




    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(),uploadSpecs.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),uploadSpecs.class));
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView;
        ArrayList<uploadInfoViewHolder> uploadInfoViewHolderArrayList = new ArrayList<>();
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

        setContentView(R.layout.activity_review_information);
        getSupportActionBar().setElevation(0);



        String fileName = getIntent().getStringExtra("fileName");


        extendedFloatingActionButton = (ExtendedFloatingActionButton) findViewById(R.id.reviewInformationButton);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File[] files = directory.listFiles();


            for (File f : files
            ) {
                try {
                    if(f.getName().equals(fileName)){
                        getFile = f;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else{

            String path = Environment.getExternalStorageDirectory().toString()+"/zstmX Benchmark Reports";
            File directory = new File(path);
            File[] files = directory.listFiles();

            for (File f : files
            ) {
                try {
                    if(f.getName().equals(fileName)){
                        getFile = f;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


        recyclerView = (RecyclerView) findViewById(R.id.reviewInfoRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        uploadInfoRV adap = new uploadInfoRV(uploadInfoViewHolderArrayList,getApplicationContext(),reviewInformation.this);
        recyclerView.setAdapter(adap);


        try {
            JSONArray jsonArray = new JSONArray(readFromFile(getFile));

            for(int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);


                uploadInfoViewHolderArrayList.add(new uploadInfoViewHolder(jsonObject.getString("prop"),jsonObject.getString("data")));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


        final JSONArray[] finalInfoInJSON = {new JSONArray()};

        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                extendedFloatingActionButton.setEnabled(false);
                HashMap<String,ArrayList<checkBoxProperties>> getInfo = adap.getFullReviewedInfo();

                if(!getInfo.isEmpty()){
                    getInfo.entrySet().forEach(entry -> {

                        try {
                            JSONObject mainObject = new JSONObject();

                            mainObject.put("prop",entry.getKey());


                            JSONArray jsonArraykid = new JSONArray();


                            for(int i=0; i<entry.getValue().size(); i++){

                                JSONObject arrayObj = new JSONObject(entry.getValue().get(i).getProperty());

                                jsonArraykid.put(arrayObj);
                            }

                            mainObject.put("data",jsonArraykid);


                            finalInfoInJSON[0].put(mainObject);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    });
                    try {

                        SafetyNet.getClient(reviewInformation.this).verifyWithRecaptcha(getApplicationContext().getString(R.string.recaptchaKey))
                                .addOnSuccessListener(reviewInformation.this,
                                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                                            @Override
                                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                                // Indicates communication with reCAPTCHA service was
                                                // successful.

                                                String userResponseToken = response.getTokenResult();
                                                if (!userResponseToken.isEmpty()) {

                                                    HashMap<String,String> headers = new HashMap<>();
                                                    headers.put("Content-Type","application/json");
                                                    headers.put("reCaptchaToken",userResponseToken);

                                                    HashMap<String,String> payload = new HashMap<>();
                                                    EasyDeviceMod easyDeviceMod = new EasyDeviceMod(getApplicationContext());
                                                    payload.put("deviceMake",easyDeviceMod.getManufacturer());
                                                    payload.put("deviceModel",easyDeviceMod.getModel());
                                                    payload.put("deviceInfo",finalInfoInJSON[0].toString());
                                                    payload.put("deviceId", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));



                                    JsonObjectRequest jsonObjectRequest =
                                            new JsonObjectRequest(Request.Method.POST, getString(R.string.uploadSpecs),
                                                    new JSONObject(payload),
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            extendedFloatingActionButton.setEnabled(true);
                                                            try {
                                                                if(response.getBoolean("success")){
                                                                    showSnack2(response.getString("status"),v);
                                                                }
                                                            }
                                                            catch (Exception e){

                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {



                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    extendedFloatingActionButton.setEnabled(true);


                                                    NetworkResponse networkResponse = error.networkResponse;
                                                    if(error instanceof ServerError && networkResponse!=null){
                                                        try{
                                                            String res = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers,  "utf-8"));
                                                            System.out.println(res);
                                                        }
                                                        catch (Exception err){
                                                            err.printStackTrace();

                                                        }
                                                    }
                                                }
                                            }){
                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                    return headers;
                                                }
                                            };

                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                            0,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    requestQueue.add(jsonObjectRequest);

                                                }
                                            }
                                        })
                                .addOnFailureListener(reviewInformation.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        extendedFloatingActionButton.setEnabled(true);
                                        if (e instanceof ApiException) {
                                            ApiException apiException = (ApiException) e;
                                            int statusCode = apiException.getStatusCode();
                                            Toast.makeText(getApplicationContext(),+statusCode+" : "+e.toString(),Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(),"Some unknown occurred, please try again or restart the application.",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    showSnack2("Please select at leat one parameter.",v);
                }


            }
        });




        adap.notifyDataSetChanged();

















    }

    public void showSnack2(String mm,View v){
        Snackbar.make(v, mm,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
    private String readFromFile(File file) {
        String out = "";

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            out = String.valueOf(sb);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return out;
    }

    public void reviewInfoButton(View view) {
    }
}