package in.koshurtech.zstmx.database.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import in.koshurtech.zstmx.database.entities.drmInformation;
@Dao
public interface accessDrmInformation {
    @Query("SELECT * FROM drmInformation")
    List<drmInformation> getDrmInformation();
    @Query("DELETE FROM drmInformation")
    void deleteDrmInformation();
    @Insert
    void insertDrmInformationIntoDatabase(drmInformation drmInformation);
}
