package com.initialsetup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.leftmenu.LeftMenuListeners;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.Utilities;
import com.car.R;
import com.widgets.CircleTransform;


/**
 * Created by Balvant on 7/20/2015.
 */
public class VroomCarSearchRideActivity extends LeftMenuListeners implements View.OnClickListener {

    private static final int SEARCH_RIDE = 1;
    private static final int OFFER_RIDE = 2;

    private Button btnSearchRide, btnOfferRide;
    private AutoCompleteTextView edtSource, edtDestination;


    private RelativeLayout relMenu;
    private DrawerLayout drawerLayout;
    private Activity mActivity;

    private ImageView  imgProfileImage, imgEditProfile,imgMenuHome;
    private TextView tvContactUs, tvTerms, tvShare, tvProfileName, tvLogout, tvFindRide, tvOfferRide, tvHomePickUpInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialsetup_search_ride_activity);
        mActivity = VroomCarSearchRideActivity.this;
        initialiseViews();
    }


    //***********************************************************************************

    private void initialiseViews() {
        btnSearchRide = (Button) findViewById(R.id.btn_find_ride);
        btnOfferRide = (Button) findViewById(R.id.btn_offer_ride);
        edtSource = (AutoCompleteTextView) findViewById(R.id.edt_from);
        edtDestination = (AutoCompleteTextView) findViewById(R.id.edt_to);


        //menu item
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relMenu=(RelativeLayout)findViewById(R.id.rel_menu);
        imgProfileImage = (ImageView) findViewById(R.id.img_profile);
        imgMenuHome=(ImageView)findViewById(R.id.img_back);

        tvProfileName = (TextView) findViewById(R.id.tv_user_name);
        tvLogout = (TextView) findViewById(R.id.tv_Logout);
        tvFindRide = (TextView) findViewById(R.id.tv_find_ride);
        tvOfferRide = (TextView) findViewById(R.id.tv_offer_ride);
        tvShare = (TextView) findViewById(R.id.share_us);
        imgEditProfile = (ImageView) findViewById(R.id.img_edit);
        tvTerms = (TextView) findViewById(R.id.tv_terms_and_conditions);
        tvContactUs = (TextView) findViewById(R.id.tv_contactus);

        //listener
        Picasso.with(mActivity).load(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large")).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImage);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        tvProfileName.setText(strUserName);


        //end menu

        btnOfferRide.setOnClickListener(this);
        btnSearchRide.setOnClickListener(this);

        imgMenuHome.setOnClickListener(this);

        initialiseListeners(tvFindRide, tvOfferRide,tvLogout, imgEditProfile);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnSearchRide.getWindowToken(), 0);


    }




    // ******************************************************************************


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
                case R.id.btn_find_ride:
                checkValidationsAndGoToNextActivity(SEARCH_RIDE);
                   break;
            case R.id.btn_offer_ride:
                checkValidationsAndGoToNextActivity(OFFER_RIDE);
                break;
            case R.id.rel_bottom_bar:


//                Intent mEditProfileIntent = new Intent(this, VroomCarEditProfileActivity.class);
//                startActivity(mEditProfileIntent);

                break;
            case R.id.img_back:
//                finish();
                drawerLayout.openDrawer(relMenu);
                break;

            //menu item click
            case R.id.tv_find_ride:

                goToSearchRideScreen();
                break;
            case R.id.tv_offer_ride:

                goToSearchRideScreen();
                break;


            case R.id.tv_Logout:
                logoutFromVroomCar();
                break;

            case R.id.img_edit:
                goToEditProfileActivity();
                break;



        }
    }


    // *********************************************************************



    //*********************************************************************************

    /**
     * Check validation and go to next activity
     *
     * @param type
     */
    private void checkValidationsAndGoToNextActivity(int type) {


        if (edtSource.getText().toString().length() > 0) {

            if (edtDestination.getText().toString().length() > 0) {

                Utilities.saveDataToPreferences(this, ApplicationConstatns.RideDetails.SOURCE, edtSource.getText().toString());
                Utilities.saveDataToPreferences(this, ApplicationConstatns.RideDetails.DESTINATION, edtDestination.getText().toString());


                switch (type) {

                    case SEARCH_RIDE:
                        Intent mIntent;
                        if (!Utilities.getDataFromPreferences(this, ApplicationConstatns.LoginDetails.USER_ID, "").equalsIgnoreCase("")) {
                            mIntent = new Intent(this, VroomCarLandingScreenActivity.class);
                        } else {
                            mIntent = new Intent(this, VroomCarLoginScreenActivity.class);
                            mIntent.putExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false);
                        }


                        startActivity(mIntent);
                        break;

                    case OFFER_RIDE:

                        Intent mIntentOffer;
                        if (!Utilities.getDataFromPreferences(this, ApplicationConstatns.LoginDetails.USER_ID, "").equalsIgnoreCase("")) {
                            mIntentOffer = new Intent(this, VroomOfferRideScreenActivity.class);
                        } else {
                            mIntentOffer = new Intent(this, VroomCarLoginScreenActivity.class);
                            mIntentOffer.putExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, true);
                        }
                        startActivity(mIntentOffer);
                        break;
                }

            } else {
                Utilities.showToast(this, "Please enter the Destination");
            }

        } else {
            Utilities.showToast(this, "Please enter the Source");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
