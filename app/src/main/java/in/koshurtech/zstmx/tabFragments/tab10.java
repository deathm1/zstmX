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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.sensorViewAdapter;
import in.koshurtech.zstmx.database.entities.sensorInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;
import in.koshurtech.zstmx.javaClasses.sensorInfoView;


public class tab10 extends Fragment {



    RecyclerView recyclerView;
    sensorViewAdapter sensorViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<sensorInfoView> sensorInfoViewArrayList = new ArrayList<>();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    in.koshurtech.zstmx.database.interfaces.accessDisplayInformation accessDisplayInformation;
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;

    List<sensorInformation> sensorInformationList = new ArrayList<>();


    public tab10(List<sensorInformation> list){
        this.sensorInformationList = list;
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
        ArrayList<sensorInfoView> filter = new ArrayList<>();
        for(sensorInfoView f: sensorInfoViewArrayList){
            if(f.getHeader().toLowerCase().trim().contains(newText.trim().toLowerCase())){
                filter.add(f);
            }
        }

        sensorViewAdapter.updateList(filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab11, container, false);


        sensorInfoViewArrayList.clear();
        recyclerView = (RecyclerView) v.findViewById(R.id.sensorRV);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.sensorSRL);










        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(sensorInfoViewArrayList!=null){
                    sensorInfoViewArrayList.clear();

                    for(int i=0; i<sensorInformationList.size(); i++){
                        sensorInfoViewArrayList.add(new sensorInfoView(sensorInformationList.get(i).propertyName,sensorInformationList.get(i).propertyBody, true));

                    }

                    sensorViewAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        sensorViewAdapter = new sensorViewAdapter(sensorInfoViewArrayList,getActivity(), getActivity());
        recyclerView.setAdapter(sensorViewAdapter);



        for(int i=0; i<sensorInformationList.size(); i++){
            sensorInfoViewArrayList.add(new sensorInfoView(sensorInformationList.get(i).propertyName,sensorInformationList.get(i).propertyBody, true));

        }

        sensorViewAdapter.notifyDataSetChanged();


        return v;
    }
}