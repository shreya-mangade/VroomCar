package com.utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DELL-PC on 06/01/2017.
 */

public class FirebaseUtils {
    public static DatabaseReference getBaseRef()
    {
        return FirebaseDatabase.getInstance().getReference("vroomcarstudent");
    }

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static DatabaseReference getUserRef() {
        return getBaseRef().child("users");
    }

    public static DatabaseReference getRideRef() {
        return getBaseRef().child("rides");
    }
    public static DatabaseReference getAddressRef() {
        return getBaseRef().child("address");
    }

    public static DatabaseReference getUser_RideRef() {
        return getBaseRef().child("user-rides");
    }





}
