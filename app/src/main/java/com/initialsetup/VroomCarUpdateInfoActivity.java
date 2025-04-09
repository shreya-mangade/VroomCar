package com.initialsetup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.modelclasses.OfferRideResponse;
import com.modelclasses.SaveUserModelClass;
import com.modelclasses.UserProfileResponse;
import com.network.ServiceClient;
import com.network.WebserviceBuilder;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.Utilities;
import com.car.R;
import com.widgets.CircleTransform;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Balvant on 8/15/2015.
 */
public class VroomCarUpdateInfoActivity extends Activity implements View.OnClickListener {


    private EditText edtName, edtLastName, edtEmail, edtContactNumber, edtAddress, edtGender, edtProfession, edtPostalCode, edtCarName, edtCarPurchase, edtCarNumber;
    private ProgressBar mProgressBar;
    private Activity mActivity;
    private TextView tvSaveDetails, edtDateOfBirth;
    private ImageView imgBack;
    private String strDate = "";
    private ImageView imgProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialsetup__update_profile_layout);
        mActivity = VroomCarUpdateInfoActivity.this;
        iniialiseViews();
    }

    private void iniialiseViews() {

        edtName = (EditText) findViewById(R.id.edt_name);
        edtLastName = (EditText) findViewById(R.id.edt_last_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtContactNumber = (EditText) findViewById(R.id.edt_contact_number);
        edtAddress = (EditText) findViewById(R.id.edt_message);
        edtGender = (EditText) findViewById(R.id.edt_gender);
        edtCarName = (EditText) findViewById(R.id.edt_car_name);
        edtCarNumber = (EditText) findViewById(R.id.edt_car_number);
        edtCarPurchase = (EditText) findViewById(R.id.edt_purchase_year);
        edtProfession = (EditText) findViewById(R.id.edt_profession);
        edtPostalCode = (EditText) findViewById(R.id.edt_postalcode);
        edtDateOfBirth = (TextView) findViewById(R.id.edt_birth_date);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvSaveDetails = (TextView) findViewById(R.id.tv_save_details);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgProfileImage = (ImageView) findViewById(R.id.img_profile);

        edtCarNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        imgBack.setOnClickListener(this);
        tvSaveDetails.setOnClickListener(this);
        edtDateOfBirth.setOnClickListener(this);

        Picasso.with(mActivity).load(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, "http://graph.facebook.com/100000771470028/picture?type=large")).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImage);
        if (!Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, "").equalsIgnoreCase("")) {
            edtName.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, ""));
            edtLastName.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, ""));
        }

        if (!Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, "").equalsIgnoreCase("")) {
            edtEmail.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, ""));
        }

        if (!Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_GENDER, "").equalsIgnoreCase("")) {
            edtGender.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_GENDER, ""));
        }

        if (!Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_DOB, "").equalsIgnoreCase("")) {
            edtDateOfBirth.setText(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_DOB, "").replace("/", "-"));

            strDate = Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_DOB, "").replace("/", "-") + " 00:00:00";
        }


