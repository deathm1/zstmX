package in.koshurtech.zstmx.workers;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import github.nisrulz.easydeviceinfo.base.EasyAppMod;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.database.entities.*;
import in.koshurtech.zstmx.database.systemInformationDatabase;


public class reportGenerator extends Worker {
    public reportGenerator(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;
    in.koshurtech.zstmx.database.interfaces.accessCpuInformation accessCpuInformation;
    in.koshurtech.zstmx.database.interfaces.accessRamInformation accessRamInformation;
    in.koshurtech.zstmx.database.interfaces.accessSystemInformation accessSystemInformation;
    in.koshurtech.zstmx.database.interfaces.accessSoftwareInformation accessSoftwareInformation;
    in.koshurtech.zstmx.database.interfaces.accessStorageInformation accessStorageInformation;
    in.koshurtech.zstmx.database.interfaces.accessBatteryInformation accessBatteryInformation;
    in.koshurtech.zstmx.database.interfaces.accessDisplayInformation accessDisplayInformation;
    in.koshurtech.zstmx.database.interfaces.accessDrmInformation accessDrmInformation;
    in.koshurtech.zstmx.database.interfaces.accessSensorsInformation accessSensorsInformation;
    in.koshurtech.zstmx.database.interfaces.accessCameraInformation accessCameraInformation;
    in.koshurtech.zstmx.database.interfaces.accessThermalInformation accessThermalInformation;
    in.koshurtech.zstmx.database.interfaces.accessNetworkInformation accessNetworkInformation;
    @NonNull
    @Override
    public Result doWork() {
        try {
            systemInformationDatabase = in.koshurtech.zstmx.database.systemInformationDatabase.getInstance(getApplicationContext());
            accessCpuInformation = systemInformationDatabase.accessCPUInformation();
            accessRamInformation = systemInformationDatabase.accessRamInformation();
            accessSystemInformation = systemInformationDatabase.accessSystemInformation();
            accessSoftwareInformation = systemInformationDatabase.accessSoftwareInformation();
            accessStorageInformation = systemInformationDatabase.accessStorageInformation();
            accessBatteryInformation = systemInformationDatabase.accessBatteryInformation();
            accessDisplayInformation = systemInformationDatabase.accessDisplayInformation();
            accessDrmInformation = systemInformationDatabase.accessDrmInformation();
            accessSensorsInformation = systemInformationDatabase.accessSensorsInformation();
            accessCameraInformation = systemInformationDatabase.accessCameraInformation();
            accessThermalInformation = systemInformationDatabase.accessThermalInformation();
            accessNetworkInformation = systemInformationDatabase.accessNetworkInformation();
            int[] getSelection = getInputData().getIntArray("configuration");
            StringBuilder veryBigString = new StringBuilder();
            EasyAppMod easyAppMod = new EasyAppMod(getApplicationContext());
            String appInfo =
                    "\n-Report Start-\nApp Name : "+easyAppMod.getAppName()+"\n"
                            +"App Version : "+easyAppMod.getAppVersion()+"\n"
                            +"Activity Name : "+easyAppMod.getActivityName()+"\n"
                            +"App Version Code : "+easyAppMod.getAppVersionCode()+"\n"
                            +"Store : "+easyAppMod.getStore()+"\n"
                            +"Package Name : "+easyAppMod.getPackageName();
            veryBigString.append(appInfo).append("\n\n");
            veryBigString.append("Certain symbols in the report may not be in readable format, please refer SI units for notations.").append("\n\n");
            for(int i=0; i<getSelection.length; i++){
                if(R.id.accessBatteryInformation == getSelection[i]){
                    //BatteryInformation
                    veryBigString.append("Battery Information").append("\n\n");
                    List<batteryInformation> batteryInformation = accessBatteryInformation.getBatteryInformation();
                    for(int j=0; j<batteryInformation.size(); j++){
                        String head = "("+batteryInformation.get(j).id + ")\n" +batteryInformation.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(batteryInformation.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessCameraInformation == getSelection[i]){
                    veryBigString.append("Camera Information").append("\n\n");
                    List<cameraInformation> cameraInformationAL = accessCameraInformation.getCameraInformation();
                    for(int j=0; j<Integer.parseInt(cameraInformationAL.get(0).propertyBody); j++){
                        veryBigString.append("-Camera ").append(j).append("-").append("\n");
                        for (cameraInformation c: cameraInformationAL
                             ) {
                            String head = "("+c.id + ")\n" +c.propertyName;
                            veryBigString.append(head).append(" : ").append(c.propertyBody).append("\n\n");
                        }
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessCpuInformation == getSelection[i]){
                    veryBigString.append("CPU Information").append("\n\n");
                    List<cpuInformation> cpuInformationList = accessCpuInformation.getCpuInformation();
                    for(int j=0; j<cpuInformationList.size(); j++){
                        String head = "("+cpuInformationList.get(j).id + ")\n" +cpuInformationList.get(j).cpuProperty;
                        veryBigString.append(head).append(" : ").append(cpuInformationList.get(j).propertyData).append("\n\n");

                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessDisplayInformation == getSelection[i]){
                    veryBigString.append("Display Information").append("\n\n");
                    List<displayInformation> displayInformationList = accessDisplayInformation.getDisplayInformation();
                    for(int j=0; j<displayInformationList.size(); j++){
                        String head = "("+displayInformationList.get(j).id + ")\n" +displayInformationList.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(displayInformationList.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessDrmInformation == getSelection[i]){
                    veryBigString.append("DRM Information").append("\n\n");
                    List<drmInformation> drmInformationList = accessDrmInformation.getDrmInformation();
                    for(int j=0; j<drmInformationList.size(); j++){
                        String head = "("+drmInformationList.get(j).id + ")\n" +drmInformationList.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(drmInformationList.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessNetworkInformation == getSelection[i]){
                    veryBigString.append("Network Information").append("\n\n");
                    List<networkInformation> networkInformationList = accessNetworkInformation.getNetworkInformation();
                    for(int j=0; j<networkInformationList.size(); j++){
                        String head = "("+networkInformationList.get(j).id + ")\n" +networkInformationList.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(networkInformationList.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessRamInformation == getSelection[i]){
                    veryBigString.append("Network Information").append("\n\n");
                    List<ramInformation> ramInformationList = accessRamInformation.getRamInformation();
                    for(int j=0; j<ramInformationList.size(); j++){
                        String head = "("+ramInformationList.get(j).id + ")\n" +ramInformationList.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(ramInformationList.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessSensorInformation == getSelection[i]){
                    veryBigString.append("Sensor Information").append("\n\n");
                    List<sensorInformation> sensorInformationList = accessSensorsInformation.getSensorInformation();
                    for(int j=0; j<sensorInformationList.size(); j++){
                        String head = "("+sensorInformationList.get(j).id + ")\n" +sensorInformationList.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(sensorInformationList.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessSoftwareInformation == getSelection[i]){
                    veryBigString.append("Software Information").append("\n\n");
                    List<softwareInformation> softwareInformationList = accessSoftwareInformation.getSoftwareInformation();
                    for(int j=0; j<softwareInformationList.size(); j++){
                        String head = "("+softwareInformationList.get(j).id + ")\n" +softwareInformationList.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(softwareInformationList.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
                if(R.id.accessThermalInformation == getSelection[i]){
                    veryBigString.append("Thermal Information").append("\n\n");
                    List<thermalInformation> thermalInformationList = accessThermalInformation.getThermalInformation();
                    for(int j=0; j<thermalInformationList.size(); j++){
                        String head = "("+thermalInformationList.get(j).id + ")\n" +thermalInformationList.get(j).propertyName;
                        veryBigString.append(head).append(" : ").append(thermalInformationList.get(j).propertyBody).append("\n\n");
                    }
                    veryBigString.append("-end-").append("\n\n");
                }
            }
            veryBigString.append("-End of Report-").append("\n\n");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                File direc = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();

                String generateUniqueFileName = "system_report_" + formatter.format(date)+".txt";

                File gpxfile = new File(direc, generateUniqueFileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(veryBigString);
                writer.flush();
                writer.close();
                myData = new Data.Builder()
                        .putString("output", generateUniqueFileName)
                        .build();
            }
            else{
                File root = new File(String.valueOf(Environment.getExternalStorageDirectory())+"/zstmX Reports");
                if (!root.exists()) {
                    root.mkdirs();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date();
                String generateUniqueFileName = "system_report_" + formatter.format(date)+".txt";
                File gpxfile = new File(root, generateUniqueFileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(veryBigString);
                writer.flush();
                writer.close();
                myData = new Data.Builder()
                        .putString("output", generateUniqueFileName)
                        .build();
            }




            return Result.success(myData);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Result.failure();
        }
    }


    Data myData;
}
