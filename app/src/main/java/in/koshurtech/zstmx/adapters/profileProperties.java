package in.koshurtech.zstmx.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.javaClasses.checkBoxProperties;

public class profileProperties extends RecyclerView.Adapter<profileProperties.ViewHolder> {


    private ArrayList<checkBoxProperties> checkBoxPropertiesArrayList;

    private Context context;
    private Activity activity;

    public profileProperties(ArrayList<checkBoxProperties> checkBoxPropertiesArrayList, Context context, Activity activity){
        this.checkBoxPropertiesArrayList = checkBoxPropertiesArrayList;
        this.activity = activity;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.select_deselect_property, parent, false);
        return new profileProperties.ViewHolder(view);
    }

    ArrayList<checkBoxProperties> checkBoxPropertiesArrayListTemp = new ArrayList<>();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        ArrayList<checkBoxProperties> checkBoxPropertiesArrayList2 = checkBoxPropertiesArrayList;
        final checkBoxProperties checkBoxProperties = checkBoxPropertiesArrayList2.get(position);

        holder.setTextView(checkBoxProperties.getProperty().replace("{","").replace("}","").replace('"',' '));



        holder.getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(holder.getCheckBox().isChecked()){

                    checkBoxPropertiesArrayListTemp.remove(checkBoxProperties);
                    checkBoxPropertiesArrayList2.add(checkBoxProperties);
                }
                else {

                    checkBoxPropertiesArrayListTemp.add(checkBoxProperties);
                    checkBoxPropertiesArrayList2.remove(checkBoxProperties);
                }

            }
        });
    }


    public ArrayList<checkBoxProperties> getCheckBoxPropertiesArrayList(){
        ArrayList<checkBoxProperties> tempStore = checkBoxPropertiesArrayList;
        tempStore.removeAll(checkBoxPropertiesArrayListTemp);
        return tempStore;
    }

    @Override
    public int getItemCount() {
        return checkBoxPropertiesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCheckBox checkBox;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = (MaterialCheckBox) itemView.findViewById(R.id.rvCheckbox);
            textView = (TextView) itemView.findViewById(R.id.propertyAndDetails);
        }

        public MaterialCheckBox getCheckBox() {
            return checkBox;
        }



        public void setTextView(String textView) {
            this.textView.setText(textView);
        }
    }
}
