package in.koshurtech.zstmx.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.adapters.showcaseProfileRV;
import in.koshurtech.zstmx.adapters.uploadInfoRV;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;
import in.koshurtech.zstmx.javaClasses.uploadInfoViewHolder;

public class profileShowcase extends AppCompatActivity {

    JSONObject jsonObject;
    LinearProgressIndicator linearProgressIndicator;
    RecyclerView recyclerView;
    showcaseProfileRV showcaseProfileRV;
    ArrayList<uploadInfoViewHolder> uploadInfoViewHolderArrayList = new ArrayList<>();




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
        setContentView(R.layout.activity_profile_showcase);

        getSupportActionBar().setElevation(0);





        recyclerView = (RecyclerView) findViewById(R.id.deviceParametersRecyclerView);
        linearProgressIndicator = (LinearProgressIndicator) findViewById(R.id.showProfileLPI);
        linearProgressIndicator.setVisibility(View.VISIBLE);





        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        showcaseProfileRV = new showcaseProfileRV(uploadInfoViewHolderArrayList,profileShowcase.this,getApplicationContext());

        recyclerView.setAdapter(showcaseProfileRV);
//        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        getProfileData(getIntent().getStringExtra("id"));





    }



    boolean isLoading = false;

    private void getProfileData(String id){
        isLoading = true;
        linearProgressIndicator.setVisibility(View.VISIBLE);
        uploadInfoViewHolderArrayList.clear();

        HashMap<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");

        HashMap<String,String> payload = new HashMap<>();
        payload.put("id",id);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST,
                        getString(R.string.getProfileByIdFormServer),
                        new JSONObject(payload),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                isLoading = false;

                                linearProgressIndicator.setVisibility(View.INVISIBLE);
                                try {
                                    jsonObject = response;


                                    String arr =response.getString("deviceInfo");

                                    JSONArray jsonArray = new JSONArray(arr);




                                    for(int i=0; i<jsonArray.length(); i++){

                                        JSONObject jsonObject  = jsonArray.getJSONObject(i);
                                        JSONArray jsonArray2 = jsonObject.getJSONArray("data");
                                        AtomicReference<String> ssrr = new AtomicReference<>("");
                                        for(int j=0; j<jsonArray2.length(); j++){
                                            JSONObject JSONObject2  = jsonArray2.getJSONObject(j);
                                            HashMap<String, Object> yourHashMap = new Gson().fromJson(JSONObject2.toString(), HashMap.class);
                                            yourHashMap.entrySet().forEach(entry -> {
                                                ssrr.set(ssrr + entry.getKey() + " " + entry.getValue()+"\n");
                                            });

                                        }

                                        uploadInfoViewHolderArrayList.add(new uploadInfoViewHolder(jsonObject.getString("prop"),jsonObject.getString("data")));

                                    }



                                    showcaseProfileRV.notifyDataSetChanged();



                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {



                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isLoading = false;
                        linearProgressIndicator.setVisibility(View.INVISIBLE);


                        NetworkResponse networkResponse = error.networkResponse;
                        if(error instanceof ServerError && networkResponse!=null){
                            try{
                                String res = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers,  "utf-8"));
                                Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
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