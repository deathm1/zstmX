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
import in.koshurtech.zstmx.database.entities.ramInformation;
import in.koshurtech.zstmx.database.entities.storageInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;


public class tab7 extends Fragment {


    RecyclerView recyclerView;
    recyclerInfoViewAdapter recyclerInfoViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    in.koshurtech.zstmx.database.interfaces.accessRamInformation accessRamInformation;
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;

    List<storageInformation> storageInformationArrayList = new ArrayList<>();


    public tab7(List<storageInformation> list){
        this.storageInformationArrayList = list;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab8, container, false);
        recyclerInfoViewArrayList.clear();
        recyclerView = (RecyclerView) v.findViewById(R.id.storageInformationRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.storageInformationSRL);










        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerInfoViewArrayList!=null){
                    recyclerInfoViewArrayList.clear();

                    for(int i=0; i<storageInformationArrayList.size(); i++){
                        recyclerInfoViewArrayList.add(new recyclerInfoView(storageInformationArrayList.get(i).propertyBody,storageInformationArrayList.get(i).propertyName));

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



        for(int i=0; i<storageInformationArrayList.size(); i++){
            recyclerInfoViewArrayList.add(new recyclerInfoView(storageInformationArrayList.get(i).propertyBody,storageInformationArrayList.get(i).propertyName));

        }

        recyclerInfoViewAdapter.notifyDataSetChanged();
        return v;
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
}