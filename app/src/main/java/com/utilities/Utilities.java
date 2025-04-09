package com.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.modelclasses.FindRideResponse;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    static String md5 = "";

    private static final ArrayList<String> monthsArray = new ArrayList<String>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"));

    // ***********************************************************************************************************

    /**
     * Check if internet is present or not
     *
     * @param mContext
     * @return
     */
    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Logger.logger("Utilites", "Internet Connection Not Present");
            return false;
        }
    }

    // ***********************************************************************************************************
    /*
     * Function for showing Toast
	 */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // ***********************************************************************************************************

    /**
     * Function for validating the emailID
     *
     * @param
     */

    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    // ***********************************************************************************************************
    /*
     * Saving and Retrieving String data to shared preferences.
	 */
    public static void saveDataToPreferences(Context mContext, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        Logger.logger("Utilities", "Storing Data to Prefrence :" + value);
    }

    public static String getDataFromPreferences(Context mContext, String key, String defValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String data = preferences.getString(key, defValue);
        Logger.logger("Utilities", "Returned Data From Prefrence :" + data);
        return data;
    }

    // ****************************************************************************************

    /**
     * Get formated date
     */

    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDate(String str) throws ParseException {

        String dateString = str;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = null;

        try {
            convertedDate = dateFormat.parse(dateString);
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String intMonth = (String) android.text.format.DateFormat.format("MM", convertedDate); // 06
        String year = (String) android.text.format.DateFormat.format("yyyy", convertedDate); // 2013
        String day = (String) android.text.format.DateFormat.format("dd", convertedDate); // 20

        String strDate = day + "/" + intMonth + "/" + year;
        strDate = day + " " + monthsArray.get(Integer.valueOf(intMonth) - 1) + " " + year;
        // String strTime = hour + ":" + min + " " + am;

        // String createdDateTime = "<font color=#51cfe7>" + strDate +
        // "</font> ";

        return strDate;

    }

    public static Date convertStringToDate(String strDate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(strDate);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        return convertedDate;
    }
    // ****************************************************************************************

    public static String getTimeStamp() {
        long currentTimestamp = System.currentTimeMillis();
        String ts = Long.toString(currentTimestamp);
        String timestamp = ts;
        String key = "Sph!nxRock";
        String temp = timestamp + key;
        String md5 = getMd5For(temp);

        setMd5(md5);
        return ts;
    }

    public static String getMd5() {
        return md5;
    }

    public static void setMd5(String cmgmd5) {
        md5 = cmgmd5;
    }

    public static String getMd5For(String s) {

        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hexValue = Integer.toHexString(0xFF & messageDigest[i]);
                if (hexValue.length() == 1) {
                    hexValue = "0" + hexValue;
                }
                hexString.append(hexValue);
            }
            Log.v("", "MD5 for '" + s + "'  is: " + hexString);
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // ************************************************************************************
    /*
     * Setlist view height when listview used in scrollview
	 */

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // ******************************************************************************************

    public static String riderObjToString(FindRideResponse.RideDetailsList rideObj) {
        Type listType = new TypeToken<FindRideResponse.RideDetailsList>() {
        }.getType();
        String friendListAsString = null;
        try {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            friendListAsString = gson.toJson(rideObj, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return friendListAsString;
    }

    public static FindRideResponse.RideDetailsList stringToRiderObject(String jsonString) {
        Type listType = new TypeToken<FindRideResponse.RideDetailsList>() {
        }.getType();

        try {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            FindRideResponse.RideDetailsList responce = gson.fromJson(jsonString, listType);
            return responce;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public static void clearSharedPreference(Context mcontext)
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.apply();
    }


}
