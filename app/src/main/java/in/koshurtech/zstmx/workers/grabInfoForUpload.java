package in.koshurtech.zstmx.workers;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import github.nisrulz.easydeviceinfo.base.EasyAppMod;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.database.entities.*;
import in.koshurtech.zstmx.database.systemInformationDatabase;


public class grabInfoForUpload extends Worker {
    public grabInfoForUpload(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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


            JSONArray jsonArray = new JSONArray();










            for(int i=0; i<getSelection.length; i++){
                if(R.id.accessBatteryInformation == getSelection[i]){
                    //BatteryInformation

                    JSONObject data = new JSONObject();

                    data.put("prop","Battery Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<batteryInformation> batteryInformation = accessBatteryInformation.getBatteryInformation();
                    for(int j=0; j<batteryInformation.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = batteryInformation.get(j).propertyName;
                        prop.put(head,batteryInformation.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessCameraInformation == getSelection[i]){


                    JSONObject data = new JSONObject();
                    data.put("prop","Camera Information(0,1,2..n)");

                    JSONArray currJSONARRAY = new JSONArray();

                    List<cameraInformation> cameraInformationAL = accessCameraInformation.getCameraInformation();
                    for(int j=0; j<Integer.parseInt(cameraInformationAL.get(0).propertyBody); j++){

                        for (cameraInformation c: cameraInformationAL
                        ) {

                            //make json object
                            JSONObject prop = new JSONObject();
                            String head = c.propertyName;
                            prop.put(head,c.propertyBody);

                            //put object in array
                            currJSONARRAY.put(prop);
                        }
                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessCpuInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","CPU Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<cpuInformation> cpuInformation = accessCpuInformation.getCpuInformation();
                    for(int j=0; j<cpuInformation.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = cpuInformation.get(j).cpuProperty;
                        prop.put(head,cpuInformation.get(j).propertyData);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessDisplayInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","Display Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<displayInformation> displayInformation = accessDisplayInformation.getDisplayInformation();
                    for(int j=0; j<displayInformation.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = displayInformation.get(j).propertyName;
                        prop.put(head,displayInformation.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessDrmInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","DRM Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<drmInformation> drmInformation = accessDrmInformation.getDrmInformation();
                    for(int j=0; j<drmInformation.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = drmInformation.get(j).propertyName;
                        prop.put(head,drmInformation.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessNetworkInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","Network Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<networkInformation> networkInformation = accessNetworkInformation.getNetworkInformation();
                    for(int j=0; j<networkInformation.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = networkInformation.get(j).propertyName;
                        prop.put(head,networkInformation.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessRamInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","RAM Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<ramInformation> ramInformation = accessRamInformation.getRamInformation();
                    for(int j=0; j<ramInformation.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = ramInformation.get(j).propertyName;
                        prop.put(head,ramInformation.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessSensorInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","Sensor Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<sensorInformation> sensorInformations = accessSensorsInformation.getSensorInformation();
                    for(int j=0; j<sensorInformations.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = sensorInformations.get(j).propertyName;
                        prop.put(head,sensorInformations.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessSoftwareInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","Software Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<softwareInformation> softwareInformation = accessSoftwareInformation.getSoftwareInformation();
                    for(int j=0; j<softwareInformation.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = softwareInformation.get(j).propertyName;
                        prop.put(head,softwareInformation.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
                if(R.id.accessThermalInformation == getSelection[i]){
                    JSONObject data = new JSONObject();

                    data.put("prop","Thermal Information");



                    JSONArray currJSONARRAY = new JSONArray();
                    List<thermalInformation> thermalInformations = accessThermalInformation.getThermalInformation();
                    for(int j=0; j<thermalInformations.size(); j++){

                        //make json object
                        JSONObject prop = new JSONObject();
                        String head = thermalInformations.get(j).propertyName;
                        prop.put(head,thermalInformations.get(j).propertyBody);

                        //put object in array
                        currJSONARRAY.put(prop);


                    }
                    data.put("data",currJSONARRAY);
                    jsonArray.put(data);
                }
            }





            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                    File direc = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date date = new Date();

                    String generateUniqueFileName = "system_upload_" + formatter.format(date)+".txt";

                    File gpxfile = new File(direc, generateUniqueFileName);
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(jsonArray.toString());
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
                    String generateUniqueFileName = "system_upload_" + formatter.format(date)+".txt";
                    File gpxfile = new File(root, generateUniqueFileName);
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(jsonArray.toString());
                    writer.flush();
                    writer.close();
                    myData = new Data.Builder()
                            .putString("output", generateUniqueFileName)
                            .build();

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


            return Result.success(myData);



        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Result.failure();
        }
    }





    Data myData;
}

