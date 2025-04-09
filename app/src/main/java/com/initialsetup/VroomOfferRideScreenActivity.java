package com.initialsetup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leftmenu.LeftMenuListeners;
import com.modelclasses.FindRideResponse;
import com.modelclasses.OfferRideModelClass;
import com.modelclasses.OfferRideResponse;
import com.network.ServiceClient;
import com.network.WebserviceBuilder;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.FirebaseUtils;
import com.utilities.Utilities;
import com.car.MyApplication;
import com.car.R;
import com.widgets.CircleTransform;
import com.widgets.DirectionsJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Balvant on 7/20/2015.
 */
public class VroomOfferRideScreenActivity extends LeftMenuListeners implements View.OnClickListener {

    private Button mLoginButton, btnSubmit, btnReset;
    private ImageView imgBack, imgProfileImage, imgEditProfile;
    private TextView tvSeatsAvilable, tvInsuranceStatus, tvJourneyDate, tvJourneyTime, edtCarType, tvRoundDate, tvRoundTime;
    private TextView tvMyRide,tvContactUs, tvTerms, tvShare, tvProfileName, tvLogout, tvFindRide, tvOfferRide, tvHomePickUpInfo;
    private int selectedItem = 0, selectedInsuranceItem = 0, selectedCar = 0;
    private String[] seatsArray = {"01", "02", "03", "04", "05"};
    private String[] insuranceArray = {"Valid", "Not Valid"};
    private String[] carTypes = {"Audi", "Alto", "Amaze", "Bolero", "BMW", "Fiat", "Jaguar", "Land Rover", "Nissan", "Renault", "Swift", "Hyundai", "WagonR", "Verito", "Ford Figo", "Nano", "Zen", "Scorpio", "Volkswagen", "Honda City", "Innova", "Merchedese", "Omni", "Scoda", "Xylo", "Verna", "I10", "I20", "Duster", "Bike(2 Wheeler)", "Other"};
    private CheckBox mCheckBox, mCheckBoxHomePickup, checkBoxTermsAndConditions;
    private LinearLayout llRoundTrip;

    private Activity mActivity;
    private EditText edtSource, edtDestination, edtDescription, edtPrice;
    private String strRideid="",strFinalDate = "", strDate = "", strTime = "", strSeats = "", strCarType = "", strInsuranceStatus = "",strDateBeforeUpdate="";
    private ProgressBar mProgressBar;
    private TextView tvName, tvEmail;
    private boolean isRoundTrip = false;
    private DrawerLayout drawerLayout;
    private RelativeLayout relMenu;
    private boolean isHomePickup = false;
    private int distance = 0;
    private boolean isTermsAndConditionsSelected = false;
    private FindRideResponse.RideDetailsList riderObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialsetup_offerride_activity);
        mActivity = VroomOfferRideScreenActivity.this;
        initialiseView();
    }

    //*******************************************************************************

    /**
     * InitialiseViews
     */
    private void initialiseView() {

        tvSeatsAvilable = (TextView) findViewById(R.id.tv_seats_count);
        tvHomePickUpInfo = (TextView) findViewById(R.id.tv_home_piceup_info);
        mCheckBox = (CheckBox) findViewById(R.id.chkbox_round_trip);
        mCheckBoxHomePickup = (CheckBox) findViewById(R.id.chkbox_home_pickup);
        checkBoxTermsAndConditions = (CheckBox) findViewById(R.id.chkbox_terms_and_conditions);
        llRoundTrip = (LinearLayout) findViewById(R.id.ll_round_date);
        tvJourneyTime = (TextView) findViewById(R.id.tv_time);
        tvJourneyDate = (TextView) findViewById(R.id.tv_date_value);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        edtSource = (EditText) findViewById(R.id.tv_from_name);
        edtDestination = (EditText) findViewById(R.id.tv_to_name);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        edtPrice = (EditText) findViewById(R.id.edt_price);
        edtCarType = (TextView) findViewById(R.id.tv_car_name);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnReset = (Button) findViewById(R.id.btn_reset);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvInsuranceStatus = (TextView) findViewById(R.id.tv_insurance_name);
        tvRoundDate = (TextView) findViewById(R.id.tv_round_date_value);
        tvRoundTime = (TextView) findViewById(R.id.tv_round_time);

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
        tvHomePickUpInfo.setText("?");

        btnSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        tvHomePickUpInfo.setOnClickListener(this);
        edtCarType.setOnClickListener(this);
        tvInsuranceStatus.setOnClickListener(this);
        Picasso.with(mActivity).load(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large")).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImage);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        tvProfileName.setText(strUserName);



        initialiseListeners(tvFindRide, tvOfferRide, tvLogout, imgEditProfile);


        edtSource.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.SOURCE, "pune"));
        edtDestination.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.DESTINATION, "Mumbai"));


