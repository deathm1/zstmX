package in.koshurtech.zstmx.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.viewModels.sensorViewModel;

public class sensorActivity extends AppCompatActivity {


    TextView sensorData;
    TextView sensorValues;
    LineChart lineChart;

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
        setContentView(R.layout.activity_sensor);
        getSupportActionBar().setElevation(0);
        Bundle extras = getIntent().getExtras();


        sensorData = (TextView) findViewById(R.id.sensorValues);
        sensorValues = (TextView) findViewById(R.id.sensorInfo);


        lineChart = (LineChart) findViewById(R.id.sensor_graph);

        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getDescription().setEnabled(false);

        lineChart.setDrawingCacheBackgroundColor(Color.parseColor("#2962ff"));

        Legend l2 = lineChart.getLegend();
        l2.setTextColor(Color.parseColor("#2962ff"));
        l2.setEnabled(true);





        sensorData.setText(extras.getString("name")+"\n\n"+extras.getString("body").replace("--",""));

        String[] seq = extras.getString("body").split("--");
        HashMap<String,String> hm = new HashMap<>();
        for (int i=0; i<seq.length; i++){
            String[] spw = seq[i].split(":");
            hm.put(spw[0].trim(),spw[1].trim());
        }


        final int[] sent = {0};

        hm.forEach((key, value) -> {
            if (key.equals("Type")) {
                sent[0] = Integer.parseInt(value);
            }

        });


        sensorViewModel = new ViewModelProvider(this).get(in.koshurtech.zstmx.viewModels.sensorViewModel.class);
        sensorViewModel.setSystem(getApplicationContext(),sensorActivity.this,sent[0]);

        sensorValues.setText("Values can not be graphed.");

        final double[] avg = new double[1];


        try {
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            ArrayList<Integer> colorList = new ArrayList<>();

            LiveData<String> sensorInfoLive = sensorViewModel.getSensorLiveData();
            sensorInfoLive.observe(sensorActivity.this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    sensorValues.setText(s);
                    ArrayList<Double> dataarray = new ArrayList<Double>();

                    String s1 = s.replace("[","");
                    String s2 = s1.replace("]","");
                    String s3 = s2.trim();

                    String[] data = s3.split(",");
                    double[] toint = new double[data.length];
                    double sum  =0 ;
                    for(int i=0; i<toint.length; i++){
                        dataarray.add(Double.parseDouble(data[i]));
                    }

                    for(int i=0; i<data.length; i++){
                        sum = sum + Double.parseDouble(data[i]);
                    }


                    avg[0] = sum/data.length;




                    if(firstTime){
                        for(int i=0; i<dataarray.size(); i++){
                            for(int j=0; j<=i; j++){
                                if(i==j){
                                    ArrayList<Entry> values = new ArrayList<>();
                                    values.add(new Entry(counter,Float.parseFloat(String.valueOf(dataarray.get(i)))));
                                    LineDataSet ds = new LineDataSet(values, String.valueOf(i));
                                    ds.setDrawCircles(false);
                                    Random rnd = new Random();
                                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                                    ds.setColor(color);
                                    ds.setLineWidth(2f);
                                    colorList.add(color);
                                    dataSets.add(ds);

                                }
                            }

                        }
                        firstTime = false;
                    }


                    if(!firstTime){
                        for(int i=0; i<dataarray.size(); i++){
                            for(int j=0; j<=i; j++){
                                if(i==j){
                                    ILineDataSet ids = dataSets.get(i);
                                    //get array
                                    ArrayList<Entry> val = new ArrayList<>();

                                    for(int z=0; z<ids.getEntryCount(); z++){
                                        val.add(z,ids.getEntryForIndex(z));
                                    }

                                    val.add(new Entry(counter,Float.parseFloat(String.valueOf(dataarray.get(i)))));

                                    LineDataSet ds = new LineDataSet(val, String.valueOf(i));
                                    ds.setDrawCircles(false);
                                    ds.setColor(colorList.get(i));
                                    ds.setLineWidth(2f);
                                    dataSets.remove(i);
                                    dataSets.add(i,ds);

                                }
                            }

                        }
                    }




                    LineData lineData = new LineData(dataSets);




                    XAxis xAxis2 = lineChart.getXAxis();
                    xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis2.setTextSize(15f);
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
                    counter++;
                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    sensorViewModel sensorViewModel;
    int counter = 0;
    boolean firstTime = true;
    public void openLink(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/guide/topics/sensors"));
        startActivity(intent);
    }
}