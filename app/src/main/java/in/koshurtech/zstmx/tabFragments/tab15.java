package in.koshurtech.zstmx.tabFragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.deviceProfileAdapter;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.javaClasses.deviceProfileView;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;


public class tab15 extends Fragment {

    RecyclerView recyclerView;
    deviceProfileAdapter deviceProfileAdapter;
    LinearProgressIndicator linearProgressIndicator;
    ArrayList<deviceProfileView> deviceProfileViewArrayList = new ArrayList<>();
    ArrayList<deviceProfileView> tempDeviceProfileViewArrayList = new ArrayList<>();
    ExtendedFloatingActionButton extendedFloatingActionButton;






    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.searchIcon);
        MenuItem item2 = menu.findItem(R.id.refreshIcon);
        item.setVisible(true);
        item2.setVisible(true);

        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);


        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(!isLoading){
                    isLoading = true;
                    getProfileFromServer(10);
                }

                return true;
            }
        });

        item.setActionView(searchView);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tempDeviceProfileViewArrayList.addAll(deviceProfileViewArrayList);
                deviceProfileViewArrayList.clear();
                deviceProfileAdapter.updateList(deviceProfileViewArrayList);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        filter(query);
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                deviceProfileViewArrayList.clear();
                deviceProfileViewArrayList.addAll(tempDeviceProfileViewArrayList);
                deviceProfileAdapter.updateList(deviceProfileViewArrayList);
                return true;
            }
        });






    }

    private void filter(String newText) {
        linearProgressIndicator.setVisibility(View.VISIBLE);





        HashMap<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");

        HashMap<String,String> payload = new HashMap<>();
        payload.put("textQuery",newText);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST,
                        getString(R.string.getProfileByQueryFormServer),
                        new JSONObject(payload),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                tempDeviceProfileViewArrayList.clear();
                                linearProgressIndicator.setVisibility(View.INVISIBLE);

                                try {
                                    if(response.getBoolean("success")){
                                        JSONArray results = response.getJSONArray("results");


                                        for(int i=0; i<results.length(); i++){

                                            if(!(response.getJSONObject("resultObject") ==null)){
                                                JSONObject profile = results.getJSONObject(i);

                                                deviceProfileViewArrayList.add(new deviceProfileView(
                                                        profile.getString("deviceMake"),
                                                        profile.getString("deviceModel"),
                                                        profile.getInt("upVotes"),
                                                        profile.getInt("downVotes"),
                                                        profile.getString("entryId"),
                                                        profile.getString("serverTime")));
                                            }



                                        }
                                        deviceProfileAdapter.notifyDataSetChanged();

                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {



                    @Override
                    public void onErrorResponse(VolleyError error) {

                        linearProgressIndicator.setVisibility(View.INVISIBLE);


                        NetworkResponse networkResponse = error.networkResponse;
                        if(error instanceof ServerError && networkResponse!=null){
                            try{
                                String res = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers,  "utf-8"));
                                showSnack2(res);
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


    public void showSnack2(String mm){
        Snackbar.make(v, mm,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab16, container, false);


        extendedFloatingActionButton = (ExtendedFloatingActionButton) v.findViewById(R.id.shareDeviceSpecs);
        recyclerView = (RecyclerView) v.findViewById(R.id.allDevicesRecyclerView);
        linearProgressIndicator = (LinearProgressIndicator) v.findViewById(R.id.infoLoadingFromServer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        deviceProfileAdapter = new deviceProfileAdapter(deviceProfileViewArrayList,getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(deviceProfileAdapter);
        getProfileFromServer(limit);

        extendedFloatingActionButton = (ExtendedFloatingActionButton) v.findViewById(R.id.shareDeviceSpecs);
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(extendedFloatingActionButton.getText().equals("Load More")){
                    limit = limit + 10;
                    getProfileFromServer(limit);
                }
                else if(extendedFloatingActionButton.getText().equals("Upload Specs")){

                }
            }
        });



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (dy >0) {
                    // Scroll Down
                    if (extendedFloatingActionButton.isShown()) {
                        extendedFloatingActionButton.setAnimateShowBeforeLayout(true);
                        extendedFloatingActionButton.setText("Load More");
                        extendedFloatingActionButton.setIcon(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_outline_read_more_24));


                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (extendedFloatingActionButton.isShown()) {
                        extendedFloatingActionButton.setAnimateShowBeforeLayout(true);
                        extendedFloatingActionButton.setText("Upload Specs");
                        extendedFloatingActionButton.setIcon(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_outline_cloud_upload_24));

                    }
                }


//                if (!isLoading) {
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == deviceProfileViewArrayList.size() - 1) {
//                        //bottom of list!
//                        limit = limit + 10;
//                        getProfileFromServer(limit);
//
//                    }
//                }
            }
        });


        return v;
    }

    boolean isLoading = false;

    int limit = 10;


    private void getProfileFromServer(int limit){
        isLoading = true;
        linearProgressIndicator.setVisibility(View.VISIBLE);
        deviceProfileViewArrayList.clear();

        HashMap<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");

        HashMap<String,Integer> payload = new HashMap<>();
        payload.put("limit",limit);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST,
                        getString(R.string.getProfileFormServer),
                        new JSONObject(payload),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                isLoading = false;
                                linearProgressIndicator.setVisibility(View.INVISIBLE);
                                try {
                                    if(response.getBoolean("success")){
                                        JSONArray results = response.getJSONArray("results");
                                        for(int i=0; i<results.length(); i++){

                                            JSONObject profile = results.getJSONObject(i);

                                            deviceProfileViewArrayList.add(new deviceProfileView(
                                                    profile.getString("deviceMake"),
                                                    profile.getString("deviceModel"),
                                                    profile.getInt("upVotes"),
                                                    profile.getInt("downVotes"),
                                                    profile.getString("entryId"),
                                                    profile.getString("serverTime")));

                                        }
                                        deviceProfileAdapter.notifyDataSetChanged();

                                    }
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