//        mCheckBox.setOnClickListener(this);
        tvSeatsAvilable.setOnClickListener(this);
        tvJourneyTime.setOnClickListener(this);
        tvJourneyDate.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        tvRoundTime.setOnClickListener(this);
        tvRoundDate.setOnClickListener(this);
//        mCheckBox.setChecked(false);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    llRoundTrip.setVisibility(View.VISIBLE);
                } else {
                    llRoundTrip.setVisibility(View.GONE);
                }
            }
        });

        mCheckBoxHomePickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isHomePickup = true;
                } else {
                    isHomePickup = false;
                }
            }
        });

        checkBoxTermsAndConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    isTermsAndConditionsSelected = true;
                } else {
                    isTermsAndConditionsSelected = false;
                }
            }
        });

        tvName.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, ""));
        tvEmail.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, ""));
       // setSaveValueTofield();to update my rides details
    }

    /*private void setSaveValueTofield()//to update my rides details
    {
          riderObj = Utilities.stringToRiderObject(getIntent().getStringExtra(ApplicationConstatns.RideDetails.RIDER_OBJ));
          //  tvHeaderText.setText(riderObj.source + " to " + riderObj.destination);
          edtSource.setText(riderObj.source);
          edtDestination.setText(riderObj.destination);

                if (riderObj.departureDateString != null && riderObj.departureDateString.length() > 0) {
                tvJourneyDate.setText(riderObj.departureDateString.substring(0, riderObj.departureDateString.indexOf(" ")));
                strDate=tvJourneyDate.getText().toString();
                tvJourneyTime.setText(getFormatedTime(riderObj.departureDateString.substring(riderObj.departureDateString.indexOf(" "), riderObj.departureDateString.length())));
                strTime=tvJourneyTime.getText().toString().replace(" am",":00").replace(" pm",":00");
                    strFinalDate = strDate + " " + strTime;

            } else {
                tvJourneyDate.setText("");
                tvJourneyTime.setText("");
            }
            edtCarType.setText(riderObj.carType);
            strCarType=riderObj.carType;
            tvSeatsAvilable.setText("0" + riderObj.seats);
            strSeats=riderObj.seats;
            //tvUserName.setText(riderObj.riderName);
            edtPrice.setText("" + riderObj.pricePerTraveller);
            edtDescription.setText(riderObj.desc);
            strRideid=riderObj.rideid;
           // tvInsuranceStatus.setText(riderObj.);
           btnSubmit.setText("Update");


    }*/
    private String getFormatedTime(String time) {
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(time);
            System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));
            return _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
    //*************************************************
    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.tv_seats_count:
                showSeatsDialog();
                break;
            case R.id.tv_insurance_name:
                showInsuranceDialog();
                break;

            case R.id.chkbox_round_trip:
                Toast.makeText(this, "Clicked " + mCheckBox.isChecked(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_time:
                isRoundTrip = false;
                showTimerPickerDialog();
                break;

            case R.id.tv_date_value:
                isRoundTrip = false;
                showDatePickerDialog();
                break;

            case R.id.tv_round_time:
                isRoundTrip = true;
                showTimerPickerDialog();
                break;

            case R.id.tv_round_date_value:
                isRoundTrip = true;
                showDatePickerDialog();
                break;

            case R.id.btn_submit:
                   checkValiadtionsForTheFields();
                break;

            case R.id.btn_reset:
                   resetRideDate();
                break;

            case R.id.tv_car_name:
                showCarTypeDialog();
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
            case R.id.tv_home_piceup_info:
                showInfoDialog();
                break;
        }
    }


    private void showInfoDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setMessage("If you wish to offer your co-traveler home pickup, select this.\n" +
                "Home pickup option fetch you more price per co-traveler.");
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


    private void showCarTypeDialog() {


        if (seatsArray != null && seatsArray.length > 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Select Car:");
            alert.setSingleChoiceItems(carTypes, selectedCar, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    selectedCar = item;
                    dialog.cancel();
                    if (carTypes[selectedCar].equalsIgnoreCase("Other")) {
                        openAlertDialog();
                    } else {

                        strCarType = carTypes[selectedCar];
                        edtCarType.setText(strCarType);
                    }
                }
            });


            AlertDialog ad = alert.create();
            ad.show();
        }


    }

    private void resetRideDate() {
        strFinalDate = "";
        strDate = "";
        strTime = "";
        strSeats = "";
        strCarType = "";
        tvSeatsAvilable.setText("00");
        tvInsuranceStatus.setText("Insurance Status");
        tvJourneyTime.setText("00:00:00");
        tvJourneyDate.setText("00/00/0000");
        tvRoundTime.setText("00:00:00");
        tvRoundDate.setText("00/00/0000");
        edtCarType.setText("CAR MODEL");
        edtDescription.setText("");
        edtDescription.setHint("Description");
        edtPrice.setText("");
        edtPrice.setHint("PRICE");
//        checkBoxTermsAndConditions.setSelected(false);


    }

    //***************************************************************************

    private void checkValiadtionsForTheFields() {

        if (edtSource.getText().toString().length() > 0) {

            if (edtDestination.getText().toString().length() > 0) {

                if (strDate.length() > 0) {

                    if (strTime.length() > 0) {
                        strFinalDate = strDate + " " + strTime;
                        strDateBeforeUpdate=strFinalDate;////set previous journey date and new journey date should have 24 hrs diff before update.

                        if (strCarType.length() > 0) {


                            if (strSeats.length() > 0) {
                                if (edtPrice.getText().toString().length() > 0) {
                                    if (edtDescription.getText().toString().length() > 0) {

                                        if (isTermsAndConditionsSelected) {

                                            if (strInsuranceStatus.length() > 0) {

                                               if(btnSubmit.getText().toString().equals("Submit".toUpperCase()))
                                                //callSaveRideDetailsApi();
                                                   writeRideDetails();
                                              /*  else {
                                                    if(updateValid_before24hr())

                                                   callUpdateRideDetailsApi();
                                                }*/

                                            } else {
                                                Utilities.showToast(mActivity, "Please select the insurance status.");
                                            }

                                        } else {
                                            Utilities.showToast(mActivity, "Please accept the terms and conditions");
                                        }

                                    } else {
                                        Utilities.showToast(mActivity, "Please enter the travel description.");
                                    }
                                } else {
                                    Utilities.showToast(mActivity, "Please enter the price.");
                                }
                            } else {
                                Utilities.showToast(mActivity, "Please select the available seats.");
                            }


                        } else {
                            Utilities.showToast(mActivity, "Please select the car type.");
                        }

                    } else {
                        Utilities.showToast(mActivity, "Please select the time.");
                    }

                } else {
                    Utilities.showToast(mActivity, "Please select the date.");
                }
            } else {
                Utilities.showToast(mActivity, "Please select the destination.");
            }

        } else {
            Utilities.showToast(mActivity, "Please select the source.");
        }
    }
    private void writeRideDetails()
    {
        String key=FirebaseUtils.getRideRef().push().getKey();
     //   final String userId = FirebaseUtils.getCurrentUserId();
        mProgressBar.setVisibility(View.VISIBLE);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        OfferRideModelClass offerRideModelClass=new OfferRideModelClass(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, ""), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.SOURCE, "pune"), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.DESTINATION, "Mumbai"), Double.valueOf(edtPrice.getText().toString()), strFinalDate, "22", strUserName, strCarType, edtDescription.getText().toString(), strSeats, Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large"), isHomePickup, distance);
        Map<String,Object> rideValues=offerRideModelClass.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/rides/" + key, rideValues);
        childUpdates.put("/user-rides/" + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, "") + "/" + key, rideValues);
        FirebaseUtils.getBaseRef().updateChildren(childUpdates);
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this,"Ride saved successfully",Toast.LENGTH_SHORT).show();

    }
