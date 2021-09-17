package in.koshurtech.zstmx.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.sensorActivity;
import in.koshurtech.zstmx.javaClasses.sensorInfoView;

public class sensorViewAdapter extends RecyclerView.Adapter<sensorViewAdapter.ViewHolder> {

    ArrayList<sensorInfoView> sensorViewArrayList;
    Context context;
    Activity activity;

    public sensorViewAdapter(ArrayList<sensorInfoView> sensorViewArrayList, Context context, Activity activity){
        this.sensorViewArrayList = sensorViewArrayList;
        this.activity = activity;
        this.context = context;
    }

    public void updateList(ArrayList<sensorInfoView> update){
        sensorViewArrayList = update;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public sensorViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sensor_select, parent, false);
        return new sensorViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(sensorViewAdapter.ViewHolder holder, int position) {
        final sensorInfoView sensorInfoView = sensorViewArrayList.get(position);

        holder.setSensorBody(sensorInfoView.getBody().replace("--",""));
        holder.setSensorName(sensorInfoView.getHeader());

        if(sensorInfoView.isClickable()){
            holder.getSensorCard().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, sensorActivity.class);
                    i.putExtra("name",sensorInfoView.getHeader());
                    i.putExtra("body",sensorInfoView.getBody());
                    context.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return sensorViewArrayList== null ? 0:sensorViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sensorName;
        TextView sensorBody;
        MaterialCardView sensorCard;
        public ViewHolder(View itemView) {
            super(itemView);
            sensorName = (TextView) itemView.findViewById(R.id.sensorName);
            sensorBody = (TextView) itemView.findViewById(R.id.sensorBody);
            sensorCard = (MaterialCardView) itemView.findViewById(R.id.sensorCard);
        }

        public MaterialCardView getSensorCard() {
            return sensorCard;
        }

        public void setSensorName(String sensorName) {
            this.sensorName.setText(sensorName);
        }

        public void setSensorBody(String sensorBody) {
            this.sensorBody.setText(sensorBody);
        }
    }
}
