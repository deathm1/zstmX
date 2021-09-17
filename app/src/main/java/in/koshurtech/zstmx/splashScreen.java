package in.koshurtech.zstmx;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import in.koshurtech.zstmx.database.entities.*;
import in.koshurtech.zstmx.viewModels.isAllDone;
import in.koshurtech.zstmx.workers.grabDeviceInfoAndSave;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.USE_FINGERPRINT;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
public class splashScreen extends AppCompatActivity {
    final int[] counter = {0};
    private final int req=101;
    boolean a = false;
    boolean b = false;
    boolean c = false;
    boolean d = false;
    boolean e = false;
    boolean f = false;
    boolean g = false;
    boolean h = false;
    boolean i = false;
    boolean j = false;
    boolean k = false;
    boolean l = false;
    OneTimeWorkRequest work;
    private GLSurfaceView glSurfaceView;
    private StringBuilder sb;
    int hasopen = 0;
    TextView textView;
    int ctrr=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(splashScreen.this);
        String theme = sp.getString("appTheme","0");
        if(theme.equals("0")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if(theme.equals("1")){
            setTheme(R.style.Theme_ZstmX_splash_day);

        }
        else if(theme.equals("2")){
            setTheme(R.style.Theme_ZstmX_splash_night);

        }
        setContentView(R.layout.activity_splash_screen);



        if(!checkPermission()){
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(splashScreen.this);
            materialAlertDialogBuilder.setTitle("Grant Permissions");
            materialAlertDialogBuilder.setMessage("This application needs to extract information from your phone, to do that permissions are required. This data is going to be stored on your device and is not going to be shared with any 3rd party, unless you decide to do so.");
            materialAlertDialogBuilder.setIcon(R.drawable.ic_outline_warning_24);
            materialAlertDialogBuilder.setPositiveButton("Grant Permissions", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(splashScreen.this,
                            new String[]{BLUETOOTH,
                                    READ_PHONE_STATE,
                                    USE_FINGERPRINT,
                                    WRITE_EXTERNAL_STORAGE},
                            req);
                }
            }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            materialAlertDialogBuilder.setCancelable(false);
            materialAlertDialogBuilder.show();
        }
        else {
            starter();
        }


    }


    private void starter(){
        work = new OneTimeWorkRequest.Builder(grabDeviceInfoAndSave.class)
                .addTag("KT_ZSTMX_DB")
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(work);

        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(work.getId())
                .observe(splashScreen.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            isAllDone isAllDone = new ViewModelProvider(splashScreen.this).get(isAllDone.class);
                            isAllDone.setContext(getApplicationContext());
                            isAllDone.invoke();

                            try {
                                LiveData<List<cpuInformation>> cpu = isAllDone.getCpu();
                                LiveData<List<ramInformation>> ram = isAllDone.getRam();
                                LiveData<List<storageInformation>> storage = isAllDone.getStorage();
                                LiveData<List<systemInformation>> system = isAllDone.getSystem();
                                LiveData<List<softwareInformation>> software = isAllDone.getSoftware();
                                LiveData<List<batteryInformation>> battery = isAllDone.getBattery();
                                LiveData<List<cameraInformation>> camera = isAllDone.getCamera();
                                LiveData<List<displayInformation>> display = isAllDone.getDisplay();
                                LiveData<List<drmInformation>> drm = isAllDone.getDrm();
                                LiveData<List<networkInformation>> network = isAllDone.getNetwork();
                                LiveData<List<sensorInformation>> sensor = isAllDone.getSensor();
                                LiveData<List<thermalInformation>> thermal = isAllDone.getThermal();

                                battery.observe(splashScreen.this, new Observer<List<batteryInformation>>() {
                                    @Override
                                    public void onChanged(List<batteryInformation> batteryInformations) {
                                        counter[0]++;
                                        f= true;
                                    }
                                });
                                camera.observe(splashScreen.this, new Observer<List<cameraInformation>>() {
                                    @Override
                                    public void onChanged(List<cameraInformation> cameraInformations) {
                                        counter[0]++;
                                        g= true;
                                    }
                                });
                                display.observe(splashScreen.this, new Observer<List<displayInformation>>() {
                                    @Override
                                    public void onChanged(List<displayInformation> displayInformations) {
                                        counter[0]++;
                                        h= true;
                                    }
                                });
                                drm.observe(splashScreen.this, new Observer<List<drmInformation>>() {
                                    @Override
                                    public void onChanged(List<drmInformation> drmInformations) {
                                        counter[0]++;
                                        i= true;
                                    }
                                });
                                network.observe(splashScreen.this, new Observer<List<networkInformation>>() {
                                    @Override
                                    public void onChanged(List<networkInformation> networkInformations) {
                                        counter[0]++;
                                        j= true;
                                    }
                                });
                                sensor.observe(splashScreen.this, new Observer<List<sensorInformation>>() {
                                    @Override
                                    public void onChanged(List<sensorInformation> sensorInformations) {
                                        counter[0]++;
                                        k= true;
                                    }
                                });
                                thermal.observe(splashScreen.this, new Observer<List<thermalInformation>>() {
                                    @Override
                                    public void onChanged(List<thermalInformation> thermalInformations) {
                                        counter[0]++;
                                        l= true;
                                    }
                                });

                                cpu.observe(splashScreen.this, new Observer<List<cpuInformation>>() {
                                    @Override
                                    public void onChanged(List<cpuInformation> cpuInformations) {
                                        a= true;
                                        counter[0]++;
                                    }
                                });
                                ram.observe(splashScreen.this, new Observer<List<ramInformation>>() {
                                    @Override
                                    public void onChanged(List<ramInformation> ramInformations) {
                                        b = true;
                                        counter[0]++;
                                    }
                                });
                                storage.observe(splashScreen.this, new Observer<List<storageInformation>>() {
                                    @Override
                                    public void onChanged(List<storageInformation> storageInformations) {
                                        c = true;
                                        counter[0]++;
                                    }
                                });
                                system.observe(splashScreen.this, new Observer<List<systemInformation>>() {
                                    @Override
                                    public void onChanged(List<systemInformation> systemInformations) {
                                        d  = true;
                                        counter[0]++;
                                    }
                                });

                                software.observe(splashScreen.this, new Observer<List<softwareInformation>>() {
                                    @Override
                                    public void onChanged(List<softwareInformation> softwareInformations) {
                                        e = true;
                                        counter[0]++;
                                        launcher();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), USE_FINGERPRINT);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED&&
                result3 == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if(checkPermission()){
                    starter();
                }
                else {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(splashScreen.this);
                    materialAlertDialogBuilder.setTitle("Permission Denied");
                    materialAlertDialogBuilder.setMessage("Sorry, You need to grant permissions to this application manually. Go to App Info > Permissions > Grant permissions.");
                    materialAlertDialogBuilder.setIcon(R.drawable.ic_outline_warning_24);
                    materialAlertDialogBuilder.setPositiveButton("App Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null)));
                            finish();
                        }
                    }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    materialAlertDialogBuilder.setCancelable(false);
                    materialAlertDialogBuilder.show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void launcher(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(a&&b&&c&&d&&e&&f&&g&&h&&i&&j&&k&&l&&counter[0]>=11&&hasopen<1){
                    hasopen++;
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        },1000);
    }
}