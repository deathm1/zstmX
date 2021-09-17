package in.koshurtech.zstmx.tabFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import java.util.ArrayList;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.database.entities.thermalInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;
import in.koshurtech.zstmx.viewModels.thermalInfoViewModel;


public class tab13 extends Fragment {

    RecyclerView recyclerView;
    in.koshurtech.zstmx.adapters.sensorViewAdapter sensorViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();
    ArrayList<thermalInformation> thermalInformationArrayList = new ArrayList<>();
    in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter recyclerInfoViewAdapter;
    ProgressBar ProgressBar;








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab14, container, false);


        recyclerInfoViewArrayList.clear();
        recyclerView = (RecyclerView) v.findViewById(R.id.thermalRV);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerInfoViewAdapter = new recyclerInfoViewAdapter(recyclerInfoViewArrayList,getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(recyclerInfoViewAdapter);
        ProgressBar = (ProgressBar) v.findViewById(R.id.progressbarthermals);

        thermalInfoViewModel = new ViewModelProvider(getActivity()).get(in.koshurtech.zstmx.viewModels.thermalInfoViewModel.class);
        thermalInfoViewModel.setContext(getActivity().getApplicationContext());
        thermalInfoViewModel.invoke();

        try {
            LiveData<String> out = thermalInfoViewModel.getThermalInfo();
            out.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    ProgressBar.setVisibility(View.INVISIBLE);
                    recyclerInfoViewArrayList.clear();
                    String[] data = s.split("--");

                    for(int i=0;i<data.length; i++){
                        String[] spl = data[i].split(":");
                        if(spl.length>1){
                            recyclerInfoViewArrayList.add(new recyclerInfoView(spl[1],spl[0]));
                        }
                    }

                    recyclerInfoViewAdapter.notifyDataSetChanged();


                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return v;
    }

    thermalInfoViewModel thermalInfoViewModel;
}