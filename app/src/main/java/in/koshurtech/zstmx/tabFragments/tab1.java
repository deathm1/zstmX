package in.koshurtech.zstmx.tabFragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.card.MaterialCardView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.meterViewAdapter;
import in.koshurtech.zstmx.javaClasses.meterView;
import in.koshurtech.zstmx.viewModels.batteryInfoViewModel;
import in.koshurtech.zstmx.viewModels.cpuInfoViewModel;
import in.koshurtech.zstmx.viewModels.ramInfoViewModel;
import in.koshurtech.zstmx.viewModels.storageInfoViewModel;


public class tab1 extends Fragment {
    ProgressBar batterProgress;
    TextView batteryText;
    TextView batteryStatus;
    RecyclerView recyclerView;
    DonutProgress donutProgress;
    TextView textViewStorageInfo;
    in.koshurtech.zstmx.adapters.meterViewAdapter meterViewAdapter;
    MaterialCardView cpuCard;
    MaterialCardView ramCard;
    MaterialCardView clashCard;
    MaterialCardView storageCard;
    MaterialCardView batteryCard;
    TextView textView;
    TextView ramText;
    ArcProgress arcProgress;
    ArcProgress ramArcProgress;
    LineChart chart;
    ArrayList<meterView> meterViewArrayList = new ArrayList<>();
    LineChart ramChart;
    cpuInfoViewModel cpuInfoViewModel;
    ViewPager2 viewPager2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab1, container, false);
        viewPager2 = (ViewPager2) getActivity().findViewById(R.id.viewPager);
        cpuCard = (MaterialCardView) v.findViewById(R.id.cpuCard);
        ramCard = (MaterialCardView) v.findViewById(R.id.ramCard);
        batteryCard = (MaterialCardView) v.findViewById(R.id.batteryCard);
        storageCard = (MaterialCardView) v.findViewById(R.id.storageCard);
        clashCard = (MaterialCardView) v.findViewById(R.id.clashCard);
        arcProgress = (ArcProgress) v.findViewById(R.id.cpu_progress);
        ramArcProgress = (ArcProgress) v.findViewById(R.id.ram_progress);
        textView = (TextView) v.findViewById(R.id.temps);
        ramText = (TextView) v.findViewById(R.id.ramText);
        batterProgress = (ProgressBar) v.findViewById(R.id.batteryProgress);
        batteryText = (TextView) v.findViewById(R.id.batteryPercentageText);
        batteryStatus = (TextView) v.findViewById(R.id.batteryInfo);

        donutProgress = (DonutProgress) v.findViewById(R.id.donut_progress);
        textViewStorageInfo = (TextView) v.findViewById(R.id.storageInfo);

        recyclerView = (RecyclerView) v.findViewById(R.id.tab1_recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(gridLayoutManager);
        meterViewAdapter = new meterViewAdapter(meterViewArrayList);
        recyclerView.setAdapter(meterViewAdapter);
        chart  = (LineChart) v.findViewById(R.id.cpu_line_chart);
        ramChart  = (LineChart) v.findViewById(R.id.ram_line_chart);

        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setTouchEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getDescription().setEnabled(false);

        ramChart.setDrawingCacheBackgroundColor(Color.parseColor("#2962ff"));

        ramChart.setDragEnabled(false);
        ramChart.setScaleEnabled(false);
        ramChart.setScaleXEnabled(false);
        ramChart.setScaleYEnabled(false);
        ramChart.setTouchEnabled(false);
        ramChart.setPinchZoom(false);
        ramChart.setDoubleTapToZoomEnabled(false);
        ramChart.getDescription().setEnabled(false);

        ramChart.setDrawingCacheBackgroundColor(Color.parseColor("#2962ff"));

        Legend l = chart.getLegend();
        l.setTextColor(Color.parseColor("#2962ff"));
        l.setEnabled(false);

        Legend l2 = ramChart.getLegend();
        l2.setTextColor(Color.parseColor("#2962ff"));
        l2.setEnabled(false);


        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> ramEntry = new ArrayList<Entry>();











        cpuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(4);

            }
        });

        clashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(14);

            }
        });
        ramCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(5);

            }
        });
        storageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(7);

            }
        });


        batteryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(6);

            }
        });






        cpuInfoViewModel = new ViewModelProvider(getActivity()).get(in.koshurtech.zstmx.viewModels.cpuInfoViewModel.class);
        cpuInfoViewModel.setContext(getActivity().getApplicationContext());

        ramInfoViewModel = new ViewModelProvider(getActivity()).get(in.koshurtech.zstmx.viewModels.ramInfoViewModel.class);
        ramInfoViewModel.setContext(getActivity().getApplicationContext());

        storageInfoViewModel = new ViewModelProvider(getActivity()).get(in.koshurtech.zstmx.viewModels.storageInfoViewModel.class);
        storageInfoViewModel.setContext(getActivity().getApplicationContext());


        batteryInfoViewModel = new ViewModelProvider(getActivity()).get(in.koshurtech.zstmx.viewModels.batteryInfoViewModel.class);
        batteryInfoViewModel.setContext(getActivity().getApplicationContext());


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
        final double[] timeCounterRam = {0};

        try {
            LiveData<String> cpuInfoLive = cpuInfoViewModel.getCpuLiveData();

            double finalTime = time;
            cpuInfoLive.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    String temperature = sp.getString("temperature","1");
                    String[] cpuData = s.split(" ");
                    double percentageOverall = (Double.parseDouble(cpuData[1])/Double.parseDouble(cpuData[0]))*100.00;
                    int per = (int) percentageOverall;
                    arcProgress.setProgress(per);
                    entries.add(new Entry(Float.parseFloat(String.valueOf(timeCounter[0])), per));
                    timeCounter[0] = timeCounter[0] + finalTime;
                    LineDataSet dataSet = new LineDataSet(entries, "CPU Usage History");
                    dataSet.setColor(Color.parseColor("#2962ff"));
                    dataSet.setDrawCircles(false);
                    LineData lineData = new LineData(dataSet);
                    chart.setData(lineData);
                    chart.invalidate();
                    chart.getLineData().setDrawValues(false);


                    int coreData = 4;
                    meterViewArrayList.clear();
                    for(int i=0; i<Integer.parseInt(cpuData[3]); i++){
                        double percentagePerCore = (Double.parseDouble(cpuData[coreData+2])/Double.parseDouble(cpuData[coreData]))*100.00;
                        int perCore = (int) percentagePerCore;

                        meterViewArrayList.add(new meterView(perCore,"Core "+i,"Some random text"));

                        coreData = coreData + 3;

                    }
                    meterViewAdapter.notifyDataSetChanged();

                    double soc = Double.parseDouble(cpuData[cpuData.length-1]);
                    double cpu = Double.parseDouble(cpuData[cpuData.length-2]);

                    if(temperature.equals(String.valueOf(1))){
                        textView.setText("CPU : "+ String.format("%.2f",cpu) + " \u2103     " + "SOC : "+String.format("%.2f",soc) + " \u2103");
                    }
                    else if(temperature.equals(String.valueOf(0))){
                        textView.setText("CPU : "+ String.format("%.2f",intoFah(cpu)) + " \u2109     " + "SOC : "+String.format("%.2f",intoFah(soc)) + " \u2109");
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            LiveData<String> ramLiveInfo = ramInfoViewModel.getRamLiveData();
            double finalTime = time;
            double finalTime1 = time;
            ramLiveInfo.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    String[] ramData = s.split(" ");
                    ramArcProgress.setProgress(Integer.parseInt(ramData[0]));
                    timeCounterRam[0] = timeCounterRam[0] + finalTime1;
                    ramEntry.add(new Entry(Float.parseFloat(String.valueOf(timeCounter[0])), Integer.parseInt(ramData[0])));
                    LineDataSet dataSet = new LineDataSet(ramEntry, "RAM Usage History");
                    dataSet.setColor(Color.parseColor("#2962ff"));
                    dataSet.setDrawCircles(false);
                    LineData lineData = new LineData(dataSet);
                    ramChart.setData(lineData);
                    ramChart.invalidate();
                    ramChart.getLineData().setDrawValues(false);


                    String texto = "Total: "+ramData[1] + "\nAvailable: "+ramData[2] + "\nUsed: "+ramData[3];
                    ramText.setText(texto);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            LiveData<String> storageInfoLiveData = storageInfoViewModel.getStorageInfo();

            storageInfoLiveData.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    String[] storgeData= s.split(" ");

                    donutProgress.setProgress(Integer.valueOf(storgeData[0]));
                    String st = "Total: "+storgeData[1] + "\nAvailable: " + storgeData[2] + "\nUsed: " + storgeData[3];
                    textViewStorageInfo.setText(st);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }




        try {
            LiveData<String> batteryInfoLiveData = batteryInfoViewModel.getStorageInfo();

            batteryInfoLiveData.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    String[] storgeData= s.split("//");

//                    System.out.println(Arrays.toString(storgeData));
                    batterProgress.setProgress(Integer.parseInt(storgeData[0]));
                    batteryText.setText(storgeData[0] + " %");
                    batteryStatus.setText(storgeData[1] + "\n" + storgeData[2] + "\n" + storgeData[3]);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }




        //time axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setAxisMinimum(0);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);
        xAxis.setTextColor(Color.parseColor("#2962ff"));
        xAxis.setDrawGridLines(false);





        YAxis yAxis = chart.getAxisRight();
        yAxis.setEnabled(false);
        yAxis.setDrawGridLines(false);


        YAxis yAxis2 = chart.getAxisLeft();
        yAxis2.setDrawGridLines(false);
        yAxis2.setTextSize(10f);
        yAxis2.setAxisMinimum(0);
        yAxis2.setAxisMaximum(100f);
        yAxis2.setLabelCount(2);
        yAxis2.setTextColor(Color.parseColor("#2962ff"));



        //time axis
        XAxis xAxisramChart = ramChart.getXAxis();
        xAxisramChart.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisramChart.setTextSize(10f);
        xAxisramChart.setAxisMinimum(0);
        xAxisramChart.setGranularity(1f);
        xAxisramChart.setLabelCount(5);
        xAxisramChart.setTextColor(Color.parseColor("#2962ff"));
        xAxisramChart.setDrawGridLines(false);





        YAxis yAxisramChart = ramChart.getAxisRight();
        yAxisramChart.setEnabled(false);
        yAxisramChart.setDrawGridLines(false);


        YAxis yAxis2ramChart = ramChart.getAxisLeft();
        yAxis2ramChart.setDrawGridLines(false);
        yAxis2ramChart.setTextSize(10f);
        yAxis2ramChart.setAxisMaximum(100f);
        yAxis2ramChart.setAxisMinimum(0);
        yAxis2ramChart.setLabelCount(2);
        yAxis2ramChart.setTextColor(Color.parseColor("#2962ff"));










        return v;

    }

    ramInfoViewModel ramInfoViewModel;
    storageInfoViewModel storageInfoViewModel;
    batteryInfoViewModel batteryInfoViewModel;



    private double intoFah(double celsius){
        return  (celsius * 1.8) + 32;
    }








}