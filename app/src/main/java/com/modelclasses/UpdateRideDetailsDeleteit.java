package com.modelclasses;

/**
 * Created by DELL-PC on 20/10/2015.
 */
public class UpdateRideDetailsDeleteit {
    public String userId;
    public long mobileNumber;
    public String source;
    public String destination;
    public String departureDateString;
    public String carNumber;
    public String carName;
    public String seats;
    public String pricePerTraveller;
    public String desc;
    public UpdateRideDetailsDeleteit(String userId, long mobileNumber, String source, String destination, String departureDateString, String carNumber, String carName, String seats, String pricePerTraveller, String desc)
    {
        this.userId=userId;
        this.mobileNumber=mobileNumber;
        this.source=source;
        this.destination=destination;
        this.departureDateString=departureDateString;
        this.carNumber=carNumber;
        this.carName=carName;
        this.seats=seats;
        this.pricePerTraveller=pricePerTraveller;
        this.desc=desc;

    }

}
