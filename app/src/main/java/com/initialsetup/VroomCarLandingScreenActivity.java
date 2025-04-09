package com.initialsetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.ListRideAdapter;
import com.adapter.SearchedPlacesAdapter;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.modelclasses.FindRideResponse;
import com.network.ServiceClient;
import com.network.WebserviceBuilder;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.FirebaseUtils;
import com.utilities.Utilities;
import com.car.R;
import com.widgets.CircleTransform;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Balvant on 7/20/2015.
 */
public class VroomCarLandingScreenActivity extends Activity implements View.OnClickListener {

    private RecyclerView listSearchResults;
    private DrawerLayout drawerLayout;
    private ImageButton imgBtnMenu;
    private ImageView imgProfileImage, imgEditProfile;
    private RelativeLayout relMenu;
    private TextView tvFindRide, tvOfferRide,  tvNoRides, tvPlaces, tvProfileName, tvLogout,tvDistance;
    private Activity mActivity;
    private ProgressBar mProgressBar;
    private ArrayList<FindRideResponse.RideDetailsList> riderList;
    ListRideAdapter adapter;

   // public static boolean isFromMyRides=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialsetup_landing_activity);

        mActivity = VroomCarLandingScreenActivity.this;
        initialiseViews();
    }

    private void initialiseViews() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        listSearchResults = (RecyclerView) findViewById(R.id.list_routes);
        imgBtnMenu = (ImageButton) findViewById(R.id.imgbtn_left_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relMenu = (RelativeLayout) findViewById(R.id.rel_menu);



        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvNoRides = (TextView) findViewById(R.id.tv_no_rides);
        tvPlaces = (TextView) findViewById(R.id.tv_places);
        imgProfileImage = (ImageView) findViewById(R.id.img_profile);
        tvProfileName = (TextView) findViewById(R.id.tv_user_name);
        tvLogout = (TextView) findViewById(R.id.tv_Logout);
        tvFindRide = (TextView) findViewById(R.id.tv_find_ride);
        tvOfferRide = (TextView) findViewById(R.id.tv_offer_ride);


        imgEditProfile = (ImageView) findViewById(R.id.img_edit);

        tvDistance = (TextView) findViewById(R.id.tv_distance);

        Picasso.with(mActivity).load(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large")).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImage);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        tvProfileName.setText(strUserName);

        tvPlaces.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.SOURCE, "").toUpperCase() + " TO " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.DESTINATION, "").toUpperCase());



        tvLogout.setOnClickListener(this);
        tvOfferRide.setOnClickListener(this);
        tvFindRide.setOnClickListener(this);

        imgEditProfile.setOnClickListener(this);


//        listSearchResults.setAdapter(new SearchedPlacesAdapter(this, findRideResponse.rideList));

        imgBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(relMenu);
            }
        });


       /* listSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent mIntent = new Intent(VroomCarLandingScreenActivity.this, VroomViewDetailsActivity.class);
                    mIntent.putExtra(ApplicationConstatns.RideDetails.RIDER_OBJ, Utilities.riderObjToString(riderList.get(position)));
                    startActivity(mIntent);


            }
        });*/


        //callAsyncTaskForGettingFindRideResponse();
        getFindRideListReponse();

    }

    @Override
    protected void onResume() {
        super.onResume();
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        tvProfileName.setText(strUserName);

    }
    //************************************************************************************
    /**
     * get find ride response
     */
    private void getFindRideListReponse()
    {
        mProgressBar.setVisibility(View.VISIBLE);
       // final String userId = FirebaseUtils.getCurrentUserId();
        adapter=new ListRideAdapter(mActivity, FirebaseUtils.getRideRef().orderByChild("source").equalTo(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.SOURCE, "")));//DatabaseReference also can pass instead of query
        listSearchResults.setLayoutManager(new LinearLayoutManager(this));
        listSearchResults.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);
    }


    //*****************************************************************************



    @Override
    public void onClick(View v) {

        switch (v.getId()) {



            case R.id.tv_find_ride:

             //   isFromMyRides=false;
                goToSearchRideScreen();


                break;
            case R.id.tv_offer_ride:

              //  isFromMyRides=false;
                goToSearchRideScreen();

                break;




            case R.id.tv_Logout:

                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Utilities.clearSharedPreference(this);
                Utilities.saveDataToPreferences(this, ApplicationConstatns.LoginDetails.USER_ID, "");
                Intent logoutIntent = new Intent(this, VroomCarChooseOption.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                break;


            case R.id.img_edit:
//                Intent mEditProfileIntent = new Intent(this, VroomCarEditProfileActivity.class);
//                startActivity(mEditProfileIntent);
                //Intent mEditProfileIntent = new Intent(this, VroomCarUpdateProfileActivity.class);
                //startActivity(mEditProfileIntent);

                break;



        }
    }

    // *********************************************************************




    private void goToSearchRideScreen() {
        Intent mIntent = new Intent(mActivity, VroomCarSearchRideActivity.class);
        startActivity(mIntent);
    }




    public  void contactUs(){

    }
}
