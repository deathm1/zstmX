package in.koshurtech.zstmx.activities.settingsPages;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.deviceMonitorResults;
import in.koshurtech.zstmx.workers.reportGenerator;

public class generateReport extends Fragment {




    ChipGroup chipGroup;
    ProgressBar progressBar;
    ExtendedFloatingActionButton floatingActionButton;
    MaterialButton openResults;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generate_report, container, false);

        chipGroup = (ChipGroup) v.findViewById(R.id.genRepChipGroup);
        openResults = (MaterialButton) v.findViewById(R.id.openResults);


        openResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), showReports.class));
            }
        });

        floatingActionButton = (ExtendedFloatingActionButton) v.findViewById(R.id.generateReportButton);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBarRep);

        progressBar.setVisibility(View.INVISIBLE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.setEnabled(false);
                List<Integer> chips = chipGroup.getCheckedChipIds();
                if(chips.size()!=0){
                    progressBar.setVisibility(View.VISIBLE);
                    int[] arr = new int[chips.size()];
                    int j=0;
                    for (int i: chips
                    ) {
                        arr[j] = i;
                        j++;
                    }
                    Data myData = new Data.Builder()
                            .putIntArray("configuration",arr)
                            .build();
                    work = new OneTimeWorkRequest.Builder(reportGenerator.class)
                            .setInputData(myData)
                            .addTag("generator")
                            .build();
                    WorkManager.getInstance(getActivity().getApplicationContext()).enqueue(work);
                    WorkManager.getInstance(getActivity().getApplicationContext())
                            .getWorkInfoByIdLiveData(work.getId())
                            .observe(getActivity(), new Observer<WorkInfo>() {
                                @Override
                                public void onChanged(WorkInfo workInfo) {
                                    String myResult = workInfo.getOutputData().getString("output");
                                    if(gg){
                                        gg = false;
                                        floatingActionButton.setEnabled(true);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        showSnack("Report is generated.",v,myResult);
                                    }

                                }
                            });
                }
                else {
                    floatingActionButton.setEnabled(true);
                    showSnack2("Query was not selected",v);
                }

            }
        });
        return v;
    }
    OneTimeWorkRequest work;

    boolean gg = true;

    public void showSnack(String mm, View v, String uri){
        Snackbar.make(v, mm,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("OPEN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gg = true;
                        startActivity(new Intent(getActivity().getApplicationContext(), showReports.class));

                    }
                }).show();
    }



    public void showSnack2(String mm,View v){
        Snackbar.make(v, mm,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }




}