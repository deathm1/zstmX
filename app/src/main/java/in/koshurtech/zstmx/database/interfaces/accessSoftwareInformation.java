package in.koshurtech.zstmx.database.interfaces;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.koshurtech.zstmx.database.entities.softwareInformation;



@Dao
public interface accessSoftwareInformation {

    @Query("DELETE FROM softwareInformation")
    void deleteSoftwareInformation();


    @Query("SELECT * FROM softwareInformation")
    List<softwareInformation> getSoftwareInformation();

    @Insert
    void insertSoftwareInformationIntoDatabase(softwareInformation softwareInformation);
}
