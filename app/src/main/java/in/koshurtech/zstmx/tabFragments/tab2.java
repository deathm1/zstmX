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
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.koshurtech.zstmx.MainActivity;
import in.koshurtech.zstmx.R;
import in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter;
import in.koshurtech.zstmx.database.entities.cpuInformation;
import in.koshurtech.zstmx.database.entities.systemInformation;
import in.koshurtech.zstmx.javaClasses.recyclerInfoView;


public class tab2 extends Fragment {


    RecyclerView recyclerView;
    in.koshurtech.zstmx.adapters.recyclerInfoViewAdapter recyclerInfoViewAdapter;
    ArrayList<recyclerInfoView> recyclerInfoViewArrayList = new ArrayList<>();

    List<systemInformation> systemInformationArrayList = new ArrayList<>();



    ImageView phoneLogo;
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




    public tab2(List<systemInformation> list){
        this.systemInformationArrayList = list;
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
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        recyclerInfoViewArrayList.clear();


        recyclerView = (RecyclerView) v.findViewById(R.id.system_Recycler_View);




        phoneLogo = (ImageView) v.findViewById(R.id.phoneLogo);
        textView1 = (TextView) v.findViewById(R.id.phoneName);
        textView2 = (TextView) v.findViewById(R.id.phoneDescription);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerInfoViewAdapter = new recyclerInfoViewAdapter(recyclerInfoViewArrayList,getActivity().getApplicationContext(),getActivity());
        recyclerView.setAdapter(recyclerInfoViewAdapter);



        for(int i=0; i<systemInformationArrayList.size(); i++){


            if(systemInformationArrayList.get(i).propertyName.equals("Manufacturer")){

                if(systemInformationArrayList.get(i).propertyBody.toLowerCase().equals("oneplus")){
                    phoneLogo.setImageResource(R.drawable.oneplus);
                }
                String f = systemInformationArrayList.get(i).propertyBody;
                textView1.setText(f);

            }

            if(systemInformationArrayList.get(i).propertyName.equals("Model")){
                String f2 = systemInformationArrayList.get(i).propertyBody;
                textView2.setText(f2);

            }
            recyclerInfoViewArrayList.add(new recyclerInfoView(systemInformationArrayList.get(i).propertyBody,systemInformationArrayList.get(i).propertyName));

        }

        recyclerInfoViewAdapter.notifyDataSetChanged();




        return v;
    }
}