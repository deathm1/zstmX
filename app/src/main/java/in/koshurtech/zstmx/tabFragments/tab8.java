package in.koshurtech.zstmx.tabFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.database.entities.storageInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;
import in.koshurtech.zstmx.viewModels.*;


public class tab8 extends Fragment {

    networkInfoViewModel vm;
    RecyclerView recyclerView;
    in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter recyclerInfoViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();
    MaterialButton materialButton;
    LinearProgressIndicator linearProgressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab9, container, false);


        recyclerInfoViewArrayList.clear();
        linearProgressIndicator = (LinearProgressIndicator) v.findViewById(R.id.ipProgress);
        recyclerView = (RecyclerView) v.findViewById(R.id.networkInformationRecyclerView);
        materialButton = (MaterialButton) v.findViewById(R.id.publicIpInfo);
        linearProgressIndicator.setVisibility(View.INVISIBLE);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zstmx API
                materialButton.setEnabled(false);
                SafetyNet.getClient(getActivity()).verifyWithRecaptcha(getString(R.string.recaptchaKey))
                        .addOnSuccessListener(getActivity(),
                                new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                                    @Override
                                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                        // Indicates communication with reCAPTCHA service was
                                        // successful.
                                        linearProgressIndicator.setVisibility(View.VISIBLE);
                                        String userResponseToken = response.getTokenResult();
                                        if (!userResponseToken.isEmpty()) {

                                            HashMap<String,String> headers = new HashMap<>();
                                            headers.put("Content-Type","application/json");
                                            headers.put("reCaptchaToken",userResponseToken);

                                            JsonObjectRequest jsonObjectRequest =
                                                    new JsonObjectRequest(Request.Method.GET,
                                                            getString(R.string.getIpDetails),
                                                            null,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    materialButton.setEnabled(true);
                                                                    try {
                                                                        if(response.getBoolean("success")){



                                                                            AlertDialog.Builder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                                                                            materialAlertDialogBuilder.setTitle("Public IP");
                                                                            materialAlertDialogBuilder.setCancelable(false);
                                                                            materialAlertDialogBuilder.setIcon(R.drawable.ic_outline_info_24);
                                                                            final View view = getLayoutInflater().inflate(R.layout.ip_rv,null);
                                                                            materialAlertDialogBuilder.setView(view);
                                                                            ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();
                                                                            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ipRV);
                                                                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                                                            recyclerView.setLayoutManager(layoutManager);
                                                                            recyclerInfoViewAdapter recyclerInfoViewAdapter = new recyclerInfoViewAdapter(recyclerInfoViewArrayList,getActivity().getApplicationContext(),getActivity());
                                                                            recyclerView.setAdapter(recyclerInfoViewAdapter);
                                                                            recyclerInfoViewArrayList.clear();

                                                                            JSONObject jsonObject = new JSONObject(response.getString("ipInfo"));



                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("status"),"Status"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("continent"),"continent"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("continentCode"),"continentCode"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("country"),"country"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("countryCode"),"countryCode"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("region"),"region"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("regionName"),"regionName"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("city"),"city"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("district"),"district"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("zip"),"zip"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("lat"),"lat"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("lon"),"lon"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("offset"),"offset"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("currency"),"currency"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("isp"),"isp"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("org"),"org"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("as"),"as"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("asname"),"asname"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("reverse"),"reverse"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("mobile"),"mobile"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("proxy"),"proxy"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("hosting"),"hosting"));
                                                                            recyclerInfoViewArrayList.add(new recyclerInfoView(jsonObject.getString("query"),"query"));


                                                                            recyclerInfoViewAdapter.notifyDataSetChanged();

                                                                            materialAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                }
                                                                            }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                }
                                                                            });

                                                                            AlertDialog alertDialog = materialAlertDialogBuilder.create();
                                                                            alertDialog.show();
                                                                            linearProgressIndicator.setVisibility(View.INVISIBLE);

                                                                        }
                                                                    }
                                                                    catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, new Response.ErrorListener() {



                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            materialButton.setEnabled(true);
                                                            linearProgressIndicator.setVisibility(View.INVISIBLE);

                                                            NetworkResponse networkResponse = error.networkResponse;
                                                            if(error instanceof ServerError && networkResponse!=null){
                                                                try{
                                                                    String res = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers,  "utf-8"));
                                                                    Toast.makeText(getActivity(),res,Toast.LENGTH_LONG).show();
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

                                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                    0,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                            requestQueue.add(jsonObjectRequest);
                                        }
                                    }
                                })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                materialButton.setEnabled(true);
                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    int statusCode = apiException.getStatusCode();
                                    Toast.makeText(getActivity(),+statusCode+" : "+e.toString(),Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(),"Some unknown occurred, please try again or restart the application.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerInfoViewAdapter = new recyclerInfoViewAdapter(recyclerInfoViewArrayList,getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(recyclerInfoViewAdapter);

        vm = new ViewModelProvider(getActivity()).get(in.koshurtech.zstmx.viewModels.networkInfoViewModel.class);
        vm.setContext(getActivity().getApplicationContext());


        WifiManager wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        try {
            LiveData<String> networkInfoViewModel = vm.getNetworkInfo();
            networkInfoViewModel.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    recyclerInfoViewArrayList.clear();
                    recyclerInfoViewArrayList.add(new recyclerInfoView(ip,"IPv4 Address"));

                    String[] data = s.split("--");
                    for(int i=0; i<data.length; i++){
                        String[] part = data[i].split("=");
                        if(part.length>1){
                            recyclerInfoViewArrayList.add(new recyclerInfoView(part[1],part[0]));

                        }
                    }

                    recyclerInfoViewAdapter.notifyDataSetChanged();
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }






        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.searchIcon);
        item.setVisible(true);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }


    private void filter(String newText) {
        ArrayList<recyclerInfoView> filter = new ArrayList<>();
        for(recyclerInfoView f: recyclerInfoViewArrayList){
            if(f.getInfoHeader().toLowerCase().trim().contains(newText.trim().toLowerCase())){
                filter.add(f);
            }
        }

        recyclerInfoViewAdapter.updateList(filter);
    }

}