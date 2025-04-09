package com.modelclasses;

/**
 * Created by Balvant on 8/15/2015.
 */
public class UserProfileResponse {

    public String massage;
    public int statusCode;
    public int userId;
    public int ridesOffered;
    public UserDetails user;
    public String vroomerStatus;
    public int userAge;

    public class UserDetails {
        public String address;
        public String email;
        public String firstname;
        public String gender;
        public String lastname;
        public String postalCode;
        public String profession;
        public int userid;
        public long mobileNumber;
        public String imageUrl;
        public String dateofbirthString;
        public String carNumber;
        public String carName;
        public String carYears;

    }

}
