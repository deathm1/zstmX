package in.koshurtech.zstmx.viewModels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.io.IOException;

import github.nisrulz.easydeviceinfo.base.EasyNetworkMod;
import github.nisrulz.easydeviceinfo.base.NetworkType;

public class networkInfoViewModel extends ViewModel {

    private MutableLiveData<String> networkLivedata;

    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

    public MutableLiveData<String> getNetworkInfo() throws IOException {
        if(networkLivedata==null){
            networkLivedata = new MutableLiveData<>();
            invoke();
        }
        return networkLivedata;
    }

    Thread thread;
    int units;

    private void invoke(){
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(context);
        String memoryUnit = sp.getString("memoryUnit","0");
        String stepSize = sp.getString("stepSize","3");
        if(memoryUnit.equals(String.valueOf(0))){
            units = 0;
        }
        else if(memoryUnit.equals(String.valueOf(1))){
            units = 1;
        }
        int mills = 0;
        if(stepSize.equals(String.valueOf(0))){
            mills = 250;
        }
        else if(stepSize.equals(String.valueOf(1))){
            mills = 500;
        }
        else if(stepSize.equals(String.valueOf(2))){
            mills = 750;
        }
        else if(stepSize.equals(String.valueOf(3))){
            mills = 1000;
        }

        int finalMills = mills;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        easyNetworkMod = new EasyNetworkMod(context);
                        networkLivedata.postValue(getNetwork());
                        Thread.sleep(finalMills);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        thread.setName("networkThread");
    }
    EasyNetworkMod easyNetworkMod;

    private String getNetwork(){
        String out = "";
        @NetworkType
        int networkType = easyNetworkMod.getNetworkType();

        String nt = "";
        switch (networkType) {
            case NetworkType.CELLULAR_UNKNOWN:
                nt =  "Unknown";
                break;
            case NetworkType.CELLULAR_UNIDENTIFIED_GEN:
                nt = "Unidentified Generation";
                break;
            case NetworkType.CELLULAR_2G:
                nt = "Cellular 2G";
                break;
            case NetworkType.CELLULAR_3G:
                nt = "Cellular 3G";
                break;
            case NetworkType.CELLULAR_4G:
                nt = "Cellular 4G";
                break;
            case NetworkType.WIFI_WIFIMAX:
                nt = "WiFi/WiFi Max";

                break;
            case NetworkType.UNKNOWN:
                nt = "Unknown";
            default:
                nt = "Unknown";
                break;
        }

        out = "Network Type"+"="+nt + "--"+"Network Availability"+":"+easyNetworkMod.isNetworkAvailable() + "--"
                +"WiFi Enabled"+"="+easyNetworkMod.isWifiEnabled()+"--"
                +"WiFi Link Speed"+"="+easyNetworkMod.getWifiLinkSpeed()+ "--"
                +"IPv6 Address"+"="+easyNetworkMod.getIPv6Address()+ "--"
                +"WiFi SSID"+"="+easyNetworkMod.getWifiSSID()+ "--"
                +"WiFi BSSID"+"="+easyNetworkMod.getWifiBSSID()+ "--"
                +"WiFi MAC"+"="+easyNetworkMod.getWifiMAC();




        return out;
    }

}