/*
/*/
/****************************************************************
    private  void callUpdateRideDetailsApi()
    {

        mProgressBar.setVisibility(View.VISIBLE);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        WebserviceBuilder.InitialSetUpAPIs initialSetUpAPIs = ServiceClient.getInstance().getClient(this, WebserviceBuilder.InitialSetUpAPIs.class);
        initialSetUpAPIs.updateUserRide(new OfferRideModelClass(strRideid,Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, ""), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.SOURCE, "pune"), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.DESTINATION, "Mumbai"), edtPrice.getText().toString(), strFinalDate, "22", strUserName, strCarType, edtDescription.getText().toString(), strSeats, Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large"), isHomePickup, distance), new Callback<OfferRideResponse>() {
            @Override
            public void success(OfferRideResponse offerRideResponse, Response response) {

                mProgressBar.setVisibility(View.GONE);
                if (offerRideResponse != null) {
                   checkStatusForOfferRideResponse(offerRideResponse, "Update");
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mProgressBar.setVisibility(View.GONE);

                Utilities.showToast(VroomOfferRideScreenActivity.this, "retrofitError ++" + retrofitError.getMessage());
            }
        });
    }
*/
    //***********************************************************

   /* private void callSaveRideDetailsApi() {

        mProgressBar.setVisibility(View.VISIBLE);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        WebserviceBuilder.InitialSetUpAPIs initialSetUpAPIs = ServiceClient.getInstance().getClient(this, WebserviceBuilder.InitialSetUpAPIs.class);
        initialSetUpAPIs.getLoginDetailsResponse(new OfferRideModelClass(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, ""), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.SOURCE, "pune"), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.RideDetails.DESTINATION, "Mumbai"), edtPrice.getText().toString(), strFinalDate, "22", strUserName, strCarType, edtDescription.getText().toString(), strSeats, Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large"), isHomePickup, distance), new Callback<OfferRideResponse>() {
            @Override
            public void success(OfferRideResponse offerRideResponse, Response response) {

                mProgressBar.setVisibility(View.GONE);
                if (offerRideResponse != null) {
                    checkStatusForOfferRideResponse(offerRideResponse,"Save");
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mProgressBar.setVisibility(View.GONE);
                Utilities.showToast(VroomOfferRideScreenActivity.this, "retrofitError ++" + retrofitError.getMessage());
            }
        });
    }
*/


  /*  private void checkStatusForOfferRideResponse(OfferRideResponse offerRideResponse,String action) {
        switch (offerRideResponse.statusCode) {

            case 201:
                if(action.equals("Save")) {
                    Utilities.showToast(mActivity, "Your ride offered successfully.");
                }else
                {
                    Utilities.showToast(mActivity,"Your ride updated successfully.");
                }
//                Utilities.showToast(mActivity, offerRideResponse.massage);
//                Intent mIntent = new Intent(this, VroomCarLandingScreenActivity.class);
//                startActivity(mIntent);
                finish();
                break;

            default:
                Utilities.showToast(mActivity, offerRideResponse.massage);
                break;
        }

    }

    /*//*************************************************************************/


    private void showSeatsDialog() {

        if (seatsArray != null && seatsArray.length > 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Seats Available:");
            alert.setSingleChoiceItems(seatsArray, selectedItem, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    selectedItem = item;
                    strSeats = seatsArray[selectedItem];
                    tvSeatsAvilable.setText(strSeats);
                    dialog.cancel();
                }
            });

