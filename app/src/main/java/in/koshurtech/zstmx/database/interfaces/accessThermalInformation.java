package in.koshurtech.zstmx.database.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.koshurtech.zstmx.database.entities.thermalInformation;


@Dao
public interface accessThermalInformation {
    @Query("DELETE FROM thermalInformation")
    void deleteThermalInformation();

    @Query("SELECT * FROM thermalInformation")
    List<thermalInformation> getThermalInformation();

    @Insert
    void insertThermalInformationIntoDatabase(thermalInformation thermalInformation);
}
