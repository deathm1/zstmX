package in.koshurtech.zstmx.database.interfaces;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import in.koshurtech.zstmx.database.entities.storageInformation;



@Dao
public interface accessStorageInformation {

    @Query("DELETE FROM storageInformation")
    void deleteStorageInformation();

    @Query("SELECT * FROM storageInformation")
    List<storageInformation> getStorageInformation();

    @Insert
    void insertStorageInformationIntoDatabase(storageInformation storageInformation);
}
