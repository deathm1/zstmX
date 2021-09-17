package in.koshurtech.zstmx.viewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import in.koshurtech.zstmx.database.entities.*;
import in.koshurtech.zstmx.database.interfaces.*;
import in.koshurtech.zstmx.database.systemInformationDatabase;

public class isAllDone extends ViewModel {
    private String TAG = this.getClass().getSimpleName();
    private MutableLiveData<List<softwareInformation>> software;
    private MutableLiveData<List<cpuInformation>> cpu;
    private MutableLiveData<List<ramInformation>> ram;
    private MutableLiveData<List<storageInformation>> storage;
    private MutableLiveData<List<systemInformation>> system;
    private MutableLiveData<List<batteryInformation>> battery;
    private MutableLiveData<List<cameraInformation>> camera;
    private MutableLiveData<List<displayInformation>> display;
    private MutableLiveData<List<drmInformation>> drm;
    private MutableLiveData<List<networkInformation>> network;
    private MutableLiveData<List<sensorInformation>> sensor;
    private MutableLiveData<List<thermalInformation>> thermal;

    private Context context;


    public void setContext(Context context){
        this.context = context;
    }


    Thread thread;

    public  void  invoker(){
        invoke();
    }

    public MutableLiveData<List<softwareInformation>> getSoftware() {
        return software;
    }
    public MutableLiveData<List<cpuInformation>> getCpu() {
        return cpu;
    }
    public MutableLiveData<List<ramInformation>> getRam() {
        return ram;
    }
    public MutableLiveData<List<storageInformation>> getStorage() {
        return storage;
    }
    public MutableLiveData<List<systemInformation>> getSystem() {
        return system;
    }
    public MutableLiveData<List<batteryInformation>> getBattery() {
        return battery;
    }
    public MutableLiveData<List<cameraInformation>> getCamera() {
        return camera;
    }
    public MutableLiveData<List<displayInformation>> getDisplay() {
        return display;
    }
    public MutableLiveData<List<drmInformation>> getDrm() {
        return drm;
    }
    public MutableLiveData<List<networkInformation>> getNetwork() {
        return network;
    }
    public MutableLiveData<List<sensorInformation>> getSensor() {
        return sensor;
    }
    public MutableLiveData<List<thermalInformation>> getThermal() {
        return thermal;
    }

    public void invoke(){
        software = new MutableLiveData<>();
        cpu = new MutableLiveData<>();
        ram = new MutableLiveData<>();
        storage = new MutableLiveData<>();
        system = new MutableLiveData<>();
        camera = new MutableLiveData<>();
        battery = new MutableLiveData<>();
        display = new MutableLiveData<>();
        drm = new MutableLiveData<>();
        network = new MutableLiveData<>();
        sensor = new MutableLiveData<>();
        thermal = new MutableLiveData<>();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    systemInformationDatabase systemInformationDatabase = in.koshurtech.zstmx.database.systemInformationDatabase.getInstance(context);
                    accessSoftwareInformation accessSoftwareInformation = systemInformationDatabase.accessSoftwareInformation();
                    accessSystemInformation accessSystemInformation = systemInformationDatabase.accessSystemInformation();
                    accessStorageInformation accessStorageInformation = systemInformationDatabase.accessStorageInformation();
                    accessRamInformation accessRamInformation = systemInformationDatabase.accessRamInformation();
                    accessCpuInformation accessCpuInformation = systemInformationDatabase.accessCPUInformation();
                    accessCameraInformation accessCameraInformation = systemInformationDatabase.accessCameraInformation();
                    accessBatteryInformation accessBatteryInformation = systemInformationDatabase.accessBatteryInformation();
                    accessDisplayInformation accessDisplayInformation = systemInformationDatabase.accessDisplayInformation();
                    accessDrmInformation accessDrmInformation = systemInformationDatabase.accessDrmInformation();
                    accessNetworkInformation accessNetworkInformation = systemInformationDatabase.accessNetworkInformation();
                    accessSensorsInformation accessSensorsInformation = systemInformationDatabase.accessSensorsInformation();
                    accessThermalInformation accessThermalInformation = systemInformationDatabase.accessThermalInformation();

                    software.postValue(accessSoftwareInformation.getSoftwareInformation());
                    cpu.postValue(accessCpuInformation.getCpuInformation());
                    ram.postValue(accessRamInformation.getRamInformation());
                    storage.postValue(accessStorageInformation.getStorageInformation());
                    system.postValue(accessSystemInformation.getSystemInformation());
                    camera.postValue(accessCameraInformation.getCameraInformation());
                    battery.postValue(accessBatteryInformation.getBatteryInformation());
                    display.postValue(accessDisplayInformation.getDisplayInformation());
                    drm.postValue(accessDrmInformation.getDrmInformation());
                    network.postValue(accessNetworkInformation.getNetworkInformation());
                    sensor.postValue(accessSensorsInformation.getSensorInformation());
                    thermal.postValue(accessThermalInformation.getThermalInformation());

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.setName("isAllDone");
    }
}
