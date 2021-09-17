package in.koshurtech.zstmx.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "cameraInformation")
public class cameraInformation {
    public cameraInformation(String id,String cameraNumber, String propertyName, String propertyBody){
        this.propertyName = propertyName;
        this.propertyBody = propertyBody;
        this.propertyBody = propertyBody;
        this.cameraNumber = cameraNumber;
        this.id = id;
    }
    @NonNull
    @PrimaryKey
    public String id;


    @NonNull
    @ColumnInfo(name = "cameraNumber")
    public String cameraNumber;


    @NonNull
    @ColumnInfo(name = "propertyName")
    public String propertyName;


    @NonNull
    @ColumnInfo(name = "propertyBody")
    public String propertyBody;
}
