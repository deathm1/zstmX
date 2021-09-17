package in.koshurtech.zstmx.database.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import in.koshurtech.zstmx.database.entities.networkInformation;

@Dao
public interface accessNetworkInformation {
    @Query("SELECT * FROM networkInformation")
    List<networkInformation> getNetworkInformation();
    @Query("DELETE FROM networkInformation")
    void deleteNetworkInformation();
    @Insert
    void insertNetworkInformationIntoDatabase(networkInformation networkInformation);
}
