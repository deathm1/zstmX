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
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.database.entities.drmInformation;
import in.koshurtech.zstmx.database.entities.softwareInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;

public class tab11 extends Fragment {


    RecyclerView recyclerView;
    in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter recyclerInfoViewAdapter;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();
    List<drmInformation> drmInformationArrayList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    TextView textView1;
    TextView textView2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void filter(String newText) {
        ArrayList<recyclerInfoView> filter = new ArrayList<>();
        for(recyclerInfoView f: recyclerInfoViewArrayList){
            if(f.getInfoHeader().toUpperCase().contains(newText.toUpperCase())){
                filter.add(f);
            }
        }
        recyclerInfoViewAdapter.updateList(filter);
    }


    public tab11(List<drmInformation> list){
        this.drmInformationArrayList = list;
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
        View v = inflater.inflate(R.layout.fragment_tab12, container, false);


        textView1 = (TextView) v.findViewById(R.id.line1);
        textView2 = (TextView) v.findViewById(R.id.line2);



        recyclerInfoViewArrayList.clear();
        recyclerView = (RecyclerView) v.findViewById(R.id.drmInformationRecyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.drmInformationSRL);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerInfoViewArrayList!=null){
                    recyclerInfoViewArrayList.clear();

                    for(int i=0; i<drmInformationArrayList.size(); i++){
                        recyclerInfoViewArrayList.add(new recyclerInfoView(drmInformationArrayList.get(i).propertyBody,drmInformationArrayList.get(i).propertyName));

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
        for(int i=0; i<drmInformationArrayList.size(); i++){
            if(drmInformationArrayList.get(i).propertyName.equals("Build Version Release")){
                String f = "Android "+drmInformationArrayList.get(i).propertyBody;
                textView1.setText(f);

            }

            if(drmInformationArrayList.get(i).propertyName.equals("Build Version SDK")){
                String f2 = "Version SDK "+drmInformationArrayList.get(i).propertyBody;
                textView2.setText(f2);

            }
            recyclerInfoViewArrayList.add(new recyclerInfoView(drmInformationArrayList.get(i).propertyBody,drmInformationArrayList.get(i).propertyName));
        }
        recyclerInfoViewAdapter.notifyDataSetChanged();



        return v;
    }
}