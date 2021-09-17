package in.koshurtech.zstmx.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;
import static android.content.Context.CLIPBOARD_SERVICE;


public class recyclerInfoViewAdapter extends RecyclerView.Adapter<recyclerInfoViewAdapter.ViewHolder> {

    ArrayList<recyclerInfoView> recyclerInfoViewArrayList;
    Context context;
    Activity activity;

    public recyclerInfoViewAdapter(ArrayList<recyclerInfoView> recyclerInfoViewArrayList, Context context, Activity activity){
        this.recyclerInfoViewArrayList = recyclerInfoViewArrayList;
        this.context = context;
        this.activity = activity;
    }



    public void updateList(ArrayList<recyclerInfoView> update){
        recyclerInfoViewArrayList = update;
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public recyclerInfoViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_info_view, parent, false);
        return new recyclerInfoViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull recyclerInfoViewAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        recyclerInfoView recyclerInfoView = recyclerInfoViewArrayList.get(position);
        holder.getCardView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, "Copied to Clipboard",
                        Snackbar.LENGTH_SHORT)
                        .setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("zstmX : Clipboard Payload", recyclerInfoView.getInfoHeader()+" : "+recyclerInfoView.getInfoBody());
                clipboard.setPrimaryClip(clip);
                return true;
            }
        });

        holder.setBody(recyclerInfoView.getInfoBody());
        holder.setHeader(recyclerInfoView.getInfoHeader());
    }

    @Override
    public int getItemCount() {
        return recyclerInfoViewArrayList == null ? 0 : recyclerInfoViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        TextView header;
        TextView body;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView.findViewById(R.id.recycler_info_view_CV);
            header = (TextView) itemView.findViewById(R.id.infoHead);
            body = (TextView) itemView.findViewById(R.id.infoBody);
        }

        public MaterialCardView getCardView() {
            return cardView;
        }
        public void setHeader(String header) {
            this.header.setText(header);
        }
        public void setBody(String body) {
            this.body.setText(body);
        }
    }
}
