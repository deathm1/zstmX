package in.koshurtech.zstmx.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.javaClasses.checkBoxProperties;


public class showcaseProperties extends RecyclerView.Adapter<showcaseProperties.ViewHolder> {


    private Activity activity;
    private Context context;
    private ArrayList<checkBoxProperties> uploadInfoViewHolderArrayList;

    public showcaseProperties(ArrayList<checkBoxProperties> uploadInfoViewHolderArrayList, Activity activity, Context context){
        this.activity = activity;
        this.context = context;
        this.uploadInfoViewHolderArrayList = uploadInfoViewHolderArrayList;
    }

    @NonNull
    @Override
    public showcaseProperties.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.showcase_properties, parent, false);
        return new showcaseProperties.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull showcaseProperties.ViewHolder holder, int position) {
        final checkBoxProperties checkBoxProperties = uploadInfoViewHolderArrayList.get(position);

        holder.setTextView(checkBoxProperties.getProperty().replace("{","").replace("}","").replace('"',' '));
    }

    @Override
    public int getItemCount() {
        return uploadInfoViewHolderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.showcaseProperty);
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(String textView) {
            this.textView.setText(textView);
        }
    }
}
