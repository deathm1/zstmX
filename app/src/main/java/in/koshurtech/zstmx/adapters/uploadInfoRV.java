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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.javaClasses.checkBoxProperties;
import in.koshurtech.zstmx.javaClasses.uploadInfoViewHolder;

public class uploadInfoRV extends RecyclerView.Adapter<uploadInfoRV.ViewHolder>{



    private ArrayList<uploadInfoViewHolder> uploadInfoViewHolderArrayList;
    private HashMap<String,ArrayList<checkBoxProperties>> fullReviewedInfo = new HashMap<>();
    private HashMap<String,ArrayList<checkBoxProperties>> fullReviewedInfoTemp = new HashMap<>();

    private Context context;
    private Activity activity;


    public uploadInfoRV(ArrayList<uploadInfoViewHolder> uploadInfoViewHolderArrayList, Context context, Activity activity){
        this.activity = activity;
        this.context = context;
        this.uploadInfoViewHolderArrayList = uploadInfoViewHolderArrayList;
    }

    @NonNull
    @Override
    public uploadInfoRV.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.upload_info_layout, parent, false);
        return new uploadInfoRV.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull uploadInfoRV.ViewHolder holder, int position) {

        final uploadInfoViewHolder uploadInfoViewHolder = uploadInfoViewHolderArrayList.get(position);





        holder.setTextView(uploadInfoViewHolder.getTableGroup());



        ArrayList<checkBoxProperties> checkBoxPropertiesArrayList = new ArrayList<>();




        try {
            JSONArray jsonArray = new JSONArray(uploadInfoViewHolder.getTableData());

            for(int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                checkBoxPropertiesArrayList.add(new checkBoxProperties(jsonObject.toString()));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        profileProperties profileProperties = new profileProperties(checkBoxPropertiesArrayList,context,activity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

        holder.getRecyclerView().setLayoutManager(layoutManager);
        holder.getRecyclerView().setAdapter(profileProperties);


        fullReviewedInfo.put(uploadInfoViewHolder.getTableGroup(),profileProperties.getCheckBoxPropertiesArrayList());


        holder.getSelectableCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getRecyclerView().getVisibility()==View.VISIBLE){
                    holder.getRecyclerView().setVisibility(View.GONE);
                    holder.getImageView().setRotation(0);
                }
                else if(holder.getRecyclerView().getVisibility()==View.GONE){
                    holder.getRecyclerView().setVisibility(View.VISIBLE);
                    holder.getImageView().setRotation(90);
                }
            }
        });



        holder.getMaterialCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getMaterialCheckBox().isChecked()){
                    fullReviewedInfoTemp.remove(uploadInfoViewHolder.getTableGroup(),profileProperties.getCheckBoxPropertiesArrayList());
                    fullReviewedInfo.put(uploadInfoViewHolder.getTableGroup(),profileProperties.getCheckBoxPropertiesArrayList());
                }
                else if(!holder.getMaterialCheckBox().isChecked()){
                    fullReviewedInfoTemp.put(uploadInfoViewHolder.getTableGroup(),profileProperties.getCheckBoxPropertiesArrayList());
                    fullReviewedInfo.remove(uploadInfoViewHolder.getTableGroup(),profileProperties.getCheckBoxPropertiesArrayList());
                }
            }
        });



    }


    public HashMap<String,ArrayList<checkBoxProperties>> getFullReviewedInfo(){
//        fullReviewedInfoTemp.entrySet().forEach(entry -> {
//            fullReviewedInfo.entrySet().removeAll(fullReviewedInfo.entrySet());
//
//        });


        return fullReviewedInfo;
    }











    @Override
    public int getItemCount() {
        return uploadInfoViewHolderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private RecyclerView recyclerView;
        private MaterialCardView selectableCard;
        private ImageView imageView;
        private MaterialCheckBox materialCheckBox;



        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.infoHeader);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.propertyHolder);
            selectableCard = (MaterialCardView) itemView.findViewById(R.id.selectableCard);
            imageView = (ImageView) itemView.findViewById(R.id.arrowOfInfo);
            materialCheckBox = (MaterialCheckBox) itemView.findViewById(R.id.isGroupSelected);

        }

        public MaterialCheckBox getMaterialCheckBox() {
            return materialCheckBox;
        }

        public MaterialCardView getSelectableCard() {
            return selectableCard;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setTextView(String textView) {
            this.textView.setText(textView);
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

    }
}
