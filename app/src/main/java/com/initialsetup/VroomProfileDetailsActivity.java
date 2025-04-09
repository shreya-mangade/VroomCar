package com.initialsetup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leftmenu.LeftMenuListeners;
import com.modelclasses.UserProfileResponse;
import com.network.ServiceClient;
import com.network.WebserviceBuilder;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.Utilities;
import com.car.MyApplication;
import com.car.R;
import com.widgets.CircleTransform;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Balvant on 7/20/2015.
 */
public class VroomProfileDetailsActivity extends LeftMenuListeners implements View.OnClickListener {

    private ImageButton imgBtnEditProfile;
    private TextView tvContactNumber, tvAge, tvProfession, tvEmail, tvRidesOffered, tvStatus, tvName;
    private TextView tvContactUs,tvTerms,tvShare, tvProfileName, tvLogout,tvFindRide,tvOfferRide,tvInfo,tvVroomersInfo;

    private Activity mActivity;
    private ImageView imgBack, imgUserProfileImage, imgProfileImageBg,imgProfileImage,imgEditProfile;
    private ProgressBar mProgressBar;
    private DrawerLayout drawerLayout;
    private RelativeLayout relMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialsetup_profile_activity);

        mActivity = VroomProfileDetailsActivity.this;
        initialiseViews();
    }

    //**********************************************************************

    private void initialiseViews() {

        imgBtnEditProfile = (ImageButton) findViewById(R.id.imgbtn_edt_profile);
        imgBtnEditProfile.setOnClickListener(this);
        tvContactNumber = (TextView) findViewById(R.id.tv_contact_number);
        tvContactNumber.setOnClickListener(this);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvProfession = (TextView) findViewById(R.id.tv_profession);
        tvEmail = (TextView) findViewById(R.id.tv_email_address);
        tvRidesOffered = (TextView) findViewById(R.id.tv_rides_offered);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvVroomersInfo = (TextView) findViewById(R.id.tv_vroomer_info);
        tvStatus = (TextView) findViewById(R.id.tv_vroomers_status);
        tvName = (TextView) findViewById(R.id.tv_rider_name);
        imgBack = (ImageView) findViewById(R.id.img_back);

        imgUserProfileImage = (ImageView) findViewById(R.id.img_profile_user);
        imgProfileImageBg = (ImageView) findViewById(R.id.img_header);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        imgProfileImage = (ImageView) findViewById(R.id.img_profile);
        tvProfileName = (TextView) findViewById(R.id.tv_user_name);
        tvLogout = (TextView) findViewById(R.id.tv_Logout);
        tvFindRide = (TextView) findViewById(R.id.tv_find_ride);
        tvOfferRide = (TextView) findViewById(R.id.tv_offer_ride);
        tvShare = (TextView) findViewById(R.id.share_us);
        imgEditProfile = (ImageView) findViewById(R.id.img_edit);
        tvTerms = (TextView) findViewById(R.id.tv_terms_and_conditions);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relMenu = (RelativeLayout) findViewById(R.id.rel_menu);
        tvContactUs = (TextView) findViewById(R.id.tv_contactus);

        tvInfo.setText("?");
        tvVroomersInfo.setText("?");
        imgBack.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
        tvVroomersInfo.setOnClickListener(this);

        Picasso.with(mActivity).load(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large")).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImage);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        tvProfileName.setText(strUserName);

        initialiseListeners(tvFindRide,tvOfferRide,tvLogout,imgEditProfile);

        if (Utilities.isOnline(mActivity)){
            callAsyncTaskForGetProfileDetails();
        }

    }

    private void callAsyncTaskForGetProfileDetails() {
        mProgressBar.setVisibility(View.VISIBLE);
        WebserviceBuilder.GetAPICalls getAPICalls = ServiceClient.getInstance().getClient(mActivity, WebserviceBuilder.GetAPICalls.class);

        getAPICalls.getUserProfileResponse(getIntent().getStringExtra(ApplicationConstatns.LoginDetails.USER_ID), new Callback<UserProfileResponse>() {
            @Override
            public void success(UserProfileResponse userProfileResponse, Response response) {

                mProgressBar.setVisibility(View.GONE);
                if (userProfileResponse != null) {
                    checkStatusForProfileDetails(userProfileResponse);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    //******************************************************************************

    private void checkStatusForProfileDetails(UserProfileResponse userProfileResponse) {
        switch (userProfileResponse.statusCode) {

            case 200:
                tvContactNumber.setText("" + userProfileResponse.user.mobileNumber);
                tvEmail.setText("" + userProfileResponse.user.email);
                tvContactNumber.setText("" + userProfileResponse.user.mobileNumber);
                tvProfession.setText("" + userProfileResponse.user.profession);
                tvName.setText(userProfileResponse.user.firstname + " " + userProfileResponse.user.lastname);
                tvRidesOffered.setText("" + userProfileResponse.ridesOffered);
                tvStatus.setText("" + userProfileResponse.vroomerStatus);
                if (userProfileResponse.userAge!=0)
                tvAge.setText("" + userProfileResponse.userAge+" Years");

                if (userProfileResponse.user.imageUrl != null && userProfileResponse.user.imageUrl.length() > 0) {
                    Picasso.with(mActivity).load(userProfileResponse.user.imageUrl).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgUserProfileImage);
                    Picasso.with(mActivity).load(userProfileResponse.user.imageUrl).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImageBg);
                }

                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgbtn_edt_profile:
                Intent mIntent = new Intent(this, VroomCarEditProfileActivity.class);
                startActivity(mIntent);
                break;

            case R.id.tv_contact_number:

                makeCallToNumber();
                break;

            case R.id.img_back:
//                finish();
                drawerLayout.openDrawer(relMenu);
                break;

            case R.id.tv_find_ride:
                goToSearchRideScreen();
                break;
            case R.id.tv_offer_ride:
                goToSearchRideScreen();
                break;

            case R.id.tv_contactus:
                //contactUs();
                break;

            case R.id.tv_Logout:
                logoutFromVroomCar();
                break;

            case R.id.img_edit:
                goToEditProfileActivity();
                break;
            case R.id.share_us:

                break;

            case R.id.tv_terms_and_conditions:

                break;
            case R.id.tv_info:
                showInfoDialog();
                break;

            case R.id.tv_vroomer_info:
                showInfoDialog();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
    private void showInfoDialog() {
// ********************************************************************


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Add the buttons
            builder.setMessage("To know more about \"Vroomer's Ride Offered\", visit our Terms and Conditions.");
//            builder.setTitle("Rider Info");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
;

                }
            });



            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();

    }

    //*******************************************************************
    private void makeCallToNumber() {

      /*  Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + tvContactNumber.getText()));
        startActivity(intent);*/
    }
}
