package in.koshurtech.zstmx.workers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.Capability;
import android.media.MediaDrm;
import android.os.Build;
import android.util.Log;
import android.util.Range;
import android.util.Rational;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import github.nisrulz.easydeviceinfo.base.BatteryHealth;
import github.nisrulz.easydeviceinfo.base.EasyBatteryMod;
import github.nisrulz.easydeviceinfo.base.EasyBluetoothMod;
import github.nisrulz.easydeviceinfo.base.EasyConfigMod;
import github.nisrulz.easydeviceinfo.base.EasyCpuMod;
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod;
import github.nisrulz.easydeviceinfo.base.EasyDisplayMod;
import github.nisrulz.easydeviceinfo.base.EasyFingerprintMod;
import github.nisrulz.easydeviceinfo.base.EasyMemoryMod;
import github.nisrulz.easydeviceinfo.base.EasySensorMod;
import github.nisrulz.easydeviceinfo.base.EasySimMod;
import github.nisrulz.easydeviceinfo.base.OrientationType;
import in.koshurtech.zstmx.database.entities.*;
import in.koshurtech.zstmx.database.interfaces.*;
import in.koshurtech.zstmx.javaClasses.getDrmInfo;

public class grabDeviceInfoAndSave extends Worker {


    public grabDeviceInfoAndSave(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    public Result doWork() {
        boolean isDown = false;
        try {
            saveInfoIntoDatabase();
            isDown = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Data output = new Data.Builder()
                .putBoolean("Result_db_worker", isDown)
                .build();
        return Result.success(output);
    }
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;
    accessCpuInformation accessCpuInformation;
    accessRamInformation accessRamInformation;
    accessSystemInformation accessSystemInformation;
    accessSoftwareInformation accessSoftwareInformation;
    accessStorageInformation accessStorageInformation;
    accessBatteryInformation accessBatteryInformation;
    accessDisplayInformation accessDisplayInformation;
    accessDrmInformation accessDrmInformation;
    accessSensorsInformation accessSensorsInformation;
    accessCameraInformation accessCameraInformation;
    accessThermalInformation accessThermalInformation;

    EasyCpuMod easyCpuMod;
    EasySimMod easySimMod;
    EasyFingerprintMod easyFingerprintMod;
    EasyDeviceMod easyDeviceMod;
    EasyBluetoothMod easyBluetoothMod;
    EasyConfigMod easyConfigMod;
    EasyMemoryMod easyMemoryMod;
    EasyBatteryMod easyBatteryMod;
    EasyDisplayMod easyDisplayMod;
    EasySensorMod easySensorMod;


    SharedPreferences sp;


    private void saveInfoIntoDatabase() throws IOException {
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


        //Delete existing information
        accessCpuInformation.deleteCpuInformation();
        accessRamInformation.deleteRamInformation();
        accessSystemInformation.deleteSystemInformation();
        accessSoftwareInformation.deleteSoftwareInformation();
        accessStorageInformation.deleteStorageInformation();
        accessBatteryInformation.deleteBatteryInformation();
        accessDisplayInformation.deleteDisplayInformation();
        accessDrmInformation.deleteDrmInformation();
        accessSensorsInformation.deleteSensorInformation();
        accessCameraInformation.deleteCameraInformation();
        accessThermalInformation.deleteThermalInformation();


        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String memoryUnit = sp.getString("memoryUnit", "0");

        //https://github.com/nisrulz/easydeviceinfo
        easyCpuMod = new EasyCpuMod();
        easyBluetoothMod = new EasyBluetoothMod(getApplicationContext());
        easyDeviceMod = new EasyDeviceMod(getApplicationContext());
        easySimMod = new EasySimMod(getApplicationContext());
        easyFingerprintMod = new EasyFingerprintMod(getApplicationContext());
        easyConfigMod = new EasyConfigMod(getApplicationContext());
        easyMemoryMod = new EasyMemoryMod(getApplicationContext());
        easyBatteryMod = new EasyBatteryMod(getApplicationContext());
        easyDisplayMod = new EasyDisplayMod(getApplicationContext());
        easySensorMod = new EasySensorMod(getApplicationContext());


        //CPU Information
        String arch = System.getProperty("os.arch");
        accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"BOARD", Build.BOARD));
        accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"CORES", String.valueOf(getNumberOfCores())));
        accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"SUPPORTED_ABIS", Arrays.toString(Build.SUPPORTED_ABIS)));
        accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"SUPPORTED_32BIT_ABIS", easyCpuMod.getStringSupported32bitABIS()));
        accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"SUPPORTED_64BIT_ABIS", easyCpuMod.getStringSupported64bitABIS()));
        accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"CPU_ARCHITECTURE", arch));
        accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"GOVERNOR", getGovernor()));


        String[] cpuData = ReadCPUinfo().split("\n");

        for (int i = 0; i < cpuData.length; i++) {
            if (!cpuData[i].equals("")) {
                String[] split = cpuData[i].split(":");
                if(split.length>1){
                    accessCpuInformation.insertCpuInformationIntoDatabase(new cpuInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),split[0].trim().toUpperCase(), split[1].trim()));
                }

            }
        }



        //RAM Information
        String[] ramData = getMemoryInfo().split("\n");
        for (int i = 0; i < ramData.length; i++) {
            if (!ramData[i].trim().equals("")) {
                String[] split = ramData[i].split(":");
                String out = "";
                try {
                    int size = Integer.parseInt(extractInt(split[1]).trim());
                    double inMb = size / 1024.0;
                    double inGB = size / (1024.0 * 1024);
                    if (memoryUnit.equals("0")) {
                        out = inMb + " MB";
                    } else if (memoryUnit.equals("1")) {
                        out = inGB + " GB";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out = cpuData[1];
                }
                accessRamInformation.insertRamInformationIntoDatabase(new ramInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),split[0].trim().toUpperCase(), out.trim()));
            }
        }

        //System Information
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Screen Display ID", easyDeviceMod.getScreenDisplayID()));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation("IMSI", easySimMod.getIMSI()));
        //accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation("SIM Serial", easySimMod.getSIMSerial()));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Country", easySimMod.getCountry()));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Carrier", easySimMod.getCarrier()));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Sim Network Locked?", String.valueOf(easySimMod.isSimNetworkLocked())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Active Multi Sim Information", String.valueOf(easySimMod.isSimNetworkLocked())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Multi Sim?", String.valueOf(easySimMod.isMultiSim())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Number Of Active Sim", String.valueOf(easySimMod.getNumberOfActiveSim())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Manufacturer", String.valueOf(easyDeviceMod.getManufacturer())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Model", String.valueOf(easyDeviceMod.getModel())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Radio Version", String.valueOf(easyDeviceMod.getRadioVer())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Product", String.valueOf(easyDeviceMod.getProduct())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Device", String.valueOf(easyDeviceMod.getDevice())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Board", String.valueOf(easyDeviceMod.getBoard())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Hardware", String.valueOf(easyDeviceMod.getHardware())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Bootloader", String.valueOf(easyDeviceMod.getBootloader())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Fingerprint", String.valueOf(easyDeviceMod.getFingerprint())));
        accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"BluetoothLE?", String.valueOf(easyBluetoothMod.hasBluetoothLe())));
        //accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"BluetoothLE Advertising?", String.valueOf(easyBluetoothMod.hasBluetoothLeAdvertising())));

        //accessSystemInformation.insertSystemInformationIntoDatabase(new systemInformation("Phone Number", String.valueOf(easyDeviceMod.getPhoneNo())));








        //Software Information
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"ROOT Status", String.valueOf(easyDeviceMod.isDeviceRooted())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Version Codename", easyDeviceMod.getBuildVersionCodename()));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Version Incremental", easyDeviceMod.getBuildVersionCodename()));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Version SDK", String.valueOf(easyDeviceMod.getBuildVersionSDK())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build ID", String.valueOf(easyDeviceMod.getBuildID())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"OS Codename", String.valueOf(easyDeviceMod.getOSCodename())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"OS Version", String.valueOf(easyDeviceMod.getOSCodename())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"OS Version", String.valueOf(easyDeviceMod.getOSCodename())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Brand", String.valueOf(easyDeviceMod.getBuildBrand())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Host", String.valueOf(easyDeviceMod.getBuildHost())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Tags", String.valueOf(easyDeviceMod.getBuildTags())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Time", String.valueOf(easyDeviceMod.getBuildTime())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build User", String.valueOf(easyDeviceMod.getBuildUser())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Build Version Release", String.valueOf(easyDeviceMod.getBuildVersionRelease())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"isFingerprintSensorPresent?", String.valueOf(easyFingerprintMod.isFingerprintSensorPresent())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"areFingerprintsEnrolled?", String.valueOf(easyFingerprintMod.areFingerprintsEnrolled())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"isRunningOnEmulator?", String.valueOf(easyConfigMod.isRunningOnEmulator())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Time", String.valueOf(easyConfigMod.getTime())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Formatted  Time", String.valueOf(easyConfigMod.getFormattedTime())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Up Time", String.valueOf(easyConfigMod.getUpTime())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Formatted Up Time", String.valueOf(easyConfigMod.getFormattedUpTime())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Current Date", String.valueOf(easyConfigMod.getCurrentDate())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Formatted Date", String.valueOf(easyConfigMod.getFormattedDate())));
        accessSoftwareInformation.insertSoftwareInformationIntoDatabase(new softwareInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"has Sd Card?", String.valueOf(easyConfigMod.hasSdCard())));




        //Storage Information
        accessStorageInformation.insertStorageInformationIntoDatabase(new storageInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Available Internal Memory Size",String.valueOf(memoryUnit.equals("0")?easyMemoryMod.convertToMb(easyMemoryMod.getAvailableInternalMemorySize())+" MB":easyMemoryMod.convertToGb(easyMemoryMod.getAvailableInternalMemorySize())+" GB")));
        accessStorageInformation.insertStorageInformationIntoDatabase(new storageInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Available External Memory Size",String.valueOf(memoryUnit.equals("0")?easyMemoryMod.convertToMb(easyMemoryMod.getAvailableExternalMemorySize())+" MB":easyMemoryMod.convertToGb(easyMemoryMod.getAvailableExternalMemorySize())+" GB")));
        accessStorageInformation.insertStorageInformationIntoDatabase(new storageInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Total Internal Memory Size",String.valueOf(memoryUnit.equals("0")?easyMemoryMod.convertToMb(easyMemoryMod.getTotalInternalMemorySize())+" MB":easyMemoryMod.convertToGb(easyMemoryMod.getTotalInternalMemorySize())+" GB")));
        accessStorageInformation.insertStorageInformationIntoDatabase(new storageInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Total External Memory Size",String.valueOf(memoryUnit.equals("0")?easyMemoryMod.convertToMb(easyMemoryMod.getTotalExternalMemorySize())+" MB":easyMemoryMod.convertToGb(easyMemoryMod.getTotalExternalMemorySize())+" GB")));




        //Battery Information

        accessBatteryInformation.insertBatteryInformationIntoDatabase(new batteryInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Battery Technology",easyBatteryMod.getBatteryTechnology()));
        accessBatteryInformation.insertBatteryInformationIntoDatabase(new batteryInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"isBatteryPresent?",String.valueOf(easyBatteryMod.isBatteryPresent())));
        @BatteryHealth
        int batteryHealth = easyBatteryMod.getBatteryHealth();
        String batHealth = "";
        switch (batteryHealth) {
            case BatteryHealth.GOOD:
                batHealth="Good";
                break;
            case BatteryHealth.HAVING_ISSUES:
                batHealth="Having issues";
                break;
            default:
                batHealth="Having issues";
                break;
        }
        accessBatteryInformation.insertBatteryInformationIntoDatabase(new batteryInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Battery Health",batHealth));
        accessBatteryInformation.insertBatteryInformationIntoDatabase(new batteryInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"isDeviceCharging?",String.valueOf(easyBatteryMod.isDeviceCharging())));
        accessBatteryInformation.insertBatteryInformationIntoDatabase(new batteryInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Battery Voltage",String.valueOf(easyBatteryMod.getBatteryVoltage())+" mV"));
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String temperature = sp.getString("temperature","0");


        float temp = easyBatteryMod.getBatteryTemperature();
        String Btemp = temp + " \u2103";

        if(temperature.equals(String.valueOf(0))){
            double fah = toFahrenheit(temp);
            double roundOff = Math.round((fah*100.0)/100.0);
            Btemp = roundOff + " \u2109";
        }
        accessBatteryInformation.insertBatteryInformationIntoDatabase(new batteryInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Battery Temperature",String.valueOf(Btemp)));





        //Display Information
        accessDisplayInformation.insertDisplayInformationIntoDatabase(new displayInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Resolution",easyDisplayMod.getResolution()));
        accessDisplayInformation.insertDisplayInformationIntoDatabase(new displayInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Refresh Rate",String.valueOf(easyDisplayMod.getRefreshRate())+" hz"));
        accessDisplayInformation.insertDisplayInformationIntoDatabase(new displayInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Physical Size",String.valueOf(easyDisplayMod.getPhysicalSize())));
        accessDisplayInformation.insertDisplayInformationIntoDatabase(new displayInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Density",String.valueOf(easyDisplayMod.getDensity())));

        int orientationType = getApplicationContext().getResources().getConfiguration().orientation;



        String or = "";


        if(orientationType== Configuration.ORIENTATION_LANDSCAPE){
            or = "LANDSCAPE";
        }
        else if(orientationType==Configuration.ORIENTATION_PORTRAIT){
            or = "PORTRAIT";
        }
        else if(orientationType==Configuration.ORIENTATION_UNDEFINED){
            or = "UNDEFINED";
        }

        accessDisplayInformation.insertDisplayInformationIntoDatabase(new displayInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Orientation",or));

        getDrmInfo.invoker(getApplicationContext());



        accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Widevine",""));
        if(getDrmInfo.getWideVineInformation().size()!=0){
            getDrmInfo.getWideVineInformation().entrySet().forEach(entry ->{
                accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),entry.getKey(),entry.getValue()));

            });
        }
        else {
            accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Unavailable",""));
        }

        accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Merlin",""));
        if(getDrmInfo.getMerlin().size()!=0){
            getDrmInfo.getMerlin().entrySet().forEach(entry ->{
                accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),entry.getKey(),entry.getValue()));
            });
        }
        else {
            accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Unavailable",""));
        }


        accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Microsoft Play Ready",""));
        if(getDrmInfo.getMicrosoftPlayReady().size()!=0){
            getDrmInfo.getMicrosoftPlayReady().entrySet().forEach(entry ->{
                accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),entry.getKey(),entry.getValue()));
            });
        }
        else {
            accessDrmInformation.insertDrmInformationIntoDatabase(new drmInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"Unavailable",""));
        }


        List<Sensor> list = easySensorMod.getAllSensors();

        for(int i=0; i<list.size(); i++){
            String head = list.get(i).getName();
            String body =
                    "Vendor : "+list.get(i).getVendor()+"\n"
                    +"--Version : "+list.get(i).getVersion()+"\n"
                    +"--Reporting Mode : "+list.get(i).getReportingMode()+"\n"
                    +"--Power : "+list.get(i).getPower()+"\n"
                    +"--Resolution : "+list.get(i).getResolution()+"\n"
                    +"--Maximum Range : "+list.get(i).getMaximumRange()+"\n"
                    +"--Fifo Max Event Count : "+list.get(i).getFifoMaxEventCount()+"\n"
                    +"--Id : "+list.get(i).getId()+"\n"
                    +"--Max Delay : "+list.get(i).getMaxDelay()+"\n"
                    +"--Min Delay : "+list.get(i).getMinDelay()+"\n"
                    +"--Type : "+list.get(i).getType()+"\n"
                    +"--Fifo Reserved Event Count : "+list.get(i).getFifoReservedEventCount();
            accessSensorsInformation.insertSensorInformationIntoDatabase(new sensorInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),head,body));
        }


        CameraManager manager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameras = manager.getCameraIdList();
            accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),"NaN","CAMERAS",String.valueOf(cameras.length)));

            for (String cameraId : manager.getCameraIdList()) {
                String cameraResolution="";
                CameraCharacteristics chars = manager.getCameraCharacteristics(cameraId);
                Integer facing = chars.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    cameraResolution = "Lens Facing Front";
                } else if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraResolution = "Lens Facing Back";
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Rect res = chars.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

                    String toProcess = String.valueOf(res).replace(")", "");
                    String s2 = toProcess.replace(",", "");
                    String s3 = s2.replace("-", "");
                    String s4 = s3.replace("(", "");
                    String s5 = s4.replace("Rect", "");
                    String[] out = s5.split(" ");
                    int w = Integer.parseInt(out[3].trim());
                    int h = Integer.parseInt(out[4].trim());
                    double megaPixel = (w * h) / 1000000.0;
                    DecimalFormat df = new DecimalFormat("###.###");
                    cameraResolution = +w + " x " + h + " - " + df.format(megaPixel) + " MP ";
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CAMERA RESOLUTION",cameraResolution));
                    float[] fs = chars.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"LENS INFO AVAILABLE APERTURES","f/"+String.valueOf(fs[0])));


                    int[] cc = chars.get(CameraCharacteristics.COLOR_CORRECTION_AVAILABLE_ABERRATION_MODES);
                    String[] ccOut = new String[cc.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            ccOut[x] = "OFF";
                        }
                        else if(x==1){
                            ccOut[x] = "FAST";
                        }
                        else if(x==2){
                            ccOut[x] = "HIGH_QUALITY";
                        }
                    }

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"COLOR CORRECTION AVAILABLE ABERRATION MODES",Arrays.toString(ccOut)));



                    if(facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT){

                        accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"FACING","LENS FACING FRONT"));

                    }
                    else if(facing != null && facing == CameraCharacteristics.LENS_FACING_BACK){

                        accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"FACING","LENS FACING BACK"));

                    }


                    int[] caaam = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
                    String[] caaamOut = new String[caaam.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            caaamOut[x] = "OFF";
                        }
                        else if(x==1){
                            caaamOut[x] = "50HZ";
                        }
                        else if(x==2){
                            caaamOut[x] = "60HZ";
                        }
                        else if(x==3){
                            caaamOut[x] = "AUTO";
                        }
                    }

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AE AVAILABLE ANTIBANDING MODES",Arrays.toString(caaamOut)));




                    int[] caam = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
                    String[] caamOut = new String[caam.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            caamOut[x] = "OFF";
                        }
                        else if(x==1){
                            caamOut[x] = "ON";
                        }
                        else if(x==2){
                            caamOut[x] = "ON_AUTO_FLASH";
                        }
                        else if(x==3){
                            caamOut[x] = "ON_ALWAYS_FLASH";
                        }else if(x==4){
                            caamOut[x] = "ON_AUTO_FLASH_REDEYE";
                        }
                    }

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AE AVAILABLE MODES",Arrays.toString(caamOut)));



                    Range<Integer>[] caatfr = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AE AVAILABLE TARGET FPS RANGES",Arrays.toString(caatfr)));


                    Range<Integer> cacr = chars.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AE COMPENSATION RANGE", String.valueOf(cacr)));


                    Rational cacs = chars.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AE COMPENSATION STEP", String.valueOf(cacs)));



                    Boolean caca = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        caca = chars.get(CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE);
                        accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AE COMPENSATION STEP", String.valueOf(caca)));
                    }

                    else {
                        accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AE COMPENSATION STEP", "Incompatible OS"));
                    }



                    int[] caam2 = chars.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
                    String[] caam2out = new String[caam2.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            caam2out[x] = "OFF";
                        }
                        else if(x==1){
                            caam2out[x] = "AUTO";
                        }
                        else if(x==2){
                            caam2out[x] = "MACRO";
                        }
                        else if(x==3){
                            caam2out[x] = "CONTINUOUS_VIDEO";
                        }else if(x==4){
                            caam2out[x] = "CONTINUOUS_PICTURE";
                        }else if(x==5){
                            caam2out[x] = "EDOF";
                        }
                    }


                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AF AVAILABLE MODES", Arrays.toString(caam2out)));



                    int[] cae = chars.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
                    String[] caeout = new String[cae.length];
                    for(int x=0; x<caeout.length; x++){
                        if(x == 0){
                            caeout[x] = "OFF";
                        }
                        else if(x==1){
                            caeout[x] = "MONO";
                        }
                        else if(x==2){
                            caeout[x] = "NEGATIVE";
                        }
                        else if(x==3){
                            caeout[x] = "SOLARIZE";
                        }else if(x==4){
                            caeout[x] = "SEPIA";
                        }else if(x==5){
                            caeout[x] = "POSTERIZE";
                        }else if(x==6){
                            caeout[x] = "WHITEBOARD";
                        }else if(x==7){
                            caeout[x] = "BLACKBOARD";
                        }else if(x==8){
                            caeout[x] = "AQUA";
                        }
                    }

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AVAILABLE EFFECTS", Arrays.toString(caeout)));


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Capability[] caesmc = chars.get(CameraCharacteristics.CONTROL_AVAILABLE_EXTENDED_SCENE_MODE_CAPABILITIES);

                        System.out.println(
                                Arrays.toString(caesmc));

                        if(caesmc!=null){
                            String[] caesmcout = new String[caesmc.length];
                            for(int x=0; x<caesmcout.length; x++){
                                if(x == 0){
                                    caesmcout[x] = "DISABLED";
                                }
                                else if(x==1){
                                    caesmcout[x] = "BOKEH_STILL_CAPTURE";
                                }
                                else if(x==2){
                                    caesmcout[x] = "BOKEH_CONTINUOUS";
                                }
                            }

                            accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AVAILABLE EXTENDED SCENE MODE CAPABILITIES", Arrays.toString(caesmcout)));

                        }
                        else {
                            accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AVAILABLE EXTENDED SCENE MODE CAPABILITIES", "OS denied information."));

                        }

                    }

                    else{
                        accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AVAILABLE EXTENDED SCENE MODE CAPABILITIES", "Incompatible OS"));

                    }

                    int[] cam = chars.get(CameraCharacteristics.CONTROL_AVAILABLE_MODES);


                    String[] camout = new String[cam.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            camout[x] = "DISABLED";
                        }
                        else if(x==1){
                            camout[x] = "BOKEH_STILL_CAPTURE";
                        }
                        else if(x==2){
                            camout[x] = "BOKEH_CONTINUOUS";
                        }
                    }

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AVAILABLE MODES", Arrays.toString(camout)));




                    int[] casm = chars.get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES);

                    String[] casmout = new String[casm.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            casmout[x] = "DISABLED";
                        }
                        else if(x==1){
                            casmout[x] = "FACE_PRIORITY";
                        }
                        else if(x==2){
                            casmout[x] = "ACTION";
                        }
                        else if(x==3){
                            casmout[x] = "PORTRAIT";
                        }else if(x==4){
                            casmout[x] = "NIGHT";
                        }else if(x==5){
                            casmout[x] = "NIGHT_PORTRAIT";
                        }else if(x==6){
                            casmout[x] = "THEATRE";
                        }else if(x==7){
                            casmout[x] = "BEACH";
                        }else if(x==8){
                            casmout[x] = "SNOW";
                        }else if(x==9){
                            casmout[x] = "SUNSET";
                        }else if(x==10){
                            casmout[x] = "STEADYPHOTO";
                        }else if(x==11){
                            casmout[x] = "FIREWORKS";
                        }else if(x==12){
                            casmout[x] = "SPORTS";
                        }else if(x==13){
                            casmout[x] = "PARTY";
                        }else if(x==14){
                            casmout[x] = "CANDLELIGHT";
                        }else if(x==15){
                            casmout[x] = "BARCODE";
                        }else if(x==16){
                            casmout[x] = "HIGH_SPEED_VIDEO";
                        }else if(x==17){
                            casmout[x] = "HDR";
                        }
                    }

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AVAILABLE SCENE MODES", Arrays.toString(casmout)));



                    int[] cavsm = chars.get(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES);
                    String[] cavsmout = new String[cavsm.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            cavsmout[x] = "OFF";
                        }
                        else if(x==1){
                            cavsmout[x] = "ON";
                        }
                    }

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AVAILABLE VIDEO STABILIZATION MODES", Arrays.toString(cavsmout)));




                    int[] caam3 = chars.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
                    String[] caam3out = new String[caam3.length];
                    for(int x=0; x<cc.length; x++){
                        if(x == 0){
                            caam3out[x] = "OFF";
                        }
                        else if(x==1){
                            caam3out[x] = "AUTO";
                        }
                        else if(x==2){
                            caam3out[x] = "INCANDESCENT";
                        }
                        else if(x==3){
                            caam3out[x] = "FLUORESCENT";
                        }else if(x==4){
                            caam3out[x] = "WARM_FLUORESCENT";
                        }else if(x==5){
                            caam3out[x] = "DAYLIGHT";
                        }else if(x==6){
                            caam3out[x] = "CLOUDY_DAYLIGHT";
                        }else if(x==7){
                            caam3out[x] = "TWILIGHT";
                        }else if(x==8){
                            caam3out[x] = "SHADE";
                        }
                    }


                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AWB AVAILABLE MODES", Arrays.toString(caam3out)));

                    boolean cala = chars.get(CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE);
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL AWB LOCK AVAILABLE", String.valueOf(cala)));

                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL MAX REGIONS AE", String.valueOf(chars.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE))));
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL MAX REGIONS AF", String.valueOf(chars.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF))));
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL MAX REGIONS AWB", String.valueOf(chars.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB))));
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"EDGE AVAILABLE EDGE MODES", Arrays.toString(chars.get(CameraCharacteristics.EDGE_AVAILABLE_EDGE_MODES))));
                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL POST RAW SENSITIVITY BOOST RANGE", String.valueOf(chars.get(CameraCharacteristics.CONTROL_POST_RAW_SENSITIVITY_BOOST_RANGE))));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL ZOOM RATIO RANGE", String.valueOf(chars.get(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE))));
                    }
                    else {
                        accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"CONTROL ZOOM RATIO RANGE", "Incompatible OS"));
                    }




                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"DEPTH DEPTH IS EXCLUSIVE", String.valueOf(chars.get(CameraCharacteristics.DEPTH_DEPTH_IS_EXCLUSIVE))));




                    accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),cameraId,"FLASH INFO AVAILABLE", String.valueOf(chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE))));




                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        //accessCameraInformation.insertCameraInformationIntoDatabase(new cameraInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),,));

        int i=0;
        while(thermalType(i)!=null){
            accessThermalInformation.insertThermalInformationIntoDatabase(new thermalInformation(UUID.randomUUID().toString()+"-"+System.currentTimeMillis(),thermalType(i),thermalTemp(i)));
            i++;
        }


        String uniqueID = UUID.randomUUID().toString()+"-"+System.currentTimeMillis();
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












    getDrmInfo getDrmInfo = new getDrmInfo();
    double toFahrenheit(double t){
        return ((t*9)/5)+32;
    }

    //Reference
    //https://github.com/learnsomuch/HardwareInfo/blob/master/app/src/main/java/com/example/android/hardwareinfo/MainActivity.java

    static String extractInt(String str) {
        str = str.replaceAll("[^\\d]", " ");
        str = str.trim();
        str = str.replaceAll(" +", " ");
        if (str.equals(""))
            return "-1";
        return str;
    }
    public String getMemoryInfo() {
        ProcessBuilder cmd;
        String result="";

        try {
            String[] args = {"/system/bin/cat", "/proc/meminfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1){
                result = result + new String(re);
            }
            in.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private String ReadCPUinfo() {
        ProcessBuilder cmd;
        String result="";

        try{
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1){
                result = result + new String(re);
            }
            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }
    private String getGovernor() {
        StringBuffer sb = new StringBuffer();
        String file = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
        if (new File(file).exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(file)));
                String aLine;
                while ((aLine = br.readLine()) != null)
                    sb.append(aLine + "\n");
                if (br != null)
                    br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public int getNumberOfCores() {
        if(Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }else {
            //Code for old SDK values
            return 0;
            //return Runtime.getRuntime().availableProcessors();
        }
    }

}
