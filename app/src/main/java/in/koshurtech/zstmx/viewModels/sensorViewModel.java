package in.koshurtech.zstmx.viewModels;

import static android.content.Context.SENSOR_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.Arrays;

public class sensorViewModel extends ViewModel {

    Activity activity;
    int sensorType;

    private MutableLiveData<String> sensorLiveData;
    private Context context;


    public void setSystem(Context context, Activity activity, int sensorType){
        this.context = context;
        this.activity = activity;
        this.sensorType = sensorType;
    }

    public MutableLiveData<String> getSensorLiveData() throws IOException {
        if(sensorLiveData==null){
            sensorLiveData = new MutableLiveData<>();
            invoke();
        }
        return sensorLiveData;
    }

    private void invoke() {
        SensorManager mySensorManager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = mySensorManager.getDefaultSensor(sensorType);
        mySensorManager.registerListener(SensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



    private final SensorEventListener SensorListener = new SensorEventListener(){
            @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {


        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            sensorLiveData.postValue(Arrays.toString(event.values));
        }

    };
}
