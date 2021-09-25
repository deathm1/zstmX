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
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.database.entities.batteryInformation;
import in.koshurtech.zstmx.database.entities.ramInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;


public class tab6 extends Fragment {


    RecyclerView recyclerView;
    in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter recyclerInfoViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();

    in.koshurtech.zstmx.database.interfaces.accessBatteryInformation accessBatteryInformation;
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;

    List<batteryInformation> batteryInformationArrayList = new ArrayList<>();


    public tab6(List<batteryInformation> list){
        this.batteryInformationArrayList = list;
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
            if(f.getInfoHeader().contains(newText.toUpperCase())){
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
        View v = inflater.inflate(R.layout.fragment_tab6, container, false);
        recyclerInfoViewArrayList.clear();

        recyclerView = (RecyclerView) v.findViewById(R.id.batteryInformationRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.batteryInformationSRL);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerInfoViewArrayList!=null){
                    recyclerInfoViewArrayList.clear();

                    for(int i=0; i<batteryInformationArrayList.size(); i++){
                        recyclerInfoViewArrayList.add(new recyclerInfoView(batteryInformationArrayList.get(i).propertyBody,batteryInformationArrayList.get(i).propertyName));

                    }

                    recyclerInfoViewAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerInfoViewAdapter = new recyclerInfoViewAdapter(recyclerInfoViewArrayList,getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(recyclerInfoViewAdapter);



        for(int i=0; i<batteryInformationArrayList.size(); i++){
            recyclerInfoViewArrayList.add(new recyclerInfoView(batteryInformationArrayList.get(i).propertyBody,batteryInformationArrayList.get(i).propertyName));

        }

        recyclerInfoViewAdapter.notifyDataSetChanged();

        return v;
    }
}