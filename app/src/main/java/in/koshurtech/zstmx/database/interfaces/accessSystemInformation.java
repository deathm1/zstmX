package in.koshurtech.zstmx.database.interfaces;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.koshurtech.zstmx.database.entities.systemInformation;

@Dao
public interface accessSystemInformation {


    @Query("DELETE FROM systemInformation")
    void deleteSystemInformation();

    @Query("SELECT * FROM systemInformation")
    List<systemInformation> getSystemInformation();

    @Insert
    void insertSystemInformationIntoDatabase(systemInformation systemInformation);
}
