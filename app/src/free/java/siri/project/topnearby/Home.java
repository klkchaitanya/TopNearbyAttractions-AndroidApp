package siri.project.topnearby;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    @BindView(R.id.imgGplaces_home_main) ImageView imgGooglePlacesMain;
    @BindView(R.id.imgGplaces_home_bottom) ImageView imgGooglePlacesBottom;
    @BindView(R.id.adView) AdView mAdView;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);


     //Butterknife'd   ImageView imgGooglePlacesMain = (ImageView)view.findViewById(R.id.imgGplaces_home_main);
     //Butterknife'd   ImageView imgGooglePlacesBottom = (ImageView)view.findViewById(R.id.imgGplaces_home_bottom);
     //Butterknife'd   AdView mAdView = (AdView)view.findViewById(R.id.adView);

        Picasso.with(getContext()).load(R.drawable.topnearby_home_100kb).into(imgGooglePlacesMain);
        Picasso.with(getContext()).load(R.drawable.gplaces).into(imgGooglePlacesBottom);


        //Butterknife'd  AdView mAdView = (AdView) view.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return view;
    }

}
