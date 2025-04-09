package com.modelclasses;

/**
 * Created by Balvant on 8/13/2015.
 */
public class SaveUserModelClass {

    public String firstname;
    public String lastname;
    public String profession;
    public String gender;
    public String dateofbirthString;
    public String email;
    public String address;
    public String postalCode;
    public String imageUrl;
    public long mobileNumber;
    public String carNumber;
    public String carName;
    public String carYears;

    public SaveUserModelClass(String firstname, String lastname, String profession, String gender, String dateOfBirth, String email, String address, String postalCode, long mobileNumber,String imageUrl,String carName,String carNumber,String carYears) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.profession = profession;
        this.gender = gender;
        this.dateofbirthString = dateOfBirth;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.mobileNumber = mobileNumber;
        this.imageUrl = imageUrl;
        this.carNumber = carNumber;
        this.carName = carName;
        this.carYears = carYears;
    }


}
