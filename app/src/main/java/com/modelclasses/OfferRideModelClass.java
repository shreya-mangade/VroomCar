package com.modelclasses;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balvant on 8/8/2015.
 */
@IgnoreExtraProperties
public class OfferRideModelClass {

    public String source;
    public String destination;
    public double pricePerTraveller;
    public String departureDateString;
    public String riderAge;
    public String riderName;
    public String carType;
    public String desc;
    public String seats;
    public String userId;
    public String imageUrl;
    public boolean homepickup;
    private int distanceTravelled;


    public OfferRideModelClass()
    {
        // Default constructor required for calls to DataSnapshot.getValue(OrderDetails.class)
    }
    public OfferRideModelClass(String userId, String source, String destination, double pricePerTraveller, String departureDate, String riderAge, String riderName, String carType, String desc, String seats, String imageUrl,boolean homepickup,int distanceTravelled) {
        this.source = source;
        this.destination = destination;
        this.pricePerTraveller = pricePerTraveller;
        this.departureDateString = departureDate;
        this.riderAge = riderAge;
        this.riderName = riderName;
        this.carType = carType;
        this.desc = desc;
        this.seats = seats;
        this.userId= userId;
        this.imageUrl = imageUrl;
        this.homepickup = homepickup;
        this.distanceTravelled = distanceTravelled;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("source", source);
        result.put("destination", destination);
        result.put("pricePerTraveller", pricePerTraveller);
        result.put("departureDateString", departureDateString);
        result.put("riderAge", riderAge);
        result.put("riderName", riderName);
        result.put("carType", carType);
        result.put("desc", desc);
        result.put("seats", seats);
        result.put("imageUrl", imageUrl);
        result.put("homepickup", homepickup);
        result.put("distanceTravelled", distanceTravelled);



        //result.put("orderID",orderID);

        return result;
    }
   /* //with ride Id for update
    public OfferRideModelClass(String rideid,String userId, String source, String destination, String pricePerTraveller, String departureDate, String riderAge, String riderName, String carType, String desc, String seats, String imageUrl,boolean homepickup,int distanceTravelled) {
        this.source = source;
        this.destination = destination;
        this.pricePerTraveller = pricePerTraveller;
        this.departureDateString = departureDate;
        this.riderAge = riderAge;
        this.riderName = riderName;
        this.carType = carType;
        this.desc = desc;
        this.seats = seats;
        this.userId= userId;
        this.imageUrl = imageUrl;
        this.homepickup = homepickup;
        this.distanceTravelled = distanceTravelled;
        this.rideid=rideid;
    }*/
}


