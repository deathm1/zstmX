package in.koshurtech.zstmx.activities.settingsPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.deviceMonitorResults;
import in.koshurtech.zstmx.adapters.resultViewAdapter;
import in.koshurtech.zstmx.javaClasses.resultView;

public class showReports extends AppCompatActivity {


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    in.koshurtech.zstmx.adapters.resultViewAdapter resultViewAdapter;
    in.koshurtech.zstmx.javaClasses.resultView resultView;
    ArrayList<resultView> resultViewArrayList = new ArrayList<>();
    LinearProgressIndicator linearProgressIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("appTheme", "0");
        if (theme.equals("0")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else if (theme.equals("1")) {
            setTheme(R.style.Theme_ZstmX_day);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            getSupportActionBar().setElevation(0);
        } else if (theme.equals("2")) {
            setTheme(R.style.Theme_ZstmX_night);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));

        }
        setContentView(R.layout.activity_show_reports);
        getSupportActionBar().setElevation(0);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.reportViewSRL);
        recyclerView = (RecyclerView) findViewById(R.id.reportsRV);
        resultViewAdapter = new resultViewAdapter(resultViewArrayList, getApplicationContext(), showReports.this);
        linearProgressIndicator = (LinearProgressIndicator) findViewById(R.id.resultsLoading);
        linearProgressIndicator.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(showReports.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(resultViewAdapter);
        getFilesAndShow();
        linearProgressIndicator.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getFilesAndShow();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void getFilesAndShow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resultViewArrayList.clear();
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File[] files = directory.listFiles();

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {

                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });


            for (File f : files
            ) {
                try {
                    JSONArray jsonArray = new JSONArray(readFromFile(f));
                } catch (JSONException e) {
                    e.printStackTrace();
                    resultViewArrayList.add(new resultView(f.getName(), f.getAbsolutePath(),true));
                }
            }

            resultViewAdapter.updateList(resultViewArrayList);
        } else {
            resultViewArrayList.clear();
            String path = Environment.getExternalStorageDirectory().toString() + "/zstmX Reports";
            File directory = new File(path);
            File[] files = directory.listFiles();

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {

                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });


            for (File f : files
            ) {
                resultViewArrayList.add(new resultView(f.getName(), f.getAbsolutePath(),true));
            }

            resultViewAdapter.updateList(resultViewArrayList);
        }

    }


    private String time(String longtime) {
        Date date = new Date(Long.parseLong(longtime));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
}