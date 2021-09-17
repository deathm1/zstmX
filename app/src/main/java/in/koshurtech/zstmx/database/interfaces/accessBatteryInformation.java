package in.koshurtech.zstmx.database.interfaces;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import in.koshurtech.zstmx.database.entities.batteryInformation;


@Dao
public interface accessBatteryInformation {
    @Query("SELECT * FROM batteryInformation")
    List<batteryInformation> getBatteryInformation();
    @Query("DELETE FROM batteryInformation")
    void deleteBatteryInformation();
    @Insert
    void insertBatteryInformationIntoDatabase(batteryInformation batteryInformation);
}
