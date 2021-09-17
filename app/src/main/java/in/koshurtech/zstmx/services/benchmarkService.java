package in.koshurtech.zstmx.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import github.nisrulz.easydeviceinfo.base.EasyNetworkMod;
import github.nisrulz.easydeviceinfo.base.NetworkType;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.deviceMonitorResults;
import in.koshurtech.zstmx.activities.deviceMonitor;


public class benchmarkService extends Service {

    String[] chips;
    private static final String TAG = "benchmarkService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Service","Started");


        String notId = intent.getStringExtra("serviceId");
        chips = intent.getStringExtra("chips").split(" ");
        String startTime = intent.getStringExtra("startTime");
        this.startTime = startTime;
        serviceId = notId;

        createNotificationChannel(notId);


        Intent notificationIntent = new Intent(getApplicationContext(), deviceMonitor.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent, 0);





        for(int i=0; i<chips.length; i++){
            if(R.id.battery == Integer.parseInt(chips[i])){
                Log.v("serviceID",String.valueOf(i));
                measured = measured + "Battery"+" ";
                batteryMonitor.start();
            }
            if(R.id.cpu == Integer.parseInt(chips[i])){
                Log.v("serviceID",String.valueOf(i));
                measured = measured + "CPU"+" ";
                cpuMonitor.start();
            }
            if(R.id.network == Integer.parseInt(chips[i])){
                Log.v("serviceID",String.valueOf(i));
                measured = measured + "Network"+" ";
                networkMonitor.start();
            }
            if(R.id.storage == Integer.parseInt(chips[i])){
                Log.v("serviceID",String.valueOf(i));
                measured = measured + "Storage"+" ";
                storageMonitor.start();

            }
            if(R.id.ram == Integer.parseInt(chips[i])){
                Log.v("serviceID",String.valueOf(i));
                measured = measured + "RAM"+" ";
                ramMonitor.start();
            }

            if(R.id.thermal == Integer.parseInt(chips[i])){
                Log.v("serviceID",String.valueOf(i));
                measured = measured + "Thermal"+" ";
                thermalMonitor.start();
            }
        }



        Notification notification = new NotificationCompat.Builder(getApplicationContext(), notId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                .setColor(getColor(R.color.mainCol))
                .setContentTitle("Benchmark Running...")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Monitoring : "+measured + "\nTo stop and generate result tap this notification and select 'Stop Benchmark'."))
                .setContentIntent(pendingIntent)
                .build();


        startForeground(1, notification);


        return START_NOT_STICKY;
    }




    //battery
    Map<Integer,String> batteryResults = new HashMap<>();
    Thread batteryMonitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int i = 0;
                while (true){

                    batteryResults.put(i,getBattery());
                    Thread.sleep(1000);
                    i++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });


    HashMap<Integer,String> cpuResults = new HashMap<>();
    Thread cpuMonitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int i = 0;
                while (true){
                    cpuResults.put(i,createCpuLoad());
                    Thread.sleep(1000);
                    i++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });


    HashMap<Integer,String> networkResults = new HashMap<>();
    Thread networkMonitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int i = 0;
                while (true){
                    networkResults.put(i,getNetwork());
                    Thread.sleep(1000);
                    i++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });

    HashMap<Integer,String> storageResults = new HashMap<>();
    Thread storageMonitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int i = 0;
                while (true){
                    storageResults.put(i,getStorage());
                    Thread.sleep(1000);
                    i++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });


    HashMap<Integer,String> ramResults = new HashMap<>();
    Thread ramMonitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int i = 0;
                while (true){
                    ramResults.put(i,getRAMInfo());
                    Thread.sleep(1000);
                    i++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });


    HashMap<Integer,String> thermalResults = new HashMap<>();
    Thread thermalMonitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int i = 0;
                while (true){
                    thermalResults.put(i,getThermals());
                    Thread.sleep(1000);
                    i++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });

    String startTime = "";
    String serviceId = "";
    String measured = "";

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Service","Stopping benchmark...");
        JSONArray results = new JSONArray();

        for(int i=0; i<chips.length; i++){
            if(R.id.battery == Integer.parseInt(chips[i])){
                batteryMonitor.interrupt();
                JSONArray batResult = new JSONArray();

                try {
                    final int[] j = {0};

                    TreeMap<Integer, String> sorted = new TreeMap<>(batteryResults);

                    sorted.entrySet().forEach(entry -> {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timeStamp",entry.getKey());
                            jsonObject.put("result",entry.getValue());

                            batResult.put(j[0],jsonObject);

                            j[0]++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    results.put(i,batResult);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("Service","Battery monitoring stopped.");
            }
            if(R.id.cpu == Integer.parseInt(chips[i])){
                cpuMonitor.interrupt();
                JSONArray Result = new JSONArray();

                try {
                    final int[] j = {0};
                    cpuResults.entrySet().forEach(entry -> {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timeStamp",entry.getKey());
                            jsonObject.put("result",entry.getValue());
                            Result.put(j[0],jsonObject);
                            j[0]++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    results.put(i,Result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("Service","CPU monitoring stopped.");
            }
            if(R.id.network == Integer.parseInt(chips[i])){
                networkMonitor.interrupt();
                JSONArray Result = new JSONArray();

                try {
                    final int[] j = {0};
                    networkResults.entrySet().forEach(entry -> {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timeStamp",entry.getKey());
                            jsonObject.put("result",entry.getValue());
                            Result.put(j[0],jsonObject);
                            j[0]++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    results.put(i,Result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("Service","Network monitoring stopped.");
            }
            if(R.id.storage == Integer.parseInt(chips[i])){
                storageMonitor.interrupt();
                JSONArray Result = new JSONArray();

                try {
                    final int[] j = {0};
                    storageResults.entrySet().forEach(entry -> {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timeStamp",entry.getKey());
                            jsonObject.put("result",entry.getValue());
                            Result.put(j[0],jsonObject);
                            j[0]++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    results.put(i,Result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("Service","Storage monitoring stopped.");
            }
            if(R.id.ram == Integer.parseInt(chips[i])){
                ramMonitor.interrupt();
                JSONArray Result = new JSONArray();

                try {
                    final int[] j = {0};
                    ramResults.entrySet().forEach(entry -> {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timeStamp",entry.getKey());
                            jsonObject.put("result",entry.getValue());
                            Result.put(j[0],jsonObject);
                            j[0]++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    results.put(i,Result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("Service","RAM monitoring stopped.");
            }
            if(R.id.thermal == Integer.parseInt(chips[i])){
                thermalMonitor.interrupt();
                JSONArray Result = new JSONArray();

                try {
                    final int[] j = {0};
                    thermalResults.entrySet().forEach(entry -> {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("timeStamp",entry.getKey());
                            jsonObject.put("result",entry.getValue());
                            Result.put(j[0],jsonObject);
                            j[0]++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    results.put(i,Result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("Service","Thermal monitoring stopped.");
            }
        }

        JSONObject info = new JSONObject();

        try {
            info.put("serviceID",serviceId);
            info.put("parameters",measured);
            info.put("startTime",startTime);
            info.put("endTime",String.valueOf(System.currentTimeMillis()));
            results.put(7,info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("Service","Generating Results...");
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)) {
            try {
                File root = new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/zstmXBenchmarkReports");
                if (!root.exists()) {
                    root.mkdirs();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
                Date date = new Date();
                String generateUniqueFileName = "benchmark_report_" + formatter.format(date) + ".json";

                File gpxfile = new File(root, generateUniqueFileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(results.toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {
                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                File direc = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                String generateUniqueFileName = "monitor_report_" + formatter.format(date)+".json";
                File root = new File(direc,generateUniqueFileName);
                FileWriter writer = new FileWriter(root);
                writer.append(results.toString());
                writer.flush();
                writer.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }



        Intent intent = new Intent(getApplicationContext(), deviceMonitorResults.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel(String notId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    notId,
                    "Benchmark Service",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    public String getBattery(){
        String[] status = status();
        String out = getBatteryPercentage(getApplicationContext()) + "--" + status[0] + "--" + status[1] + "--" + status[2];
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
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String temperature = sp.getString("temperature","0");

        String[] out = new String[4];
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

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
    public String createCpuLoad() throws IOException {
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


        return out[0] + " " + out[1] + " " + out[2] + " " + coreCount +" "+getCpuTemp();
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
    private String getNetwork(){
        EasyNetworkMod easyNetworkMod = new EasyNetworkMod(getApplicationContext());
        String out = "";
        @NetworkType
        int networkType = easyNetworkMod.getNetworkType();

        String nt = "";
        switch (networkType) {
            case NetworkType.CELLULAR_UNKNOWN:
                nt =  "Unknown";
                break;
            case NetworkType.CELLULAR_UNIDENTIFIED_GEN:
                nt = "Unidentified Generation";
                break;
            case NetworkType.CELLULAR_2G:
                nt = "Cellular 2G";
                break;
            case NetworkType.CELLULAR_3G:
                nt = "Cellular 3G";
                break;
            case NetworkType.CELLULAR_4G:
                nt = "Cellular 4G";
                break;
            case NetworkType.WIFI_WIFIMAX:
                nt = "WiFi/WiFi Max";

                break;
            case NetworkType.UNKNOWN:
                nt = "Unknown";
            default:
                nt = "Unknown";
                break;
        }

        out = "Network Type"+"="+nt + "--"+"Network Availability"+":"+easyNetworkMod.isNetworkAvailable() + "--"
                +"WiFi Enabled"+"="+easyNetworkMod.isWifiEnabled()+"--"
                +"WiFi Link Speed"+"="+easyNetworkMod.getWifiLinkSpeed()+ "--"
                +"IPv6 Address"+"="+easyNetworkMod.getIPv6Address()+ "--"
                +"WiFi SSID"+"="+easyNetworkMod.getWifiSSID()+ "--"
                +"WiFi BSSID"+"="+easyNetworkMod.getWifiBSSID()+ "--"
                +"WiFi MAC"+"="+easyNetworkMod.getWifiMAC();




        return out;
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
    int units;
    double saveSizeTot;
    double saveSizeavailable;
    public String getRAMInfo(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
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
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
}
