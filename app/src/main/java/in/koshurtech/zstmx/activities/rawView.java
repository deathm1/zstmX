package in.koshurtech.zstmx.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;

import in.koshurtech.zstmx.R;

public class rawView extends AppCompatActivity {



    TextView toShowText;


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(getApplicationContext(), resultDataView.class);
        intent.putExtra("head",headd);
        intent.putExtra("body",bodyd);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("appTheme","0");
        if(theme.equals("0")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if(theme.equals("1")){
            setTheme(R.style.Theme_ZstmX_day);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            getSupportActionBar().setElevation(0);
        }
        else if(theme.equals("2")){
            setTheme(R.style.Theme_ZstmX_night);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));

        }
        setContentView(R.layout.activity_raw_view);
        getSupportActionBar().setElevation(0);

        String head = getIntent().getStringExtra("head");
        String body = getIntent().getStringExtra("body");
        int index = getIntent().getIntExtra("index",0);


        headd = head;
        bodyd = body;

        toShowText = (TextView) findViewById(R.id.toShowText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File[] files = directory.listFiles();

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {

                    if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                        return -1;
                    } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });


            for (File f : files
            ) {
                try {
                    if(f.getName().equals(head)){
                        getFile = f;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        else{

            String path = Environment.getExternalStorageDirectory().toString()+"/zstmX Benchmark Reports";
            File directory = new File(path);
            File[] files = directory.listFiles();

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {

                    if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                        return -1;
                    } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });


            for (File f : files
            ) {
                try {
                    if(f.getName().equals(head)){
                        getFile = f;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


        try {
            JSONArray jsonArray = new JSONArray(readFromFile(getFile));


            String toShow = String.valueOf(jsonArray.getJSONArray(0));

            toShowText.setText(toShow);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    File getFile;


    private String readFromFile(File file) {
        String out = "";

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            out = String.valueOf(sb);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return out;
    }




    String headd= "";
    String bodyd = "";
}