package in.koshurtech.zstmx.tabFragments.subFragments.cpuSubFragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.database.entities.cpuInformation;
import in.koshurtech.zstmx.viewModels.cpuInfoViewModel;


public class cpuGraphs extends Fragment {



    TextView graphData;
    TextView temperatureText;

    BarChart chart;
    cpuInfoViewModel cpuInfoViewModel;

    LineChart lineChart;
    ImageView imageView;

    TextView socName;
    TextView socInfo;


    in.koshurtech.zstmx.database.interfaces.accessCpuInformation accessCpuInformation;
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;


    List<cpuInformation> cpuInformationList = new ArrayList<>();

    Thread databaseQuery = new Thread(new Runnable() {
        @Override
        public void run() {
            systemInformationDatabase = in.koshurtech.zstmx.database.systemInformationDatabase.getInstance(getContext());
            accessCpuInformation = systemInformationDatabase.accessCPUInformation();

            cpuInformationList = accessCpuInformation.getCpuInformation();
        }
    });





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cpuInformationList.clear();
        databaseQuery.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab4, container, false);

        cpuInfoViewModel = new ViewModelProvider(getActivity()).get(in.koshurtech.zstmx.viewModels.cpuInfoViewModel.class);


        chart = (BarChart) v.findViewById(R.id.cpuBarChart);
        graphData = (TextView) v.findViewById(R.id.graphData);
        lineChart = (LineChart) v.findViewById(R.id.cpu_tmperature);
        temperatureText = (TextView) v.findViewById(R.id.graphDataTemp);
        imageView = (ImageView) v.findViewById(R.id.socImage);
        socName = (TextView) v.findViewById(R.id.socName);
        socInfo = (TextView) v.findViewById(R.id.socInformation);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> ff = new ArrayList<>();

                for (int i=0; i<cpuInformationList.size(); i++){
                    if(cpuInformationList.get(i).cpuProperty.equals("HARDWARE")){
                        String[] gg = cpuInformationList.get(i).propertyData.split(" ");
                        if(gg[0].toLowerCase().equals("qualcomm")){
                            imageView.setBackgroundResource(R.drawable.qcom);
                            socName.setText(cpuInformationList.get(i).propertyData);
                        }

                    }
                    if(cpuInformationList.get(i).cpuProperty.equals("PROCESSOR")){
                        ff.add(cpuInformationList.get(i).propertyData);
                    }
                }


                if(ff.get(0)!=null){
                    socInfo.setText(ff.get(0));
                }
            }
        },100);







        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);
        chart.setClickable(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setDrawingCacheBackgroundColor(Color.parseColor("#2962ff"));



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


        List<Entry> cpuTemps = new ArrayList<Entry>();
        List<Entry> socTemps = new ArrayList<Entry>();


        cpuInfoViewModel.setContext(getActivity().getApplicationContext());
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String frequency = sp.getString("stepSize","3");
        double time = 0.0;
        if(frequency.equals(String.valueOf(0))){
            time = 0.25;
        }
        else if(frequency.equals(String.valueOf(1))){
            time = 0.5;

        }
        else if(frequency.equals(String.valueOf(2))){
            time = 0.75;

        }
        else if(frequency.equals(String.valueOf(3))){
            time = 1.0;

        }

        final double[] timeCounter = {0};

        try {
            LiveData<String> cpuInfoLive = cpuInfoViewModel.getCpuLiveData();

            double finalTime = time;
            double finalTime1 = time;

            cpuInfoLive.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    //System.out.println(s);
                    String[] cpuData = s.split(" ");
                    String frequency = sp.getString("frequency","0");

                    boolean isGhz = false;
                    if(frequency.equals("0")){
                        //MHz
                        isGhz = false;
                        graphData.setText("X - Axis : Core Name (Integer)\nY - Axis : Frequency (MHz)");
                    }
                    else if(frequency.equals("1")){
                        //GHz
                        isGhz = true;
                        graphData.setText("X - Axis : Core Name (Integer)\nY - Axis : Frequency (GHz)");
                    }


                    int coreData = 4;
                    ArrayList<Double> coreMax = new ArrayList<Double>();
                    ArrayList<Double> coreCurrent = new ArrayList<Double>();
                    ArrayList<Double> coreMin = new ArrayList<Double>();

                    double maxAvg = 0;
                    double minAvg = 0;
                    double currentAvg = 0;

                    for(int i=0; i<Integer.parseInt(cpuData[3]); i++){
                        if (isGhz){
                            coreMax.add(Double.parseDouble(cpuData[coreData])/(1000*1000));
                            coreCurrent.add(Double.parseDouble(cpuData[coreData+2])/(1000*1000));
                            coreMin.add(Double.parseDouble(cpuData[coreData+1])/(1000*1000));

                            maxAvg = maxAvg + (Double.parseDouble(cpuData[coreData])/(1000*1000));
                            minAvg = minAvg + (Double.parseDouble(cpuData[coreData+1])/(1000*1000));
                            currentAvg = (Double.parseDouble(cpuData[coreData+2])/(1000*1000));
                        }
                        else {
                            coreMax.add(Double.parseDouble(cpuData[coreData])/1000);
                            coreCurrent.add(Double.parseDouble(cpuData[coreData+2])/1000);
                            coreMin.add(Double.parseDouble(cpuData[coreData+1])/1000);

                            maxAvg = maxAvg + (Double.parseDouble(cpuData[coreData])/1000);
                            minAvg = minAvg + (Double.parseDouble(cpuData[coreData+1])/1000);
                            currentAvg = (Double.parseDouble(cpuData[coreData+2])/1000);
                        }

                        coreData = coreData + 3;
                    }
//

                    XAxis xAxis = chart.getXAxis();

                    xAxis.setDrawGridLines(false);
                    xAxis.setTextColor(Color.parseColor("#2962ff"));
                    xAxis.setAxisMaximum(Integer.parseInt(cpuData[3]));
                    xAxis.setLabelCount(Integer.parseInt(cpuData[3])+2,true);
                    xAxis.setAxisMinimum(-1);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



                    YAxis leftAxis = chart.getAxisLeft();
                    leftAxis.setLabelCount(10, true);
                    leftAxis.setTextColor(Color.parseColor("#2962ff"));
                    leftAxis.setDrawGridLines(false);
                    leftAxis.setAxisMinimum(0);
                    leftAxis.setAxisMaximum(Float.parseFloat(String.valueOf(maxAvg/Integer.parseInt(cpuData[3]))));

                    YAxis rightAxis = chart.getAxisRight();
                    rightAxis.setEnabled(false);

                    Legend l = chart.getLegend();
                    l.setEnabled(true);
                    l.setTextColor(Color.parseColor("#2962ff"));
                    l.setTextSize(10);





                    BarDataSet set1;
                    BarDataSet set2;
                    BarDataSet set3;
                    ArrayList<BarEntry> values = new ArrayList<>();
                    ArrayList<BarEntry> values2 = new ArrayList<>();
                    ArrayList<BarEntry> values3 = new ArrayList<>();

                    for (int i = 0; i < Integer.parseInt(cpuData[3]); i++) {
                        values.add(new BarEntry(i, Float.parseFloat(String.valueOf(coreCurrent.get(i)))));
                        values2.add(new BarEntry(i, Float.parseFloat(String.valueOf(coreMin.get(i)))));
                        values3.add(new BarEntry(i, Float.parseFloat(String.valueOf(coreMax.get(i)))));
                    }

                    set1 = new BarDataSet(values, "Current Frequency");
                    set1.setColor(Color.parseColor("#fcba03"));

                    set2 = new BarDataSet(values2, "Minimum Frequency");
                    set2.setColor(Color.parseColor("#03fc28"));

                    set3 = new
                            BarDataSet(values3, "Maximum Frequency");
                    set3.setColor(Color.parseColor("#fc1403"));

                    BarData data = new BarData(set1,set2,set3);
                    data.setBarWidth(0.2f);
                    data.groupBars(-0.5f,0.1f,0.1f);
                    data.setDrawValues(false);
                    chart.setData(data);

                    chart.invalidate();

                    String temperature = sp.getString("temperature","1");

                    float max = 200f;

                    if(temperature.equals(String.valueOf(1))){

                        max = 110f;

                        temperatureText.setText("Y - Axis : Temperature (\u2103)\nX - Axis : Time ("+ finalTime1+" seconds)");
                        //temperature chart
                        cpuTemps.add(new Entry(Float.parseFloat(String.valueOf(timeCounter[0])), Float.parseFloat(cpuData[cpuData.length-2])));
                        socTemps.add(new Entry(Float.parseFloat(String.valueOf(timeCounter[0])), Float.parseFloat(cpuData[cpuData.length-1])));
                    }
                    else if(temperature.equals(String.valueOf(0))){
                        max = 200f;
                        temperatureText.setText("Y - Axis : Temperature (\u2109)\nX - Axis : Time ("+ finalTime1+" seconds)");
                        //temperature chart
                        cpuTemps.add(new Entry(Float.parseFloat(String.valueOf(timeCounter[0])), intoFah(Float.parseFloat(cpuData[cpuData.length-2]))));
                        socTemps.add(new Entry(Float.parseFloat(String.valueOf(timeCounter[0])), intoFah(Float.parseFloat(cpuData[cpuData.length-1]))));
                    }

                    timeCounter[0] = timeCounter[0] + finalTime;



                    LineDataSet cpuTemperature = new LineDataSet(cpuTemps, "CPU Temperature");
                    LineDataSet socTemperature = new LineDataSet(socTemps, "SOC Temperature");
                    cpuTemperature.setColor(Color.parseColor("#fc1403"));
                    cpuTemperature.setDrawCircles(false);

                    socTemperature.setColor(Color.parseColor("#03fc28"));
                    socTemperature.setDrawCircles(false);

                    LineData lineData = new LineData(cpuTemperature,socTemperature);


                    XAxis xAxis2 = lineChart.getXAxis();
                    xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis2.setTextSize(10f);
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
                    yAxis22.setTextSize(10f);
                    yAxis22.setAxisMinimum(0);
                    yAxis22.setAxisMaximum(max);
                    yAxis22.setTextColor(Color.parseColor("#2962ff"));


                    lineChart.setData(lineData);
                    lineChart.invalidate();
                    lineChart.getLineData().setDrawValues(false);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }




        return v;
    }

    private float intoFah(float celsius){
        double gg =  (celsius * 1.8) + 32;
        return  Float.parseFloat(String.valueOf(gg));
    }
}