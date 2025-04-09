package com.modelclasses;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balvant on 8/10/2015.
 */
public class FindRideResponse {

    public int statusCode;
    public String massage;
    public ArrayList<RideDetailsList> rideList;

    public class RideDetailsList {

        public String carType;
        public String desc;
        public String destination;
        public double pricePerTraveller;
        public int riderAge;
        public String riderName;
        public String source;
        public String trip;
        public int distanceTravelled;
        public int userId;
        public boolean homepickup;
        public String seats;
        public String imageUrl;
        public String departureDateString;
        public String rideid;

    }
}
