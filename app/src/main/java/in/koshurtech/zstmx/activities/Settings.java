package in.koshurtech.zstmx.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.database.entities.batteryInformation;
import in.koshurtech.zstmx.database.entities.cameraInformation;
import in.koshurtech.zstmx.database.entities.cpuInformation;
import in.koshurtech.zstmx.database.entities.displayInformation;
import in.koshurtech.zstmx.database.entities.drmInformation;
import in.koshurtech.zstmx.database.entities.networkInformation;
import in.koshurtech.zstmx.database.entities.ramInformation;
import in.koshurtech.zstmx.database.entities.sensorInformation;
import in.koshurtech.zstmx.database.entities.softwareInformation;
import in.koshurtech.zstmx.database.entities.storageInformation;
import in.koshurtech.zstmx.database.entities.systemInformation;
import in.koshurtech.zstmx.database.entities.thermalInformation;
import in.koshurtech.zstmx.splashScreen;
import in.koshurtech.zstmx.viewModels.isAllDone;
import in.koshurtech.zstmx.workers.grabDeviceInfoAndSave;

public class Settings extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TITLE_TAG = "settingsActivityTitle";


    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            super.onBackPressed();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("appTheme","0");
        if(theme.equals("0")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else if(theme.equals("1")){
            setTheme(R.style.Theme_ZstmX_day);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            getSupportActionBar().setElevation(0);
        }
        else if(theme.equals("2")){
            setTheme(R.style.Theme_ZstmX_night);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#121212")));

        }


        setContentView(R.layout.settings_activity);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new HeaderFragment())
                    .commit();
        } else {
            setTitle(savedInstanceState.getCharSequence(TITLE_TAG));
        }
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            setTitle(R.string.title_activity_settings);
                        }
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();

        }
        return true;
    }



    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());
        return true;
    }

    public static class HeaderFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey);



            ListPreference lp2 = (ListPreference) findPreference("memoryUnit");
            lp2.setDefaultValue(0);
            lp2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Creating database with modifications...", true);
                    dialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.hide();
                            dialog.dismiss();
                            startActivity(new Intent(getActivity().getApplicationContext(), splashScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            getActivity().finish();
                        }
                    },3000);
                    return true;
                }
            });


            ListPreference lp3 = (ListPreference) findPreference("temperature");
            lp3.setDefaultValue(0);
            lp3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Creating database with modifications...", true);
                    dialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.hide();
                            dialog.dismiss();
                            startActivity(new Intent(getActivity().getApplicationContext(), splashScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            getActivity().finish();
                        }
                    },2000);

                    return true;
                }
            });

            ListPreference lp = (ListPreference) findPreference("appTheme");
            lp.setDefaultValue(0);
            lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    return true;
                }
            });
        }
    }









}
