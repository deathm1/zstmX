package in.koshurtech.zstmx.javaClasses;

import android.content.Context;
import android.media.MediaDrm;
import java.util.IdentityHashMap;
import java.util.Map;

public class getDrmInfo {


    private static final int[] f62a = {C0702R.string.text_version, C0702R.string.text_widevine_system_id, C0702R.string.text_widevine_security_level, C0702R.string.text_widevine_hdcp_level, C0702R.string.text_widevine_max_hdcp_level, C0702R.string.text_widevine_usage_reporting_support, C0702R.string.text_widevine_maximum_number_of_sessions, C0702R.string.text_widevine_number_of_open_sessions};
    private static final String[] f63b = {"version", "systemId", "securityLevel", "hdcpLevel", "maxHdcpLevel", "usageReportingSupport", "maxNumberOfSessions", "numberOfOpenSessions"};
    private MediaDrm f64c;
    private MediaDrm f45a;
    private MediaDrm f52a;
    private MediaDrm f61a;
    Context context;

    public void invoker(Context context) {

        try {
            this.f52a = MediaDrmFactory.m45a(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            this.f64c = MediaDrmFactory.m45a(1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            this.f45a = MediaDrmFactory.m45a(2);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            this.f61a  = MediaDrmFactory.m45a(3);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        this.context = context;
    }

    public Map<String,String> getWideVineInformation(){
        Map<String, String> map = new IdentityHashMap<>();

        if (this.f64c != null) {
            for (int i = 0; i < f62a.length; i++) {
                try {
                    map.put(f63b[i],this.f64c.getPropertyString(f63b[i]));
                } catch (RuntimeException e) {
                }
            }
        }
        return map;
    }
    public Map<String,String> getMerlin(){
        Map<String, String> map = new IdentityHashMap<>();
        if (this.f45a != null) {
            int[] iArr = {C0702R.string.text_drm_description, C0702R.string.text_version};
            String[] strArr = {"description", "version"};
            for (int i = 0; i < iArr.length; i++) {
                try {
                    map.put(String.valueOf(iArr[i]),this.f45a.getPropertyString(strArr[i]));
                } catch (RuntimeException e) {
                }
            }
        }
        return map;
    }
    public Map<String,String> getMicrosoftPlayReady(){
        Map<String, String> map = new IdentityHashMap<>();
        if (this.f61a!= null) {
            int[] iArr = {C0702R.string.text_drm_description, C0702R.string.text_version};
            String[] strArr = {"description", "version"};
            for (int i = 0; i < iArr.length; i++) {
                try {
                    map.put(String.valueOf(iArr[i]),this.f61a.getPropertyString(strArr[i]));
                } catch (RuntimeException e) {
                }
            }
        }
        return map;
    }

}