//        checkValidationForFields();
    }

    private void checkValidationForFields() {
        if (edtName.getText().toString().length() > 0) {

            if (edtEmail.getText().toString().length() > 0) {

                if (edtContactNumber.getText().toString().length() > 0) {

                    if (edtAddress.getText().toString().length() > 0) {

                        if (strDate.length() > 0) {

                            if (edtGender.getText().toString().length() > 0) {

                                if (edtProfession.getText().toString().length() > 0) {


                                    if (edtCarName.getText().toString().length() > 0) {

                                        if (edtCarPurchase.getText().toString().length() > 3) {

                                            if (edtCarNumber.getText().toString().length() > 5) {

                                                if (edtPostalCode.getText().toString().length() > 0) {

                                                    verifyUser(edtEmail.getText().toString());

                                                } else {
                                                    Utilities.showToast(mActivity, "Please enter your postal code");
                                                }
                                            } else {
                                                Utilities.showToast(mActivity, "Please enter valid car number");
                                            }
                                        } else {
                                            Utilities.showToast(mActivity, "Please enter the valid car purchase year");
                                        }

                                    } else {
                                        Utilities.showToast(mActivity, "Please enter your car model");
                                    }


                                } else {
                                    Utilities.showToast(mActivity, "Please enter your profession");
                                }
                            } else {
                                Utilities.showToast(mActivity, "Please enter your gender");
                            }
                        } else {
                            Utilities.showToast(mActivity, "Please enter your date of birth");
                        }
                    } else {
                        Utilities.showToast(mActivity, "Please enter your address");
                    }
                } else {
                    Utilities.showToast(mActivity, "Please enter your contact number");
                }

            } else {
                Utilities.showToast(mActivity, "Please enter your email");
            }


        } else {
            Utilities.showToast(mActivity, "Please enter your name");
        }
    }


    private void verifyUser(String fbUserEmail) {

        mProgressBar.setVisibility(View.VISIBLE);
        WebserviceBuilder.GetAPICalls mGetAPICalls = ServiceClient.getInstance().getClient(mActivity, WebserviceBuilder.GetAPICalls.class);

        mGetAPICalls.verifyUserProfileResponse(fbUserEmail, new Callback<UserProfileResponse>() {
            @Override
            public void success(UserProfileResponse userProfileResponse, Response response) {
                mProgressBar.setVisibility(View.GONE);
                if (userProfileResponse != null) {
                    checkStatusForVeriFyUser(userProfileResponse);
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void checkStatusForVeriFyUser(UserProfileResponse userProfileResponse) {

        switch (userProfileResponse.statusCode) {

            case 200:

                if (userProfileResponse.user.email != null) {

                    if (userProfileResponse.userId != 0) {
                        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, String.valueOf(userProfileResponse.userId));
                    }
                    if (getIntent().getBooleanExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false)) {
                        Intent mIntentOffer = new Intent(this, VroomOfferRideScreenActivity.class);
                        mIntentOffer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntentOffer);
                    } else {
                        Intent mIntent = new Intent(this, VroomCarLandingScreenActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent);
                    }

                } else {
                    callApiForFacebookLogin(edtName.getText().toString(), edtLastName.getText().toString());
                }

                break;
        }
    }


    //***********************************************************************************************

    private void callApiForFacebookLogin(String fbUserFirstName, String fbUserLastName) {

        mProgressBar.setVisibility(View.VISIBLE);
        WebserviceBuilder.InitialSetUpAPIs mInitialSetUpAPIs = ServiceClient.getInstance().getClient(mActivity, WebserviceBuilder.InitialSetUpAPIs.class);

        mInitialSetUpAPIs.getSaveUserResponse(new SaveUserModelClass(fbUserFirstName, fbUserLastName, edtProfession.getText().toString(), edtGender.getText().toString(), strDate, edtEmail.getText().toString(), edtAddress.getText().toString(), edtPostalCode.getText().toString(), Long.valueOf(edtContactNumber.getText().toString()), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, ""), edtCarName.getText().toString(), edtCarNumber.getText().toString(), edtCarPurchase.getText().toString()), new Callback<OfferRideResponse>() {
            @Override
            public void success(OfferRideResponse offerRideResponse, Response response) {
                mProgressBar.setVisibility(View.GONE);

                if (offerRideResponse != null) {
                    checkStatusForSaveUser(offerRideResponse);
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mProgressBar.setVisibility(View.GONE);
                Utilities.showToast(mActivity, "Error - " + retrofitError.getMessage());
            }
        });

    }

    private void checkStatusForSaveUser(OfferRideResponse offerRideResponse) {

        switch (offerRideResponse.statusCode) {
            case 201:
                Utilities.showToast(mActivity, "User Saved Successfully");
                if (offerRideResponse.userId != 0) {
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, String.valueOf(offerRideResponse.userId));
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ADDRESS, edtAddress.getText().toString());
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFFESSION, edtProfession.getText().toString());
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_POSTALCODE, edtPostalCode.getText().toString());
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, edtName.getText().toString());
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, edtLastName.getText().toString());
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_MOBILE, edtContactNumber.getText().toString());
                }

//                Utilities.showToast(mActivity, "Inside verify user ** " + offerRideResponse.userId);
                if (getIntent().getBooleanExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false)) {
                    Intent mIntentOffer = new Intent(this, VroomOfferRideScreenActivity.class);
                    mIntentOffer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntentOffer);
//                    finish();
                } else {
                    Intent mIntent = new Intent(this, VroomCarLandingScreenActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
//                    finish();
                }


                break;
            case 304:
                Utilities.showToast(mActivity,offerRideResponse.massage);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_save_details:
                checkValidationForFields();
                break;

            case R.id.img_back:
                finish();
                break;
            case R.id.edt_birth_date:
                showDatePickerDialog();
                break;
        }
    }


    private void showDatePickerDialog() {

        final Calendar c = Calendar.getInstance();
        final int yearToday = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mTimePicker;
        mTimePicker = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

//                ((AddDealActivity) mActivity).setDate(position, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                edtDateOfBirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                strDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year + " 00:00:00";

                if (year > yearToday) {

                    Utilities.showToast(mActivity, "Please select previous date.");
//                    tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                } else if (monthOfYear > month && year == yearToday) {
                    Utilities.showToast(mActivity, "Please select previous date.");
                } else if (dayOfMonth >= day && year == yearToday && monthOfYear == month) {
                    Utilities.showToast(mActivity, "Please select previous date.");
                } else {

                    setDateToView(dayOfMonth, monthOfYear, year);
                }
            }

        }, yearToday, month, day);

        mTimePicker.setTitle("Select Date");
//		mTimePicker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        mTimePicker.show();

    }

    private void setDateToView(int dayOfMonth, int monthOfYear, int year) {

        if (dayOfMonth < 10 && (monthOfYear + 1) < 10) {
            edtDateOfBirth.setText("0" + dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
            strDate = "0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year + " 00:00:00";
        } else if (dayOfMonth < 10) {
            edtDateOfBirth.setText("0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            strDate = "0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " 00:00:00";
        } else if ((monthOfYear + 1) < 10) {
            edtDateOfBirth.setText(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
            strDate = dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year + " 00:00:00";
        } else {
            edtDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " 00:00:00";
        }

    }
}
