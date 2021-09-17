package in.koshurtech.zstmx.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.resultDataView;
import in.koshurtech.zstmx.activities.textView;
import in.koshurtech.zstmx.javaClasses.resultView;
import in.koshurtech.zstmx.javaClasses.sensorInfoView;


public class resultViewAdapter extends RecyclerView.Adapter<resultViewAdapter.ViewHolder>{


    ArrayList<resultView> resultViewArrayList;
    ArrayList<resultView> selectionList = new ArrayList<>();
    Context context;
    Activity activity;


    public void updateList(ArrayList<resultView> update){
        resultViewArrayList = update;
        notifyDataSetChanged();
    }

    public resultViewAdapter(ArrayList<resultView> resultViewArrayList,
            Context context,
            Activity activity){
        this.resultViewArrayList = resultViewArrayList;
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @Override
    public resultViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.results_view, parent, false);
        return new resultViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull resultViewAdapter.ViewHolder holder, int position) {
        final resultView resultView = resultViewArrayList.get(position);
        if(allSelected){

            holder.setIsRecyclable(false);
            holder.setHead(resultView.getHead());
            holder.setBody(resultView.getBody());


            Random rnd = new Random();
            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] colors = new int[] {
                    Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                    Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                    Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                    Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            };

            ColorStateList myList = new ColorStateList(states, colors);
            holder.materialCardView.setCardForegroundColor(myList);
        }


        holder.setIsRecyclable(false);
        holder.setHead(resultView.getHead());
        holder.setBody(resultView.getBody());


        Random rnd = new Random();
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        };

        ColorStateList myList = new ColorStateList(states, colors);
        holder.materialCardView.setRippleColor(myList);

        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longSelection){
                    if(!selectionList.contains(resultView)){
                        holder.materialCardView.setCardForegroundColor(null);
                        holder.materialCardView.setCardForegroundColor(myList);
                        selectionList.add(resultView);
                    }
                    else {
                        holder.materialCardView.setCardForegroundColor(null);
                        selectionList.remove(resultView);
                    }
                    if(selectionList.size()==0){
                        actionMode.finish();
                    }
                    actionMode.setTitle(String.valueOf(selectionList.size())+" Selected");
                }
                else {

                    if(resultView.isType()){
                        Intent intent = new Intent(context, textView.class);
                        intent.putExtra("head",resultView.getHead());
                        intent.putExtra("body",resultView.getBody());
                        activity.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(context, resultDataView.class);
                        intent.putExtra("head",resultView.getHead());
                        intent.putExtra("body",resultView.getBody());
                        activity.startActivity(intent);
                    }

                }
            }
        });

        holder.materialCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!longSelection){
                    //ui
                    longSelection = true;
                    holder.materialCardView.setCardForegroundColor(null);
                    holder.materialCardView.setCardForegroundColor(myList);

                    selectionList.add(resultView);



                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            actionMode = mode;
                            mode.setTitle("1 Selected");
                            menuInflater.inflate(R.menu.on_long_click_rv, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            int id = item.getItemId();
                            switch (id){
                                case R.id.deleteFile:

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        ContextWrapper contextWrapper = new ContextWrapper(context);
                                        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                        File[] files = directory.listFiles();
                                        System.out.println(files.length);

                                        for (File f : files
                                        ) {
                                            for (resultView r: selectionList
                                            ) {
                                                if(f.getName().equals(r.getHead())){
                                                    f.delete();
                                                }
                                                resultViewArrayList.removeAll(selectionList);
                                                notifyDataSetChanged();
                                            }
                                        }

                                    }
                                    else {
                                        String path = Environment.getExternalStorageDirectory().toString()+"/zstmX Benchmark Reports";
                                        File directory = new File(path);
                                        File[] files = directory.listFiles();
                                        System.out.println(files.length);

                                        for (File f : files
                                        ) {
                                            for (resultView r: selectionList
                                            ) {
                                                if(f.getName().equals(r.getHead())){
                                                    f.delete();
                                                }
                                                resultViewArrayList.removeAll(selectionList);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }
                                    mode.finish();

                                    break;
                                case R.id.select_all:
                                    allSelected = true;
                                    selectionList.clear();
                                    selectionList.addAll(resultViewArrayList);
                                    mode.setTitle("All Selected");
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            longSelection = false;
                            allSelected = false;
                            selectionList.clear();
                            notifyDataSetChanged();
                        }
                    };

                    activity.startActionMode(callback);
                }



                return true;
            }
        });


    }

    ActionMode actionMode;
    boolean longSelection = false;
    boolean allSelected = false;

    @Override
    public int getItemCount() {
        return resultViewArrayList== null ? 0:resultViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;
        TextView head;
        TextView body;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = (MaterialCardView) itemView.findViewById(R.id.openResult);
            head = (TextView) itemView.findViewById(R.id.resultTitle);
            body = (TextView) itemView.findViewById(R.id.resultBody);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
        }

        public MaterialCardView getMaterialCardView() {
            return materialCardView;
        }

        public void setHead(String head) {
            this.head.setText(head);
        }

        public void setBody(String body) {
            this.body.setText(body);
        }
    }
}
