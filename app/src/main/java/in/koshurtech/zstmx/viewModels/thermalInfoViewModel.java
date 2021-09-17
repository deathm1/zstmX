package in.koshurtech.zstmx.viewModels;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class thermalInfoViewModel extends ViewModel {

    private MutableLiveData<String> thermalLiveData;
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

    public MutableLiveData<String> getThermalInfo() throws IOException {
        if(thermalLiveData==null){
            thermalLiveData = new MutableLiveData<>();
            invoke();
        }
        return thermalLiveData;
    }

    Thread thread;

    public void invoke(){
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(context);
        String stepSize = sp.getString("stepSize","3");





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
                        thermalLiveData.postValue(getThermals());
                        Thread.sleep(finalMills);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        thread.setName("thermalThread");
    }


    private String getThermals(){
        StringBuilder output = new StringBuilder();
        int i=0;
        while(thermalType(i)!=null){
            output.append(thermalType(i)).append(":").append(String.valueOf(thermalTemp(i))).append("--");
            i++;
        }
        return String.valueOf(output);
    }





    public String  thermalTemp(int i) {
        Process process;
        String out = null;
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(context);
        String tempert = sp.getString("temperature","0");
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone"+i+"/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if(line!=null) {
                float temp = Float.parseFloat(line)/1000.0f;
                out = "";
                if(tempert.equals("0")){
                    double fah = toFahrenheit(temp);
                    double roundOff = Math.round((fah*100.0)/100.0);
                    out = roundOff + " \u2109";
                }
                else if(tempert.equals("1")){
                    out = temp + " \u2103";
                }
                return out;
            }else{
                return "0.0f";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }
    public String thermalType(int i) {
        Process process;
        String line = null;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone" + i + "/type");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();

        } catch (Exception e) {
            e.printStackTrace();
            line = "Error: Information denied by Operating System.";
        }

        return line;
    }

    static double toFahrenheit(double t){
        return ((t*9)/5)+32;
    }
}
