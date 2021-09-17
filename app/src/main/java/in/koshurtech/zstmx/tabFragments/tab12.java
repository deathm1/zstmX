package in.koshurtech.zstmx.tabFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.database.entities.cameraInformation;
import in.koshurtech.zstmx.database.entities.displayInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;


public class tab12 extends Fragment {

    RecyclerView recyclerView;
    in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter recyclerInfoViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();
    in.koshurtech.zstmx.database.interfaces.accessCameraInformation accessCameraInformation;
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;
    List<cameraInformation> cameraInformationArrayList = new ArrayList<>();
    Spinner spinner;

    public tab12(List<cameraInformation> list){
        this.cameraInformationArrayList = list;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.searchIcon);
        item.setVisible(true);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }


    private void filter(String newText) {
        ArrayList<recyclerInfoView> filter = new ArrayList<>();
        for(recyclerInfoView f: recyclerInfoViewArrayList){
            if(f.getInfoHeader().toLowerCase().trim().contains(newText.trim().toLowerCase())){
                filter.add(f);
            }
        }
        recyclerInfoViewAdapter.updateList(filter);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab13, container, false);
        spinner = (Spinner) v.findViewById(R.id.cameraSelector);


        recyclerInfoViewArrayList.clear();
        recyclerView = (RecyclerView) v.findViewById(R.id.cameraInformationRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.cameraInformationSRL);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerInfoViewAdapter = new recyclerInfoViewAdapter(recyclerInfoViewArrayList,getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(recyclerInfoViewAdapter);


        int len = Integer.parseInt(cameraInformationArrayList.get(0).propertyBody);
        String[] spinnerVals = new String[len];
        for(int i=0; i<len; i++){
            spinnerVals[i] = "Camera "+i;
        }
        aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,spinnerVals);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);







        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                cp = position;
                recyclerInfoViewArrayList.clear();
                for(int i=0; i<cameraInformationArrayList.size(); i++){

                    if(!cameraInformationArrayList.get(i).cameraNumber.equals("NaN")){
                        if(Integer.parseInt(cameraInformationArrayList.get(i).cameraNumber)==position){
                            recyclerInfoViewArrayList.add(new recyclerInfoView(cameraInformationArrayList.get(i).propertyBody,cameraInformationArrayList.get(i).propertyName));
                        }
                    }
                }

                recyclerInfoViewAdapter.updateList(recyclerInfoViewArrayList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerInfoViewArrayList!=null){
                    for(int i=0; i<cameraInformationArrayList.size(); i++){

                        if(!cameraInformationArrayList.get(i).cameraNumber.equals("NaN")){
                            if(Integer.parseInt(cameraInformationArrayList.get(i).cameraNumber)==cp){
                                recyclerInfoViewArrayList.add(new recyclerInfoView(cameraInformationArrayList.get(i).propertyBody,cameraInformationArrayList.get(i).propertyName));
                            }
                        }
                    }
                    recyclerInfoViewAdapter.updateList(recyclerInfoViewArrayList);
                    swipeRefreshLayout.setRefreshing(false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        return v;
    }


    ArrayAdapter aa;
    int cp;
}