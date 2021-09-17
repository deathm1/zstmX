package in.koshurtech.zstmx.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.showDeviceProfile;
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
                Intent intent = new Intent(context, showDeviceProfile.class);
                intent.putExtra("id",deviceProfileView.getProfileId());
                activity.startActivity(intent);
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
