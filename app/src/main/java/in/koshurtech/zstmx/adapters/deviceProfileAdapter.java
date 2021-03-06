package in.koshurtech.zstmx.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.profileShowcase;
import in.koshurtech.zstmx.javaClasses.deviceProfileView;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;

public class deviceProfileAdapter extends RecyclerView.Adapter<deviceProfileAdapter.ViewHolder> {



    private ArrayList<deviceProfileView> deviceProfileViewArrayList;
    private Context context;
    private Activity activity;

    public void updateList(ArrayList<deviceProfileView> update){
        deviceProfileViewArrayList = update;
        notifyDataSetChanged();
    }

    public deviceProfileAdapter(ArrayList<deviceProfileView> deviceProfileViewArrayList, Context context, Activity activity){
        this.deviceProfileViewArrayList = deviceProfileViewArrayList;
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @Override
    public deviceProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.device_card, parent, false);
        return new deviceProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull deviceProfileAdapter.ViewHolder holder, int position) {
        final deviceProfileView deviceProfileView = deviceProfileViewArrayList.get(position);

        holder.setMake(deviceProfileView.getDeviceMake());
        holder.setModel(deviceProfileView.getDeviceModel());
        int up = deviceProfileView.getUpVotes();
        int down = deviceProfileView.getDownVotes();
        int sum = up+down;
        double perc = (Double.parseDouble(String.valueOf(up))/(Double.parseDouble(String.valueOf(sum))))*100.00;
        double roundOff = Math.round(perc * 100.0) / 100.0;
        holder.setReliability("Reliability : "+String.valueOf(roundOff) + " %");
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, profileShowcase.class);
                intent.putExtra("id",deviceProfileView.getProfileId());
                activity.startActivity(intent);
            }
        });



        holder.getUpVote().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getUpVote().setEnabled(false);
                SafetyNet.getClient(activity).verifyWithRecaptcha(context.getString(R.string.recaptchaKey))
                        .addOnSuccessListener(activity,
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
                                            payload.put("deviceId", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                                            payload.put("entryId",deviceProfileView.getProfileId());

                                            JsonObjectRequest jsonObjectRequest =
                                                    new JsonObjectRequest(Request.Method.POST,
                                                            context.getString(R.string.putUpVote),
                                                            new JSONObject(payload),
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    holder.getUpVote().setEnabled(true);
                                                                    try {
                                                                        Snackbar.make(v, response.getString("status"),
                                                                                Snackbar.LENGTH_SHORT)
                                                                                .setAction("Dismiss", new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                    }
                                                                                }).show();
                                                                    }
                                                                    catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, new Response.ErrorListener() {



                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            holder.getUpVote().setEnabled(true);


                                                            NetworkResponse networkResponse = error.networkResponse;
                                                            if(error instanceof ServerError && networkResponse!=null){
                                                                try{
                                                                    String res = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers,  "utf-8"));
                                                                    Snackbar.make(v, res,
                                                                            Snackbar.LENGTH_SHORT)
                                                                            .setAction("Dismiss", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                }
                                                                            }).show();
                                                                }
                                                                catch (Exception err){
                                                                    err.printStackTrace();
                                                                    Snackbar.make(v, "Unknown Error",
                                                                            Snackbar.LENGTH_SHORT)
                                                                            .setAction("Dismiss", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                }
                                                                            }).show();
                                                                }
                                                            }
                                                        }
                                                    }){
                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                            return headers;
                                                        }
                                                    };

                                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                    0,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                            requestQueue.add(jsonObjectRequest);

                                        }
                                    }
                                })
                        .addOnFailureListener(activity, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    int statusCode = apiException.getStatusCode();
                                    Toast.makeText(activity,+statusCode+" : "+e.toString(),Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(activity,"Some unknown occurred, please try again or restart the application.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        holder.getDownVote().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getUpVote().setEnabled(false);
                SafetyNet.getClient(activity).verifyWithRecaptcha(context.getString(R.string.recaptchaKey))
                        .addOnSuccessListener(activity,
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
                                            payload.put("deviceId", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                                            payload.put("entryId",deviceProfileView.getProfileId());

                                            JsonObjectRequest jsonObjectRequest =
                                                    new JsonObjectRequest(Request.Method.POST,
                                                            context.getString(R.string.putDownVote),
                                                            new JSONObject(payload),
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    holder.getUpVote().setEnabled(true);
                                                                    try {
                                                                        Snackbar.make(v, response.getString("status"),
                                                                                Snackbar.LENGTH_SHORT)
                                                                                .setAction("Dismiss", new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                    }
                                                                                }).show();
                                                                    }
                                                                    catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, new Response.ErrorListener() {



                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {


                                                            holder.getUpVote().setEnabled(true);
                                                            NetworkResponse networkResponse = error.networkResponse;
                                                            if(error instanceof ServerError && networkResponse!=null){
                                                                try{
                                                                    String res = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers,  "utf-8"));
                                                                    Snackbar.make(v, res,
                                                                            Snackbar.LENGTH_SHORT)
                                                                            .setAction("Dismiss", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                }
                                                                            }).show();
                                                                }
                                                                catch (Exception err){
                                                                    err.printStackTrace();
                                                                    Snackbar.make(v, "Unknown Error",
                                                                            Snackbar.LENGTH_SHORT)
                                                                            .setAction("Dismiss", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                }
                                                                            }).show();
                                                                }
                                                            }
                                                        }
                                                    }){
                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                            return headers;
                                                        }
                                                    };

                                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                    0,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                            requestQueue.add(jsonObjectRequest);

                                        }
                                    }
                                })
                        .addOnFailureListener(activity, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    int statusCode = apiException.getStatusCode();
                                    Toast.makeText(activity,+statusCode+" : "+e.toString(),Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(activity,"Some unknown occurred, please try again or restart the application.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceProfileViewArrayList== null ? 0:deviceProfileViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView make;
        private TextView model;
        private TextView reliability;
        private MaterialButton upVote;
        private MaterialButton downVote;
        private MaterialCardView materialCardView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            make = (TextView) itemView.findViewById(R.id.deviceMake);
            model = (TextView) itemView.findViewById(R.id.deviceModel);
            reliability = (TextView) itemView.findViewById(R.id.reliabilityPercentage);
            upVote = (MaterialButton) itemView.findViewById(R.id.upVoteButton);
            downVote = (MaterialButton) itemView.findViewById(R.id.downVoteButton);
            materialCardView = (MaterialCardView) itemView.findViewById(R.id.deviceProfileClickable);
        }

        public MaterialCardView getMaterialCardView() {
            return materialCardView;
        }



        public void setMake(String make) {
            this.make.setText(make);
        }




        public void setModel(String model) {
            this.model.setText(model);
        }


        public void setReliability(String reliability) {
            this.reliability.setText(reliability);
        }

        public MaterialButton getUpVote() {
            return upVote;
        }


        public MaterialButton getDownVote() {
            return downVote;
        }
    }
}
