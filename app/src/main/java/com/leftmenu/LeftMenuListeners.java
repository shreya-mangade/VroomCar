package com.leftmenu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.initialsetup.VroomCarChooseOption;
import com.initialsetup.VroomCarLandingScreenActivity;
import com.initialsetup.VroomCarLoginScreenActivity;
import com.initialsetup.VroomCarSearchRideActivity;
import com.initialsetup.VroomCarUpdateProfileActivity;
import com.utilities.ApplicationConstatns;
import com.utilities.Utilities;
import com.car.R;

/**
 * Created by Balvant on 8/23/2015.
 */
public class LeftMenuListeners extends Activity implements View.OnClickListener {

    private Activity mActivity;
    private TextView tvFindRide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = LeftMenuListeners.this;
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public void initialiseListeners(TextView tvFindRide, TextView tvOfferRide, TextView tvLogout, ImageView imgEditProfile) {


        this.tvFindRide = tvFindRide;
        this.tvFindRide.setOnClickListener(this);
        tvOfferRide.setOnClickListener(this);
      /*  tvContactUs.setOnClickListener(this);
        tvTerms.setOnClickListener(this);
        tvShare.setOnClickListener(this);*/
        tvLogout.setOnClickListener(this);
        imgEditProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


           /* case R.id.tv_find_ride:

                goToSearchRideScreen();

                break;
            case R.id.tv_offer_ride:

                goToSearchRideScreen();

                break;

            case R.id.tv_about_us:

                goToNextActivity(LeftMenuAboutUsActivity.ABOUT_US);

                break;
            case R.id.tv_careers:
                goToNextActivity(LeftMenuAboutUsActivity.CAREERS);

                break;
            case R.id.tv_why_vroomcar:
                goToNextActivity(LeftMenuAboutUsActivity.WHY_VROOMCAR);

                break;

            case R.id.tv_contactus:
                contactUs();
                break;

            case R.id.tv_Logout:

                logoutFromVroomCar();
                break;

            case R.id.rel_bottom_bar:
                goToCommentActivity();
                break;

            case R.id.img_edit:
                goToEditProfileActivity();
                break;
            case R.id.share_us:
                shareDialog();
                break;

            case R.id.tv_terms_and_conditions:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vroomcar.in/Terms&Conditions.html"));
                startActivity(browserIntent);
                break;*/

            case R.id.tv_Logout:

                logoutFromVroomCar();
                break;
        }
    }

    public void goToEditProfileActivity(){
        Intent mEditProfileIntent = new Intent(this, VroomCarUpdateProfileActivity.class);
        startActivity(mEditProfileIntent);
    }
    public void logoutFromVroomCar(){
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        Utilities.clearSharedPreference(this);
        Utilities.saveDataToPreferences(this, ApplicationConstatns.LoginDetails.USER_ID, "");
        Intent logoutIntent = new Intent(this, VroomCarChooseOption.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutIntent);
    }


    public void goToSearchRideScreen() {
        Intent mIntent = new Intent(mActivity, VroomCarSearchRideActivity.class);
        startActivity(mIntent);
    }
    public void goToLandingPageScreen()//call upon myride menu click
    {
        Intent mIntent = new Intent(mActivity, VroomCarLandingScreenActivity.class);
        startActivity(mIntent);
    }



}
