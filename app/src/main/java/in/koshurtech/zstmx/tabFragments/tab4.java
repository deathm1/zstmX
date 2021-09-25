package in.koshurtech.zstmx.tabFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.deviceProfileAdapter;
import in.koshurtech.zstmx.javaClasses.deviceProfileView;
import in.koshurtech.zstmx.tabFragments.subFragments.cpuSubFragments.cpuGraphs;
import in.koshurtech.zstmx.tabFragments.subFragments.cpuSubFragments.cpuInformationFragment;


public class tab4 extends Fragment {







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
    }
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab7, container, false);



        BottomNavigationView navigationView = (BottomNavigationView) v.findViewById(R.id.bottomNavigationCPU);




        getChildFragmentManager().beginTransaction().replace(R.id.cpu_bottomNav_frame,new cpuGraphs()).commit();


        navigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull @NotNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.cpu_graphs:



                    case R.id.cpu_information:

                }
            }
        });

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.cpu_graphs:
                        getChildFragmentManager().beginTransaction().replace(R.id.cpu_bottomNav_frame,new cpuGraphs()).commit();
                        return true;


                    case R.id.cpu_information:
                        getChildFragmentManager().beginTransaction().replace(R.id.cpu_bottomNav_frame,new cpuInformationFragment()).commit();
                        return true;
                }

                return false;
            }
        });
        return v;
    }

}