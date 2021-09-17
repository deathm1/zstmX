package in.koshurtech.zstmx.activities.settingsPages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

import in.koshurtech.zstmx.R;


public class appInfo extends Fragment {
    MaterialCardView github;
    MaterialCardView twitter;
    MaterialCardView steam;
    MaterialCardView instagram;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_app_info, container, false);

        github = (MaterialCardView) v.findViewById(R.id.github);
        twitter = (MaterialCardView) v.findViewById(R.id.twitter);
        steam = (MaterialCardView) v.findViewById(R.id.steam);
        instagram = (MaterialCardView) v.findViewById(R.id.instagram);


        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("dsd");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/deathm1"));
                startActivity(intent);
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/komradKoshur"));
                startActivity(intent);
            }
        });


        steam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://steamcommunity.com/id/phaking_noob/"));
                startActivity(intent);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/k0shur/"));
                startActivity(intent);
            }
        });


       return v;
    }
}