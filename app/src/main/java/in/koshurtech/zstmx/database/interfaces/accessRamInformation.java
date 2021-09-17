package in.koshurtech.zstmx.database.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import in.koshurtech.zstmx.database.entities.ramInformation;



@Dao
public interface accessRamInformation {

    @Query("DELETE FROM ramInformation")
    void deleteRamInformation();


    @Query("SELECT * FROM ramInformation")
    List<ramInformation> getRamInformation();

    @Insert
    void insertRamInformationIntoDatabase(ramInformation ramInformation);
}
