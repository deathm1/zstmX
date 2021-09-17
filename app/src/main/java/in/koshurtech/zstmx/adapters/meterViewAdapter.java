package in.koshurtech.zstmx.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.javaClasses.meterView;

public class meterViewAdapter extends RecyclerView.Adapter<meterViewAdapter.ViewHolder>{

    ArrayList<meterView> meterViewArrayList;

    Context context;





    public meterViewAdapter(ArrayList<meterView> meterViewArrayList){
        this.meterViewArrayList = meterViewArrayList;

    }



    @NonNull
    @Override
    public meterViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.meter_view, parent, false);
        return new meterViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(meterViewAdapter.ViewHolder holder, int position) {
        final meterView meterView = meterViewArrayList.get(position);

        holder.setCoreTile(meterView.getMeterProgress(),meterView.getMeterText());
        holder.setMessage(meterView.getCardMessage());


    }

    @Override
    public int getItemCount() {
        return meterViewArrayList== null ? 0:meterViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView coreName;
        TextView corePercentage;
        String textMessage;

        MaterialCardView materialCardView;


        public ViewHolder(View itemView) {
            super(itemView);

            coreName = (TextView) itemView.findViewById(R.id.coreName);
            corePercentage = (TextView) itemView.findViewById(R.id.corePercentage);
            materialCardView = (MaterialCardView) itemView.findViewById(R.id.informationCard);

        }


        public void setMessage(String message){
            this.textMessage=message;
        }

        public void setCoreTile(int corePercentage, String coreName){
            this.coreName.setText(coreName);
            String set = String.valueOf(corePercentage)+" %";
            this.corePercentage.setText(set);
        }

        public MaterialCardView getMaterialCardView() {
            return materialCardView;
        }
    }
}
