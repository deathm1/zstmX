package in.koshurtech.zstmx.database.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import in.koshurtech.zstmx.database.entities.displayInformation;

@Dao
public interface accessDisplayInformation {
    @Query("SELECT * FROM displayInformation")
    List<displayInformation> getDisplayInformation();
    @Query("DELETE FROM displayInformation")
    void deleteDisplayInformation();
    @Insert
    void insertDisplayInformationIntoDatabase(displayInformation displayInformation);
}
