package in.koshurtech.zstmx.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.*;
import in.koshurtech.zstmx.database.entities.*;
import in.koshurtech.zstmx.database.interfaces.*;
import in.koshurtech.zstmx.database.*;
import in.koshurtech.zstmx.tabFragments.*;

public class tabAdapter extends FragmentStateAdapter {


    Context context;

    public tabAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, Context context) {
        super(fragmentManager, lifecycle);
        this.context = context;
        databaseQuery.start();
    }





    List<systemInformation> systemInformationArrayList = new ArrayList<>();
    List<cpuInformation> cpuInformationArrayList = new ArrayList<>();
    List<ramInformation> ramInformationArrayList = new ArrayList<>();
    List<storageInformation> storageInformationArrayList = new ArrayList<>();
    List<softwareInformation> softwareInformationArrayList = new ArrayList<>();
    List<batteryInformation> batteryInformationArrayList = new ArrayList<>();
    List<drmInformation> drmInformationArrayList = new ArrayList<>();
    List<displayInformation> displayInformationArrayList = new ArrayList<>();
    List<sensorInformation> sensorInformationArrayList = new ArrayList<>();
    List<cameraInformation> cameraInformationArrayList = new ArrayList<>();


    Thread databaseQuery = new Thread(new Runnable() {
        @Override
        public void run() {
            systemInformationDatabase systemInformationDatabase = in.koshurtech.zstmx.database.systemInformationDatabase.getInstance(context);
            accessSoftwareInformation accessSoftwareInformation = systemInformationDatabase.accessSoftwareInformation();
            accessSystemInformation accessSystemInformation = systemInformationDatabase.accessSystemInformation();
            accessStorageInformation accessStorageInformation = systemInformationDatabase.accessStorageInformation();
            accessRamInformation accessRamInformation = systemInformationDatabase.accessRamInformation();
            accessCpuInformation accessCpuInformation = systemInformationDatabase.accessCPUInformation();
            accessBatteryInformation accessBatteryInformation = systemInformationDatabase.accessBatteryInformation();
            accessDrmInformation accessDrmInformation = systemInformationDatabase.accessDrmInformation();
            accessDisplayInformation accessDisplayInformation = systemInformationDatabase.accessDisplayInformation();
            accessSensorsInformation accessSensorsInformation = systemInformationDatabase.accessSensorsInformation();
            accessCameraInformation accessCameraInformation = systemInformationDatabase.accessCameraInformation();

            systemInformationArrayList = accessSystemInformation.getSystemInformation();
            cpuInformationArrayList = accessCpuInformation.getCpuInformation();
            ramInformationArrayList = accessRamInformation.getRamInformation();
            storageInformationArrayList = accessStorageInformation.getStorageInformation();
            softwareInformationArrayList = accessSoftwareInformation.getSoftwareInformation();
            batteryInformationArrayList = accessBatteryInformation.getBatteryInformation();
            drmInformationArrayList = accessDrmInformation.getDrmInformation();
            displayInformationArrayList = accessDisplayInformation.getDisplayInformation();
            sensorInformationArrayList = accessSensorsInformation.getSensorInformation();
            cameraInformationArrayList = accessCameraInformation.getCameraInformation();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    @NonNull

    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 1:
                return new tab2(systemInformationArrayList);
            case 2:
                return new tab3(softwareInformationArrayList);
            case 3:
                return new tab11(drmInformationArrayList);
            case 4:
                return new tab4();
            case 5:
                return new tab5(ramInformationArrayList);
            case 6:
                return new tab6(batteryInformationArrayList);
            case 7:
                return new tab7(storageInformationArrayList);
            case 8:
                return new tab8();
            case 9:
                return new tab9(displayInformationArrayList);
            case 10:
                return new tab10(sensorInformationArrayList);
            case 11:
                return new tab12(cameraInformationArrayList);
            case 12:
                return new tab13();
            case 13:
                return new tab15();
            case 14:
                return new tab14();
        }
        return new tab1();
    }

    @Override
    public int getItemCount() {
        return 15;
    }
}
