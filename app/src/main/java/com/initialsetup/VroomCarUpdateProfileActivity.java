package com.initialsetup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.modelclasses.UpdateUserModelClass;
import com.modelclasses.UserProfileResponse;
import com.network.ServiceClient;
import com.network.WebserviceBuilder;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.Utilities;
import com.car.MyApplication;
import com.car.R;
import com.widgets.CircleTransform;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Balvant on 8/15/2015.
 */
public class VroomCarUpdateProfileActivity extends Activity implements View.OnClickListener {


    private EditText edtName, edtLastName, edtEmail, edtContactNumber, edtAddress, edtGender, edtProfession, edtPostalCode, edtCarName, edtCarPurchase, edtCarNumber;
    ;
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
        mActivity = VroomCarUpdateProfileActivity.this;
        iniialiseViews();
    }

    private void iniialiseViews() {

        edtName = (EditText) findViewById(R.id.edt_name);
        edtLastName = (EditText) findViewById(R.id.edt_last_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtContactNumber = (EditText) findViewById(R.id.edt_contact_number);
        edtAddress = (EditText) findViewById(R.id.edt_message);
        edtGender = (EditText) findViewById(R.id.edt_gender);
        edtProfession = (EditText) findViewById(R.id.edt_profession);
        edtCarName = (EditText) findViewById(R.id.edt_car_name);
        edtCarNumber = (EditText) findViewById(R.id.edt_car_number);
        edtCarPurchase = (EditText) findViewById(R.id.edt_purchase_year);
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

        edtDateOfBirth.setEnabled(false);
        edtEmail.setEnabled(false);
        edtGender.setEnabled(false);
        if (Utilities.isOnline(mActivity)) {
            callAsyncTaskForGetProfileDetails();
        }
//        checkValidationForFields();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtProfession.getWindowToken(), 0);

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

                                                    callApiupdateProfile(edtName.getText().toString(), edtLastName.getText().toString());
                                                } else {
                                                    Utilities.showToast(mActivity, "Please enter your postal code");
                                                }
                                            } else {
                                                Utilities.showToast(mActivity, "Please enter valid car number");
                                            }
                                        } else {
                                            Utilities.showToast(mActivity, "Please enter the car purchase year");
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

    private void callAsyncTaskForGetProfileDetails() {
        mProgressBar.setVisibility(View.VISIBLE);

        WebserviceBuilder.GetAPICalls getAPICalls = ServiceClient.getInstance().getClient(mActivity, WebserviceBuilder.GetAPICalls.class);

        getAPICalls.verifyUserProfileResponse(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, ""), new Callback<UserProfileResponse>() {
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
                }

        );

    }

    //******************************************************************************

    private void checkStatusForProfileDetails(UserProfileResponse userProfileResponse) {
        switch (userProfileResponse.statusCode) {

            case 200:
                edtContactNumber.setText("" + userProfileResponse.user.mobileNumber);
                edtEmail.setText("" + userProfileResponse.user.email);
                edtProfession.setText("" + userProfileResponse.user.profession);
                edtName.setText(userProfileResponse.user.firstname);
                edtLastName.setText(userProfileResponse.user.lastname);
                edtAddress.setText(userProfileResponse.user.address);
                edtGender.setText(userProfileResponse.user.gender);


                if (userProfileResponse.user.carName != null) {
                    edtCarName.setText(userProfileResponse.user.carName);
                }
                if (userProfileResponse.user.carNumber != null) {
                    edtCarNumber.setText(userProfileResponse.user.carNumber);
                }
                if (userProfileResponse.user.carYears != null) {
                    edtCarPurchase.setText(userProfileResponse.user.carYears);
                }
                if (userProfileResponse.user.dateofbirthString != null) {

                    if (userProfileResponse.user.dateofbirthString.contains(" ")) {
                        edtDateOfBirth.setText(userProfileResponse.user.dateofbirthString.substring(0, userProfileResponse.user.dateofbirthString.indexOf(" ")));
                    } else {
                        edtDateOfBirth.setText(userProfileResponse.user.dateofbirthString);
                    }
                    strDate = edtDateOfBirth.getText().toString().replace("/", "-") + " 00:00:00";
                }


                edtPostalCode.setText(userProfileResponse.user.postalCode);

                if (userProfileResponse.user.imageUrl != null && userProfileResponse.user.imageUrl.length() > 0) {
                    Picasso.with(mActivity).load(userProfileResponse.user.imageUrl).resize(200, 200).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(imgProfileImage);
                }

                break;


        }

    }


    //***********************************************************************************************

    private void callApiupdateProfile(String fbUserFirstName, String fbUserLastName) {

        mProgressBar.setVisibility(View.VISIBLE);
        strDate = edtDateOfBirth.getText().toString().replace("/", "-") + " 00:00:00";
        WebserviceBuilder.InitialSetUpAPIs mInitialSetUpAPIs = ServiceClient.getInstance().getClient(mActivity, WebserviceBuilder.InitialSetUpAPIs.class);

        mInitialSetUpAPIs.updateUserResponse(new UpdateUserModelClass(Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, ""), fbUserFirstName, fbUserLastName, edtProfession.getText().toString(), edtGender.getText().toString(), strDate, edtEmail.getText().toString(), edtAddress.getText().toString(), edtPostalCode.getText().toString(), Long.valueOf(edtContactNumber.getText().toString()), Utilities.getDataFromPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, ""), edtCarName.getText().toString(), edtCarNumber.getText().toString(), edtCarPurchase.getText().toString()), new Callback<UserProfileResponse>() {
            @Override
            public void success(UserProfileResponse offerRideResponse, Response response) {
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

    private void checkStatusForSaveUser(UserProfileResponse offerRideResponse) {

        switch (offerRideResponse.statusCode) {
            case 200:

                if (offerRideResponse.userId != 0) {
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, String.valueOf(offerRideResponse.userId));
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, edtName.getText().toString());
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, edtLastName.getText().toString());
                    Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, edtEmail.getText().toString());
                    Utilities.saveDataToPreferences(mActivity,ApplicationConstatns.LoginDetails.USER_MOBILE,edtContactNumber.getText().toString());
                }
                Utilities.showToast(mActivity, offerRideResponse.massage);
                finish();
                break;

            default:
                Utilities.showToast(mActivity, offerRideResponse.massage);
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
//                showDatePickerDialog();
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
//                    tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                } else if (dayOfMonth >= day && year == yearToday && monthOfYear == month) {
                    Utilities.showToast(mActivity, "Please select previous date.");
//                    tvJourneyDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                } else {

                    setDateToView(dayOfMonth,monthOfYear,year);
                }


//                Logger.logger("DAte *** " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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

    @Override
    protected void onResume() {
        super.onResume();

          }
}
