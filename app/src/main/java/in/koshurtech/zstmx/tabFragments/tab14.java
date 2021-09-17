package in.koshurtech.zstmx.tabFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.activities.deviceMonitor;
import in.koshurtech.zstmx.activities.deviceMonitorResults;


public class tab14 extends Fragment {

    MaterialButton materialButton;
    MaterialButton materialButton2;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab15, container, false);
        materialButton = (MaterialButton) v.findViewById(R.id.startBenchmark);
        materialButton2 = (MaterialButton) v.findViewById(R.id.monitorResults);






        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), deviceMonitor.class));
                getActivity().finishAffinity();
                System.exit(0);



                //startActivity(new Intent(getActivity().getApplicationContext(), deviceBenchmark.class));
            }
        });

        materialButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), deviceMonitorResults.class));
                //startActivity(new Intent(getActivity().getApplicationContext(), deviceBenchmark.class));
            }
        });
        return v;
    }
}