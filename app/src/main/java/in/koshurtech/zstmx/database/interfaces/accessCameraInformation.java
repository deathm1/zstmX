package in.koshurtech.zstmx.database.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import in.koshurtech.zstmx.database.entities.cameraInformation;

@Dao
public interface accessCameraInformation {
    @Query("SELECT * FROM cameraInformation")
    List<cameraInformation> getCameraInformation();


    @Query("SELECT * FROM cameraInformation WHERE :cquery")
    List<cameraInformation> getCameraInformationQuery(boolean cquery);


    @Query("DELETE FROM cameraInformation")
    void deleteCameraInformation();
    @Insert
    void insertCameraInformationIntoDatabase(cameraInformation cameraInformation);
}
