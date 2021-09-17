package in.koshurtech.zstmx.database.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;



@Entity(tableName = "cpuInformation")
public class cpuInformation {



    public cpuInformation(String id,String cpuProperty, String propertyData){
        this.cpuProperty = cpuProperty;
        this.propertyData = propertyData;
        this.id = id;

    }



    @NonNull
    @PrimaryKey
    public String id;

    @NonNull
    @ColumnInfo(name = "propertyName")
    public String cpuProperty;


    @NonNull
    @ColumnInfo(name = "propertyData")
    public String propertyData;

}
