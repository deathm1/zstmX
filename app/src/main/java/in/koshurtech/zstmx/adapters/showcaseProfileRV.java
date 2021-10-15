package in.koshurtech.zstmx.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.javaClasses.checkBoxProperties;
import in.koshurtech.zstmx.javaClasses.uploadInfoViewHolder;

public class showcaseProfileRV extends RecyclerView.Adapter<showcaseProfileRV.viewHolder> {

    private Activity activity;
    private Context context;

    ArrayList<uploadInfoViewHolder> uploadInfoViewHolderArrayList;

    public showcaseProfileRV(ArrayList<uploadInfoViewHolder> uploadInfoViewHolderArrayList, Activity activity, Context context){
        this.activity = activity;
        this.context = context;
        this.uploadInfoViewHolderArrayList = uploadInfoViewHolderArrayList;
    }


    @NonNull
    @Override
    public showcaseProfileRV.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.showcase_rv, parent, false);
        return new showcaseProfileRV.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull showcaseProfileRV.viewHolder holder, int position) {


        final uploadInfoViewHolder uploadInfoViewHolder = uploadInfoViewHolderArrayList.get(position);
        holder.setHeader(uploadInfoViewHolder.getTableGroup());

        ArrayList<checkBoxProperties> checkBoxPropertiesArrayList = new ArrayList<>();

        System.out.println(uploadInfoViewHolder.getTableGroup());
        System.out.println(uploadInfoViewHolder.getTableData());



        try {
            JSONArray jsonArray = new JSONArray(uploadInfoViewHolder.getTableData());

            for(int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                checkBoxPropertiesArrayList.add(new checkBoxProperties(jsonObject.toString()));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        showcaseProperties showcaseProperties = new showcaseProperties(checkBoxPropertiesArrayList,activity,context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        holder.getRecyclerView().setLayoutManager(layoutManager);
        holder.getRecyclerView().setAdapter(showcaseProperties);



        holder.getMaterialCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getRecyclerView().getVisibility()==View.VISIBLE){
                    holder.getRecyclerView().setVisibility(View.GONE);
                    holder.getArrow().setRotation(0);
                }
                else if(holder.getRecyclerView().getVisibility()==View.GONE){
                    holder.getRecyclerView().setVisibility(View.VISIBLE);
                    holder.getArrow().setRotation(90);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return uploadInfoViewHolderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView header;
        private ImageView arrow;
        private RecyclerView recyclerView;
        private MaterialCardView materialCardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.showcaseGroupHeader);
            arrow = (ImageView) itemView.findViewById(R.id.showcaseMoreInfoArrow);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.showcaseRV);
            materialCardView = (MaterialCardView) itemView.findViewById(R.id.selectionCV);
        }



        public MaterialCardView getMaterialCardView() {
            return materialCardView;
        }
        public void setMaterialCardView(MaterialCardView materialCardView) {
            this.materialCardView = materialCardView;
        }
        public void setHeader(String header) {
            this.header.setText(header);
        }
        public ImageView getArrow() {
            return arrow;
        }
        public RecyclerView getRecyclerView() {
            return recyclerView;
        }
        public void setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }
    }
}
