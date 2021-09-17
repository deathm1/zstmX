package in.koshurtech.zstmx.viewModels;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.io.IOException;

import static android.content.Context.BATTERY_SERVICE;

public class batteryInfoViewModel extends ViewModel {
    private MutableLiveData<String> batteryLiveData;
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

    public MutableLiveData<String> getStorageInfo() throws IOException {
        if(batteryLiveData==null){
            batteryLiveData = new MutableLiveData<>();
            invoke();
        }
        return batteryLiveData;
    }

    Thread thread;
    int units;

    public void invoke(){
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(context);
        String memoryUnit = sp.getString("memoryUnit","0");
        String stepSize = sp.getString("stepSize","3");


        if(memoryUnit.equals(String.valueOf(0))){
            units = 0;
        }
        else if(memoryUnit.equals(String.valueOf(1))){
            units = 1;
        }


        int mills = 0;

        if(stepSize.equals(String.valueOf(0))){
            mills = 250;
        }
        else if(stepSize.equals(String.valueOf(1))){
            mills = 500;

        }
        else if(stepSize.equals(String.valueOf(2))){
            mills = 750;

        }
        else if(stepSize.equals(String.valueOf(3))){
            mills = 1000;

        }

        int finalMills = mills;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        batteryLiveData.postValue(getBattery());
                        Thread.sleep(finalMills);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        thread.setName("batteryThread");
    }


    public String getBattery(){
        String[] status = status();
        String out = getBatteryPercentage(context) + "//" + status[0] + "//" + status[1] + "//" + status[2];
        return out;
    }


    public static int getBatteryPercentage(Context context) {

        if (Build.VERSION.SDK_INT > 23) {

            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        } else {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            double batteryPct = level / (double) scale;

            return (int) (batteryPct * 100);
        }
    }


    private String[] status(){
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(context);
        String temperature = sp.getString("temperature","0");

        String[] out = new String[4];
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int scale = -1;
        int level = -1;
        int voltage = -1;
        int temp = -1;

        level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

        if(isCharging){
            if(usbCharge){
                out[0] = "Status : Charging via USB";
            }
            else if(acCharge){
                out[0] = "Status : AC Charging";
            }
        }
        else {
            out[0] = "Status : Discharging";
        }

        out[1] = "Voltage : "+voltage+" mV";

        if(temperature.equals(String.valueOf(0))){
            double fah = toFahrenheit(celsius(temp));
            double roundOff = Math.round((fah*100.0)/100.0);
            out[2] = "Temperature : " + roundOff + " \u2109";
        }
        else if(temperature.equals(String.valueOf(1))){
            double cel = celsius(temp);
            double roundOff = Math.round((cel*100.0)/100.0);
            out[2] = "Temperature : " + roundOff + " \u2103";
        }

        out[3] = "Level : " + level +" %";

        return out;
    }


    double celsius(double t) {
        return  t/10;
    }
    double toFahrenheit(double t){
        return ((t*9)/5)+32;
    }
}
