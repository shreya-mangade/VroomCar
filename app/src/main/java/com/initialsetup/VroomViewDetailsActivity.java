package com.initialsetup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.leftmenu.LeftMenuListeners;
import com.modelclasses.CarNumberResponse;
import com.modelclasses.FindRideResponse;
import com.modelclasses.OfferRideModelClass;
import com.network.ServiceClient;
import com.network.WebserviceBuilder;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.Utilities;
import com.car.MyApplication;
import com.car.R;
import com.widgets.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Balvant on 7/20/2015.
 */
public class VroomViewDetailsActivity extends LeftMenuListeners implements View.OnClickListener {

    private Button btnViewProfile, btnBookRide;
    private TextView tvUserName, tvDate, tvTime, tvVehicleType, tvSeats, tvHeaderText, tvCarNumber;
    private TextView tvContactUs, tvTerms, tvShare, tvProfileName, tvLogout, tvFindRide, tvOfferRide, tvPriceValue, tvDescription;
    private ImageView imgUserImage, imgHeader, imgProfileImage, imgEditProfile;
    private OfferRideModelClass riderObj;
    private ImageView imgBack;
    private DrawerLayout drawerLayout;
    private RelativeLayout relMenu;
    private Activity mActivity;
    private CheckBox checkBoxTermsAndConditions;
    private boolean isTermsAndConditionsSelected = false;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialsetup_details_activity);

        mActivity = VroomViewDetailsActivity.this;
        initialiseViews();

        if (Utilities.isOnline(mActivity)) {
            callApiForGettingTheCarNumber();
        }
    }

    private void callApiForGettingTheCarNumber() {

        WebserviceBuilder.GetAPICalls mGetAPICalls = ServiceClient.getInstance().getClient(mActivity, WebserviceBuilder.GetAPICalls.class);

        mGetAPICalls.getUserCarNumber(String.valueOf(riderObj.userId), new Callback<CarNumberResponse>() {
            @Override
            public void success(CarNumberResponse carNumberResponse, Response response) {

                if (carNumberResponse != null) {

                    checkStatusForCarNumber(carNumberResponse);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private void checkStatusForCarNumber(CarNumberResponse carNumberResponse) {

        switch (carNumberResponse.statusCode) {

            case 200:
                tvCarNumber.setText(carNumberResponse.carNumber);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    //************************************************************************

    private void initialiseViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnViewProfile = (Button) findViewById(R.id.btn_contact);
        btnBookRide = (Button) findViewById(R.id.btn_bookRide);
        tvUserName = (TextView) findViewById(R.id.tv_rider_name);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvCarNumber = (TextView) findViewById(R.id.tv_car_number);
        tvVehicleType = (TextView) findViewById(R.id.tv_vehicle_type);
        tvSeats = (TextView) findViewById(R.id.tv_seats_available);
        tvPriceValue = (TextView) findViewById(R.id.tv_price_value);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        imgUserImage = (ImageView) findViewById(R.id.img_profile_user);
        imgHeader = (ImageView) findViewById(R.id.img_header);
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvHeaderText = (TextView) findViewById(R.id.tv_header_text);

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
        checkBoxTermsAndConditions = (CheckBox) findViewById(R.id.chkbox_terms_and_conditions);


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

        Picasso.with(mActivity).load(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large")).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImage);
        String strUserName = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "") + " " + Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, "");
        tvProfileName.setText(strUserName);

        initialiseListeners(tvFindRide, tvOfferRide, tvLogout, imgEditProfile);

        imgBack.setOnClickListener(this);
        btnViewProfile.setOnClickListener(this);
        btnBookRide.setOnClickListener(this);

       // riderObj = Utilities.stringToRiderObject(getIntent().getStringExtra(ApplicationConstatns.RideDetails.RIDER_OBJ));
        String riderParse = getIntent().getStringExtra(ApplicationConstatns.RideDetails.RIDER_OBJ);
        Gson gS = new Gson();
        riderObj= gS.fromJson(riderParse, OfferRideModelClass.class);

        tvHeaderText.setText(riderObj.source + " to " + riderObj.destination);
        if (riderObj.imageUrl != null && riderObj.imageUrl.length() > 0) {
            Picasso.with(this).load(riderObj.imageUrl).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgUserImage);
//            Picasso.with(this).load(riderObj.imageUrl).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.profile_bg).into(imgHeader);

        } else {
            imgUserImage.setImageResource(R.drawable.image);
        }


        if (riderObj.departureDateString != null && riderObj.departureDateString.length() > 0) {
            tvDate.setText(riderObj.departureDateString.substring(0, riderObj.departureDateString.indexOf(" ")));
            tvTime.setText(getFormatedTime(riderObj.departureDateString.substring(riderObj.departureDateString.indexOf(" "), riderObj.departureDateString.length())));
        } else {
            tvDate.setText("");
            tvTime.setText("");
        }
        tvVehicleType.setText(riderObj.carType);
        tvSeats.setText("0" + riderObj.seats);
        tvUserName.setText(riderObj.riderName);
        tvPriceValue.setText("" + riderObj.pricePerTraveller);
        tvDescription.setText(riderObj.desc);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_contact:

                if (isTermsAndConditionsSelected) {

                    Intent mIntent = new Intent(this, VroomProfileDetailsActivity.class);
                    mIntent.putExtra(ApplicationConstatns.LoginDetails.USER_ID, String.valueOf(riderObj.userId));
                    startActivity(mIntent);
                } else {
                    Utilities.showToast(mActivity, "Please accept the terms and conditions");
                }

                break;
            case R.id.btn_bookRide:
                if (isTermsAndConditionsSelected) {
                    //callPaymentGatewayAPI();
                }
                else {
                    Utilities.showToast(mActivity, "Please accept the terms and conditions");
                }
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
           /* case R.id.tv_date:
                break;
            case R.id.tv_time:
                break;*/

        }
    }

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
    /*private void showDatePickerDialog() {

        final Calendar c = Calendar.getInstance();
        final int yearToday = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mTimePicker;
        mTimePicker = new DatePickerDialog(VroomViewDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (year > yearToday) {

                    setDateToView(dayOfMonth, monthOfYear, year);

                } else if (monthOfYear > month && year == yearToday) {
                    setDateToView(dayOfMonth, monthOfYear, year);

                } else if (dayOfMonth >= day && year == yearToday && monthOfYear == month) {
                    setDateToView(dayOfMonth, monthOfYear, year);

                } else {
                    Utilities.showToast(mActivity, "Please select future date.");
                }
            }

        }, yearToday, month, day);

        mTimePicker.setTitle("Select Date");
//		mTimePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        mTimePicker.show();

    }
    private void setDateToView(int dayOfMonth, int monthOfYear, int year) {

        if (dayOfMonth < 10 && (monthOfYear + 1) < 10) {
            tvDate.setText("0" + dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
            strDate = "0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
        } else if (dayOfMonth < 10) {
            tvDate.setText("0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            strDate = "0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        } else if ((monthOfYear + 1) < 10) {
            tvDate.setText(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
            strDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year;
        } else {
            tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        }

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

    }*/

    /* private void callApiUpdateRideDetails()
     {
         mProgressBar.setVisibility(View.VISIBLE);


     }*/

    /*private void callPaymentGatewayAPI() {

        Intent intent=new Intent(this,PaymentGateway.class);
        intent.putExtra(ApplicationConstatns.RideDetails.PRICE_PER_TRAVELLER,tvPriceValue.getText().toString());
        mActivity.startActivity(intent);
    }
*/

    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
