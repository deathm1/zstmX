package in.koshurtech.zstmx.viewModels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

import in.koshurtech.zstmx.MainActivity;

public class cpuInfoViewModel extends ViewModel {
    private String TAG = this.getClass().getSimpleName();
    private MutableLiveData<String> cpuLiveData;

    private Context context;


    public void setContext(Context context){
        this.context = context;
    }



    public MutableLiveData<String> getCpuLiveData() throws IOException {
        if(cpuLiveData==null){
            cpuLiveData = new MutableLiveData<>();
            invoke();
        }
        return cpuLiveData;
    }


    public String getCpuTemp() {
        Process p;
        Process p2;
        try {
            p = Runtime.getRuntime().exec("cat /sys/devices/virtual/thermal/thermal_zone1/temp");
            p2 = Runtime.getRuntime().exec("cat /sys/devices/virtual/thermal/thermal_zone0/temp");
            p.waitFor();
            p2.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));

            String line = reader.readLine();
            String line2 = reader2.readLine();
            float temp = Float.parseFloat(line) / 1000.0f;
            float temp2 = Float.parseFloat(line2) / 1000.0f;

            return temp + " " +temp2;

        } catch (Exception e) {
            e.printStackTrace();
            return "0.0";
        }
    }


    Thread thread;
    public void invoke() {

        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(context);
        String frequency = sp.getString("stepSize","3");

        int mills = 0;

        if(frequency.equals(String.valueOf(0))){
            mills = 250;
        }
        else if(frequency.equals(String.valueOf(1))){
            mills = 500;

        }
        else if(frequency.equals(String.valueOf(2))){
            mills = 750;

        }
        else if(frequency.equals(String.valueOf(3))){
            mills = 1000;

        }

        int finalMills = mills;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        createCpuLoad();
                        Thread.sleep(finalMills);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        thread.setName("cpuThread");
    }

    public void createCpuLoad() throws IOException {
        int coreCount = calcCpuCoreCount();
        double[] out = new double[3];
        StringBuilder perCoreMaxMin = new StringBuilder();


        double cpuMaxFreq = 0.0;
        double cpuMinFreq = 0.0;
        double cpuCurrFreq = 0.0;

        double maxfa = 0.0;
        double currfa = 0.0;
        double unusedfa = 0.0;

        for(int i=0; i<coreCount; i++){
            double save1 = 0.0;
            double save2 = 0.0;
            double save3 = 0.0;

            RandomAccessFile maxFreqr = new RandomAccessFile("/sys/devices/system/cpu/cpu"+i+"/cpufreq/cpuinfo_max_freq", "r");
            save1 = Double.parseDouble(maxFreqr.readLine());
            maxfa = maxfa + save1;


            RandomAccessFile unFreqr = new RandomAccessFile("/sys/devices/system/cpu/cpu"+i+"/cpufreq/cpuinfo_min_freq", "r");
            save2 = Double.parseDouble(unFreqr.readLine());
            unusedfa = unusedfa + save2;

            RandomAccessFile currFreqr = new RandomAccessFile("/sys/devices/system/cpu/cpu"+i+"/cpufreq/scaling_cur_freq", "r");
            save3 = Double.parseDouble(currFreqr.readLine());
            currfa = currfa + save3;


            String coreWise = save1 + " " + save2 + " " + save3+" ";
            perCoreMaxMin.append(coreWise);
        }

        cpuMaxFreq = maxfa/coreCount;
        cpuMinFreq = unusedfa/coreCount;
        cpuCurrFreq = currfa/coreCount;

        out[0] = cpuMaxFreq;
        out[1] = cpuCurrFreq;
        out[2] = cpuMinFreq;


        cpuLiveData.postValue(out[0] + " " + out[1] + " " + out[2] + " " + coreCount + " " + perCoreMaxMin + "" + getCpuTemp());
    }
    private static int sLastCpuCoreCount = -1;



    public int calcCpuCoreCount() {

        if (sLastCpuCoreCount >= 1) {
            return sLastCpuCoreCount;
        }

        try {
            // Get directory containing CPU info
            final File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            final File[] files = dir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    //Check if filename is "cpu", followed by a single digit number
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            // Return the number of cores (virtual CPU devices)
            sLastCpuCoreCount = files.length;

        } catch(Exception e) {
            sLastCpuCoreCount = Runtime.getRuntime().availableProcessors();
        }

        return sLastCpuCoreCount;
    }
}
