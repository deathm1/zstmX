package in.koshurtech.zstmx.viewModels;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.io.IOException;

import static android.content.Context.ACTIVITY_SERVICE;

public class ramInfoViewModel extends ViewModel {

    private MutableLiveData<String> ramLiveData;
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }


    public MutableLiveData<String> getRamLiveData() throws IOException {
        if(ramLiveData==null){
            ramLiveData = new MutableLiveData<>();
            invoke();
        }
        return ramLiveData;
    }

    int units  = 0;

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
                        ramLiveData.postValue(getRAMInfo());
                        Thread.sleep(finalMills);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        thread.setName("ramThread");
    }


    public String getRAMInfo(){


        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        double totalMegs = mi.totalMem / 0x100000L;
        double usedMegs = totalMegs-availableMegs;


        int percentage = (int)(usedMegs/totalMegs*100);


        if(units == 1){
            //in GB

            double aminGB = availableMegs/1024.0;
            double tminGB = totalMegs/1024.0;
            double uminGB = usedMegs/1024.0;
            return percentage + " " + String.format("%.2f",tminGB) + "GB " + String.format("%.2f",aminGB) + "GB " + String.format("%.2f",uminGB) +"GB";

        }


        //MB
        return percentage + " " + String.format("%.2f",totalMegs) + "MB " + String.format("%.2f",availableMegs) + "MB " + String.format("%.2f",usedMegs)+"MB";
    }

    Thread thread;

}
