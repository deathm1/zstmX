package in.koshurtech.zstmx.viewModels;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;


public class storageInfoViewModel extends ViewModel {
    private MutableLiveData<String> storageLiveData;
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

    public MutableLiveData<String> getStorageInfo() throws IOException {
        if(storageLiveData==null){
            storageLiveData = new MutableLiveData<>();
            invoke();
        }
        return storageLiveData;
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
                        storageLiveData.postValue(getStorage());
                        Thread.sleep(finalMills);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        thread.setName("storageThread");
    }


    private String getStorage(){


        double per = ((saveSizeTot-saveSizeavailable)/saveSizeTot)*100.0;
        return (int)per + " " +getTotalInternalMemorySize()+ " " + getAvailableInternalMemorySize() + " "+formatSize(saveSizeTot-saveSizeavailable);
    }


    public  boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
    public  String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        saveSizeavailable = availableBlocks * blockSize;
        return formatSize(availableBlocks * blockSize);
    }
    public  String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        saveSizeTot = totalBlocks * blockSize;
        return formatSize(totalBlocks * blockSize);
    }
    public String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
        } else {
            return "ERROR";
        }
    }
    public String formatSize(double size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {

                if(units==0){
                    suffix = "MB";
                    size /= 1024;
                }

                else if(units==1){
                    suffix = "GB";
                    size /= (1024*1024);
                }
            }
        }
        return String.valueOf(String.format("%.2f",size)+suffix);
    }

    double saveSizeTot;
    double saveSizeavailable;
}
