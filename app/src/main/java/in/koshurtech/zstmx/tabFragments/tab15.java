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

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.deviceProfileAdapter;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.javaClasses.deviceProfileView;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;


public class tab15 extends Fragment {

    RecyclerView recyclerView;
    deviceProfileAdapter deviceProfileAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<deviceProfileView> deviceProfileViewArrayList = new ArrayList<>();




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
        ArrayList<deviceProfileView> filter = new ArrayList<>();
        for(deviceProfileView f: deviceProfileViewArrayList){
            if(f.getDeviceModel().toLowerCase().trim().contains(newText.trim().toLowerCase())){
                filter.add(f);
            }
        }
        deviceProfileAdapter.updateList(filter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab16, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.deviceProfileSRL);
        recyclerView = (RecyclerView) v.findViewById(R.id.allDevicesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        deviceProfileAdapter = new deviceProfileAdapter(deviceProfileViewArrayList,getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(deviceProfileAdapter);
        deviceProfileViewArrayList.add(new deviceProfileView("OnePlus","6T",10,3,"jkggjh-dfdgdg-dghdfghfdh-dfhdfhdfh"));



        deviceProfileAdapter.notifyDataSetChanged();

        return v;
    }
}