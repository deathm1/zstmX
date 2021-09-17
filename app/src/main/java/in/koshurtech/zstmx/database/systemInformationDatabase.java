package in.koshurtech.zstmx.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import in.koshurtech.zstmx.database.entities.*;
import in.koshurtech.zstmx.database.interfaces.*;

@Database(entities = {
        cpuInformation.class,
        systemInformation.class,
        ramInformation.class,
        softwareInformation.class,
        batteryInformation.class,
        cameraInformation.class,
        sensorInformation.class,
        networkInformation.class,
        drmInformation.class,
        displayInformation.class,
        thermalInformation.class,
        storageInformation.class},
        version = 23)
public abstract class systemInformationDatabase extends RoomDatabase {
    public abstract accessCpuInformation accessCPUInformation();
    public abstract accessRamInformation accessRamInformation();
    public abstract accessSoftwareInformation accessSoftwareInformation();
    public abstract accessSystemInformation accessSystemInformation();
    public abstract accessStorageInformation accessStorageInformation();
    public abstract accessDisplayInformation accessDisplayInformation();
    public abstract accessBatteryInformation accessBatteryInformation();
    public abstract accessDrmInformation accessDrmInformation();
    public abstract accessNetworkInformation accessNetworkInformation();
    public abstract accessCameraInformation accessCameraInformation();
    public abstract accessSensorsInformation accessSensorsInformation();
    public abstract accessThermalInformation accessThermalInformation();

    public static volatile systemInformationDatabase systemInformationDatabase;

    public static systemInformationDatabase getInstance(final Context context){
        if(systemInformationDatabase==null){
            synchronized (systemInformationDatabase.class){
                if(systemInformationDatabase==null){
                    systemInformationDatabase = Room.databaseBuilder(context.getApplicationContext(),systemInformationDatabase.class,"systemInformationDatabase")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return systemInformationDatabase;
    }

}
