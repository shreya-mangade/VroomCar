package com.network;

import com.modelclasses.CarNumberResponse;
import com.modelclasses.ContactUsModelClass;
import com.modelclasses.ContactUsResponse;
import com.modelclasses.FindRideResponse;
import com.modelclasses.OfferRideModelClass;
import com.modelclasses.OfferRideResponse;
import com.modelclasses.SaveUserModelClass;
import com.modelclasses.UpdateUserModelClass;
import com.modelclasses.UserProfileResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public class WebserviceBuilder {

    public interface GetAPICalls {
        //        @GET("/category.php/")
//        void getCategoryListResponse(Callback<CategoryListModelClass> callback);
//
        @GET("/appRide/findride/{source}/{destination}")
        void getFindRideResponse(@Path("source") String source, @Path("destination") String destination, Callback<FindRideResponse> callback);

        @GET("/appRide/findprofileById/{id}")
        void getUserProfileResponse(@Path("id") String id, Callback<UserProfileResponse> callback);

        @GET("/appRide/findprofileByEmailId/{email}")
        void verifyUserProfileResponse(@Path("email") String email, Callback<UserProfileResponse> callback);

        @GET("/appRide/findCarNumberByUserId/{userid}")
        void getUserCarNumber(@Path("userid") String userid, Callback<CarNumberResponse> callback);

        @GET("/appRide/findrideByUser/{id}")
        void getMyRideResponse(@Path("id") String id,Callback<FindRideResponse> callback);

    }


    // *******************************************

    public interface InitialSetUpAPIs {

        @POST("/appRide/updateride")
        void updateUserRide(@Body OfferRideModelClass mLoginUpdate,Callback<OfferRideResponse> callback );


        @POST("/appRide/saveRide/")
        void getLoginDetailsResponse(@Body OfferRideModelClass mLoginRequest, Callback<OfferRideResponse> callback);

        @POST("/appRide/saveuser/")
        void getSaveUserResponse(@Body SaveUserModelClass mSaveUser, Callback<OfferRideResponse> callback);

        @POST("/appRide/updateuser/")
        void updateUserResponse(@Body UpdateUserModelClass mSaveUser, Callback<UserProfileResponse> callback);





    }

}
