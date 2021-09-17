package in.koshurtech.zstmx.database.interfaces;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.koshurtech.zstmx.database.entities.cpuInformation;

@Dao
public interface accessCpuInformation {

    @Query("SELECT * FROM cpuInformation")
    List<cpuInformation> getCpuInformation();

    @Query("DELETE FROM cpuInformation")
    void deleteCpuInformation();

    @Insert
    void insertCpuInformationIntoDatabase(cpuInformation cpuInformation);
}