//            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int id) {
//
////                    strCurrencyId = listCurrencyid.get(selectedItem);
////                    selectedCurrency = selectedItem;
//
////                    mTvCurrency.setText("Currency:$" + currencyArray[selectedItem]);
//                }
//            });
//
//            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });

            AlertDialog ad = alert.create();
            ad.show();
        }

    }

    private void openAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                strCarType = userInput.getText().toString();
                                edtCarType.setText(userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void showInsuranceDialog() {

        if (seatsArray != null && seatsArray.length > 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Vehicle Insurance Status");
            alert.setSingleChoiceItems(insuranceArray, selectedInsuranceItem, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    selectedInsuranceItem = item;
//                    strSeats = insuranceArray[selectedItem];
                    tvInsuranceStatus.setText(insuranceArray[selectedInsuranceItem]);
                    strInsuranceStatus = insuranceArray[selectedInsuranceItem];
                    dialog.cancel();
                }
            });


            AlertDialog ad = alert.create();
            ad.show();
        }

    }


    // ************************************************************************************



    private boolean updateValid_before24hr()
    {
        Calendar today=Calendar.getInstance();

        if(strDateBeforeUpdate!=null) {
            Log.i("Date before converting",""+strDateBeforeUpdate);
         Date journeyDatebeforeUpdate = Utilities.convertStringToDate(strDateBeforeUpdate);
            Log.i("updatevalid date object",""+journeyDatebeforeUpdate);
            Calendar journeyDate=Calendar.getInstance();
            journeyDate.setTime(journeyDatebeforeUpdate);
            Log.i("calendar obje",""+today.getTime());
         long calDaysDiff = journeyDate.getTimeInMillis()-today.getTimeInMillis();
         long diffHours = calDaysDiff / (60 * 60 * 1000);
         int diffDays = (int) (calDaysDiff / (24 * 60 * 60 * 1000));
            Log.i("DiffHours",""+diffHours);
            Log.i("DiffDays",""+diffDays);

         if (diffHours > 24) {
           //  Toast.makeText(VroomOfferRideScreenActivity.this, "Diffhours" + diffHours + "diffDays" + diffDays, Toast.LENGTH_SHORT).show();
             return true;
         } else {
             Toast.makeText(VroomOfferRideScreenActivity.this, "You can`t update journey details before 24 hours", Toast.LENGTH_SHORT).show();
             return false;
         }
     }

return false;
    }

    private void showTimerPickerDialog() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(VroomOfferRideScreenActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                if (isRoundTrip) {
                    tvRoundTime.setText(selectedHour + ":" + selectedMinute);
                } else {
                    String strHours = "0";
                    String strMinuts = "0";
                    if (selectedHour < 10) {
                        strHours = strHours + selectedHour;
                    } else {
                        strHours = String.valueOf(selectedHour);
                    }

                    if (selectedMinute < 10) {
                        strMinuts = strMinuts + selectedMinute;
                    } else {
                        strMinuts = String.valueOf(selectedMinute);
                    }
                    tvJourneyTime.setText(strHours + ":" + strMinuts);
                    strTime = strHours + ":" + strMinuts + ":00";

                }
//                ((AddDealActivity) mActivity).setTime(position, isStartDate, selectedHour + ":" + selectedMinute);selectedMinute

            }
        }, hour, minute, true);// Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


    private void showDatePickerDialog() {

        final Calendar c = Calendar.getInstance();
        final int yearToday = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mTimePicker;
        mTimePicker = new DatePickerDialog(VroomOfferRideScreenActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

//                ((AddDealActivity) mActivity).setDate(position, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                if (year > yearToday) {

                    setDateToView(dayOfMonth, monthOfYear, year);
//                    tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                } else if (monthOfYear > month && year == yearToday) {
                    setDateToView(dayOfMonth, monthOfYear, year);
//                    tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                } else if (dayOfMonth >= day && year == yearToday && monthOfYear == month) {
                    setDateToView(dayOfMonth, monthOfYear, year);
//                    tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                } else {
                    Utilities.showToast(mActivity, "Please select future date.");
                }


//                if (isRoundTrip) {
//                    tvRoundDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                } else {
//                    tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//
//                }

//                Logger.logger("DAte *** " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }

        }, yearToday, month, day);

        mTimePicker.setTitle("Select Date");
//		mTimePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        mTimePicker.show();

    }


    private void setDateToView(int dayOfMonth, int monthOfYear, int year) {

        if (dayOfMonth < 10 && (monthOfYear + 1) < 10) {
            tvJourneyDate.setText("0" + dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
            strDate = "0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
        } else if (dayOfMonth < 10) {
            tvJourneyDate.setText("0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            strDate = "0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        } else if ((monthOfYear + 1) < 10) {
            tvJourneyDate.setText(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
            strDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
        } else {
            tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
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
