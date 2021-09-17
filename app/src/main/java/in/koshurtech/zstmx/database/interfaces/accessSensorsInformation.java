package in.koshurtech.zstmx.database.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.koshurtech.zstmx.database.entities.sensorInformation;


@Dao
public interface accessSensorsInformation {

    @Query("DELETE FROM sensorInformation")
    void deleteSensorInformation();


    @Query("SELECT * FROM sensorInformation")
    List<sensorInformation> getSensorInformation();

    @Insert
    void insertSensorInformationIntoDatabase(sensorInformation sensorInformation);
}
