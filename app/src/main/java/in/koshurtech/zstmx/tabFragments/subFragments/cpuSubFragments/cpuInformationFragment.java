package in.koshurtech.zstmx.tabFragments.subFragments.cpuSubFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import java.util.ArrayList;
import java.util.List;

import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.database.entities.cpuInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;


public class cpuInformationFragment extends Fragment {

    RecyclerView recyclerView;
    recyclerInfoViewAdapter recyclerInfoViewAdapter;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseQuery.start();
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

    in.koshurtech.zstmx.database.interfaces.accessCpuInformation accessCpuInformation;
    in.koshurtech.zstmx.database.systemInformationDatabase systemInformationDatabase;

    List<cpuInformation> cpuInformationList = new ArrayList<>();

    Thread databaseQuery = new Thread(new Runnable() {
        @Override
        public void run() {
            systemInformationDatabase = in.koshurtech.zstmx.database.systemInformationDatabase.getInstance(getContext());
            accessCpuInformation = systemInformationDatabase.accessCPUInformation();
            cpuInformationList = accessCpuInformation.getCpuInformation();
        }
    });


    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cpu_information, container, false);
        recyclerInfoViewArrayList.clear();

        recyclerView = (RecyclerView) v.findViewById(R.id.cpu_InformationView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.cpu_SRL);

        SearchView searchView = (SearchView) v.findViewById(R.id.cpu_InfoRVSearch);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideKeyboardFrom(getContext(),v);
                searchView.onActionViewCollapsed();
                return true;
            }
        });

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


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(recyclerInfoViewArrayList!=null){
                    recyclerInfoViewArrayList.clear();

                    for(int i=0; i<cpuInformationList.size(); i++){
                        recyclerInfoViewArrayList.add(new recyclerInfoView(cpuInformationList.get(i).propertyData,cpuInformationList.get(i).cpuProperty));

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




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<cpuInformationList.size(); i++){
                    recyclerInfoViewArrayList.add(new recyclerInfoView(cpuInformationList.get(i).propertyData,cpuInformationList.get(i).cpuProperty));
                }
                recyclerInfoViewAdapter.notifyDataSetChanged();
            }
        },100);

        return v;
    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void showKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

}