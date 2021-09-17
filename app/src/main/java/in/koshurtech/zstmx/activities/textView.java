package in.koshurtech.zstmx.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLConnection;

import in.koshurtech.zstmx.R;

public class textView extends AppCompatActivity {


    TextView reportHeadTV;
    TextView reportBodyTV;

    TextView fileText;

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
        setContentView(R.layout.activity_text_view);
        getSupportActionBar().setElevation(0);

        String head = getIntent().getStringExtra("head");
        String body = getIntent().getStringExtra("body");


        reportHeadTV = (TextView) findViewById(R.id.reportHeadTV);
        reportBodyTV = (TextView) findViewById(R.id.reportBodyTV);
        fileText = (TextView) findViewById(R.id.fileText);

        reportHeadTV.setText(head);
        reportBodyTV.setText(body);


       file = new File(body);



        fileText.setText(readFromFile(file));


    }
    File file;

    private String readFromFile(File file) {
        String out = "";

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            out = String.valueOf(sb);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return out;
    }

    public void ShareReport(View view) {
        //shareReport(file);
    }
    private void shareReport(File file) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        Uri screenshotUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", file);
        intentShareFile.setType("*/*");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, screenshotUri);



        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"WARNING");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "This file may contain critical information.");

        startActivity(Intent.createChooser(intentShareFile, "Share Report"));

    }

    public void copyText(View view) {
        Snackbar.make(view, "Copied to Clipboard",
                Snackbar.LENGTH_SHORT)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();

        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("zstmX : Clipboard Payload", readFromFile(file));
        clipboard.setPrimaryClip(clip);
    }
}