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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.javaClasses.resultView;

public class resultDataView extends AppCompatActivity {


    TextView bodyTV;
    Boolean firstTime = true;

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
        setContentView(R.layout.activity_result_data_view);
        getSupportActionBar().setElevation(0);


        bodyTV = (TextView) findViewById(R.id.bodyTV);
        lineChart = (LineChart) findViewById(R.id.batteryResultGraph);


        String head = getIntent().getStringExtra("head");
        headd = head;
        String body = getIntent().getStringExtra("body");
        bodyd = body;
        bodyTV.setText(body);








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



        if(getFile!=null){
            try {
                jsonArray = new JSONArray(readFromFile(getFile));

                if(jsonArray.length()>=1){
                    batteryInfo = jsonArray.getJSONArray(0);

                    lineChart.setDragEnabled(true);
                    lineChart.setScaleEnabled(true);
                    lineChart.setScaleXEnabled(true);
                    lineChart.setScaleYEnabled(true);
                    lineChart.setTouchEnabled(true);
                    lineChart.setPinchZoom(true);
                    lineChart.setDoubleTapToZoomEnabled(true);
                    lineChart.getDescription().setEnabled(false);

                    lineChart.setDrawingCacheBackgroundColor(Color.parseColor("#2962ff"));

                    Legend l2 = lineChart.getLegend();
                    l2.setTextColor(Color.parseColor("#2962ff"));
                    l2.setEnabled(true);



                    for(int i=0; i<batteryInfo.length(); i++){
                        //here i is time
                        //sss is val
                        String[] sss = batteryInfo.getJSONObject(i).getString("result").split("--");
                        if(firstTime){
                            for(int j=0; j<sss.length; j++){
                                if(!sss[j].equals("")){
                                    String numberOnly = String.valueOf(sss[j]).replaceAll("[^0-9]", "");

                                    System.out.println(Arrays.toString(sss));
                                    if(!numberOnly.equals("")){
                                        String label = "";
                                        if(j==0){
                                            label = "Percentage";
                                        }
                                        else  if(j==2){
                                            label = "Voltage";
                                            int num = Integer.parseInt(numberOnly)/1000;
                                            numberOnly = String.valueOf(num);
                                        }
                                        else if(j==3){
                                            label = "Temperature";
                                            int num = Integer.parseInt(numberOnly)/10;
                                            numberOnly = String.valueOf(num);
                                        }

                                        ArrayList<Entry> values = new ArrayList<>();
                                        values.add(new Entry(i,Float.parseFloat(numberOnly)));

                                        LineDataSet ds = new LineDataSet(values, label);
                                        ds.setDrawCircles(false);
                                        Random rnd = new Random();
                                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                                        ds.setColor(color);
                                        ds.setLineWidth(2f);
                                        batteryInfoColorList.add(color);
                                        batteryInfoDataSets.add(ds);
                                    }
                                }
                            }
                            firstTime = false;
                        }else {
                            System.out.println(batteryInfoDataSets.size());
                            int f = 0;
                            for(int j=0; j<sss.length; j++){
                                if(!sss[j].equals("")){
                                    String numberOnly = String.valueOf(sss[j]).replaceAll("[^0-9]", "");

                                    if(!numberOnly.equals("")){
                                        String label = "";
                                        if(j==0){
                                            label = "Percentage";
                                        }
                                        else  if(j==2){
                                            label = "Voltage";
                                            int num = Integer.parseInt(numberOnly)/1000;
                                            numberOnly = String.valueOf(num);
                                        }
                                        else if(j==3){
                                            label = "Temperature";
                                            int num = Integer.parseInt(numberOnly)/10;
                                            numberOnly = String.valueOf(num);
                                        }
                                        ILineDataSet ids = batteryInfoDataSets.get(f);
                                        ArrayList<Entry> values = new ArrayList<>();
                                        for(int z=0; z<ids.getEntryCount(); z++){
                                            values.add(z,ids.getEntryForIndex(z));
                                        }
                                        values.add(new Entry(i,Float.parseFloat(numberOnly)));

                                        LineDataSet ds = new LineDataSet(values, label);
                                        ds.setDrawCircles(false);
                                        ds.setColor(batteryInfoColorList.get(f));
                                        ds.setLineWidth(2f);
                                        batteryInfoDataSets.remove(f);
                                        batteryInfoDataSets.add(f,ds);


                                        f++;
                                    }
                                }

                            }
                        }


                    }
                    LineData lineData = new LineData(batteryInfoDataSets);




                    XAxis xAxis2 = lineChart.getXAxis();
                    xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis2.setTextSize(15f);
                    System.out.println(batteryInfo.length());
                    xAxis2.setAxisMaximum(Float.parseFloat(String.valueOf(batteryInfo.length())));

                    xAxis2.setAxisMinimum(0);
                    xAxis2.setGranularity(1f);
                    xAxis2.setLabelCount(10);
                    xAxis2.setTextColor(Color.parseColor("#2962ff"));
                    xAxis2.setDrawGridLines(false);





                    YAxis yAxis2 = lineChart.getAxisRight();
                    yAxis2.setEnabled(false);

                    yAxis2.setDrawGridLines(false);


                    YAxis yAxis22 = lineChart.getAxisLeft();
                    yAxis22.setDrawGridLines(false);
                    yAxis22.setTextSize(15f);
                    yAxis22.setTextColor(Color.parseColor("#2962ff"));
                    lineChart.setData(lineData);
                    lineChart.invalidate();
                    lineChart.getLineData().setDrawValues(false);




                }
                if(jsonArray.length()>=2){
                    cpuInfo = jsonArray.getJSONArray(1);
                }
                if(jsonArray.length()>=3){
                    storageInfo = jsonArray.getJSONArray(2);
                }
                if(jsonArray.length()>=4){
                    ramInfo = jsonArray.getJSONArray(3);
                }
                if(jsonArray.length()>=5){
                    thermalInfo = jsonArray.getJSONArray(4);
                }
                if(jsonArray.length()>=6){
                    networkInfo = jsonArray.getJSONArray(5);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }




        System.out.println(pl);
    }





    String pl = "";


    File getFile;

    JSONArray jsonArray;


    JSONArray batteryInfo;
    ArrayList<ILineDataSet> batteryInfoDataSets = new ArrayList<>();
    ArrayList<Integer> batteryInfoColorList = new ArrayList<>();


    String headd = "";
    String bodyd = "";

    JSONArray cpuInfo;
    JSONArray networkInfo;
    JSONArray storageInfo;
    JSONArray ramInfo;
    JSONArray thermalInfo;
    LineChart lineChart;

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

    public void OpenBatteryResults(View view) {

        Intent intent = new Intent(getApplicationContext(), rawView.class);
        intent.putExtra("head",headd);
        intent.putExtra("body",bodyd);
        intent.putExtra("index",0);
        startActivity(intent);
    }
}