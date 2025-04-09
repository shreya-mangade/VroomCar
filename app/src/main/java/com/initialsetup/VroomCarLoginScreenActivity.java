package com.initialsetup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.modelclasses.UpdateUserModelClass;

import com.utilities.ApplicationConstatns;
import com.utilities.FirebaseUtils;
import com.utilities.Utilities;
import com.car.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;


/**
 * Created by Balvant on 7/20/2015.
 */
public class VroomCarLoginScreenActivity extends Activity implements View.OnClickListener {

    public CallbackManager callbackManager;
    private ImageButton mLoginButton;
    private Activity mActivity;
 //   private String fbUserLastName, fbUserFirstName, fbUserEmail = "", fbUserProfilePics = "", fbGender = "", dateOfBirth = "", fbUserId = "";
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String TAG = "LOGINACTIVITY";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mContext=VroomCarLoginScreenActivity.this;
        setContentView(R.layout.initialsetup_loginscreen_activity);

        initialseViews();
        firebaseAuthListener();//initialise firebase auth listener -facebook
        generateKeyHash();

    }

    private void initialseViews() {


        mActivity = VroomCarLoginScreenActivity.this;
        mLoginButton = (ImageButton) findViewById(R.id.btn_login);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mLoginButton.setOnClickListener(this);

    }

    //*************************************************
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login:

              //  checkFacebookLogin();//after web service hosting it will work properly
                getFacebookAuthLogin();

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


    }
    // *******************************************************************************
    /*
     * Function for generating KeyHash for facebook.
	 */

    private void generateKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.car",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash: ************* ", Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
                Log.d("KeyHash: ************", Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
    private void firebaseAuthListener() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // AuthStateListener that responds to changes in the user's sign-in state
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

            //    mProgressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    onAuthSuccess(user);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                   /* if(FirebaseAuth.getInstance()!=null) {
                        Logger.ToastMsg(mContext, "FirebaseLogin failed");
                        FirebaseAuth.getInstance().signOut();
                    }*/
                }
                // ...
            }
        };
    }
    private void onAuthSuccess(FirebaseUser user) {
      //  String username = usernameFromEmail(user.getEmail());
        // Write new user
        String username[]=user.getDisplayName().split(" ");

        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, user.getEmail());
        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, username[0]);
        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, username[1]);
        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, user.getPhotoUrl().toString());
        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, user.getUid());


        writeNewUser(user.getUid(), username[0], username[1], user.getEmail());
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    private void writeNewUser(String userId, String fName,String lName, String email) {
        UpdateUserModelClass user= new UpdateUserModelClass(userId,fName, lName, email);

        FirebaseUtils.getUserRef().child(userId).setValue(user);
        onSuccessLogin(email);

    }

    public void onSuccessLogin(String email) {

           /* Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
           if(!email.equals("")) {
               if (getIntent().getBooleanExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false)) {
                   Intent mIntentOffer = new Intent(this, VroomOfferRideScreenActivity.class);
                   mIntentOffer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(mIntentOffer);
                   finish();
               } else {
                   Intent mIntent = new Intent(this, VroomCarLandingScreenActivity.class);
//          mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(mIntent);
                   finish();
               }
           }else
           {
               Intent mIntent = new Intent(mActivity, VroomCarUpdateInfoActivity.class);
               mIntent.putExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, getIntent().getBooleanExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false));
               startActivity(mIntent);
           }

    }
    private void getFacebookAuthLogin() {
        // Initialize Facebook Login button

        callbackManager = CallbackManager.Factory.create();
        //LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile,email,user_birthday"));//set permission like public_profile,email
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                //If network connection lose when trying to login facebook before reaching home screen need to log out...or update Ui with sign out button
                if(LoginManager.getInstance()!=null)
                {
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();
                    Utilities.clearSharedPreference(VroomCarLoginScreenActivity.this);
                }

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                Utilities.clearSharedPreference(VroomCarLoginScreenActivity.this);
            }
        });


    }

    // ************************************************************

  /*  *//**
     * Facebook login code.
     *//*
    private void checkFacebookLogin() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("email");
        LoginManager.getInstance().logInWithReadPermissions(this, list);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult result) {
                Toast.makeText(mActivity, "Login Successful", Toast.LENGTH_LONG).show();

                GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                            try {

                                String jsonresult = String.valueOf(json);
                                System.out.println("JSON Result" + jsonresult);
//                                {"id":"936398019732571","name":"Manoj Nimbalkar","email":"md5558@gmail.com","gender":"male","birthday":"06\/09\/1990","first_name":"Manoj","last_name":"Nimbalkar"}


                                Logger.logger("Email *** " + json.getString("email"));
                                Logger.logger("Id *** " + json.getString("id"));
                                Logger.logger("First Name *** " + json.getString("first_name"));
                                Logger.logger("Last Name *** " + json.getString("last_name"));


                                if (json.has("email")) {
//                                    json.getString("email");
                                    fbUserEmail = json.getString("email");
                                } else {
                                    Utilities.showToast(mActivity, "Email not found");
                                }

//                                fbUserEmail = "abcjghdh@gmail.com";
                                if (json.has("gender")) {
                                    fbGender = json.getString("gender");
                                }

                                if (json.has("birthday")) {
                                    dateOfBirth = json.getString("birthday");
                                }

                                fbUserId = json.getString("id");
                                fbUserFirstName = json.getString("first_name");
                                fbUserLastName = json.getString("last_name");

                                fbUserProfilePics = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";


                                Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, fbUserEmail);
                                Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, fbUserFirstName);
                                Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, fbUserLastName);
                                Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_PROFILE_PIC, fbUserProfilePics);
                                Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_DOB, dateOfBirth);
                                Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_GENDER, fbGender);

//                                verifyUser(fbUserId, fbUserFirstName, fbUserLastName, fbUserEmail, fbUserProfilePics);
//                                callApiForFacebookLogin(fbUserId, fbUserFirstName, fbUserLastName, fbUserEmail, fbUserProfilePics);

                                if (!fbUserEmail.equalsIgnoreCase("")) {
                                    verifyUser();


                                } else {
                                    Intent mIntent = new Intent(mActivity, VroomCarUpdateInfoActivity.class);
                                    mIntent.putExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, getIntent().getBooleanExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false));
//                                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(mIntent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,first_name,last_name,address");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }*/

   /* private void verifyUser() {

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
    }*/

   /* private void checkStatusForVeriFyUser(UserProfileResponse userProfileResponse) {

        switch (userProfileResponse.statusCode) {

            case 200:

//                Utilities.showToast(mActivity,"Inside verify user");
                if (userProfileResponse.user.email != null) {

                    if (userProfileResponse.userId != 0) {
                        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_ID, String.valueOf(userProfileResponse.userId));
                        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_EMAIL, userProfileResponse.user.email);
                        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_FIRST_NAME, userProfileResponse.user.firstname);
                        Utilities.saveDataToPreferences(mActivity, ApplicationConstatns.LoginDetails.USER_LAST_NAME, userProfileResponse.user.lastname);

                    }
                    if (getIntent().getBooleanExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false)) {
                        Intent mIntentOffer = new Intent(this, VroomOfferRideScreenActivity.class);
//                        mIntentOffer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntentOffer);
                        finish();
                    } else {
                        Intent mIntent = new Intent(this, VroomCarLandingScreenActivity.class);
//                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent);
                        finish();
                    }
                } else {
                    Intent mIntent = new Intent(mActivity, VroomCarUpdateInfoActivity.class);
                    mIntent.putExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, getIntent().getBooleanExtra(ApplicationConstatns.IS_FROM_OFFER_RIDE, false));
                    startActivity(mIntent);
                }

                break;
        }
    }*/


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        mProgressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }


                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null)
            mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
