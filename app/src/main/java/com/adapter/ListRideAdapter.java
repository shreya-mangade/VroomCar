package com.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.car.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.initialsetup.VroomCarLandingScreenActivity;
import com.initialsetup.VroomViewDetailsActivity;
import com.modelclasses.FindRideResponse;
import com.modelclasses.OfferRideModelClass;
import com.squareup.picasso.Picasso;
import com.utilities.ApplicationConstatns;
import com.utilities.Utilities;
import com.widgets.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pranita Patil on 04/04/2017.
 */

public class ListRideAdapter extends RecyclerView.Adapter<PastRideViewHolder> {

    public List<String> mRidesIds=new ArrayList<>();
    private List<OfferRideModelClass> mRideList = new ArrayList<>();
    private Query mDatabaseReference;
    Context mContext;
    String TAG="ListRideAdapter";
    private ChildEventListener mChildEventListener;
    ProgressDialog dialog;
    public ListRideAdapter(final Context context,Query ref)
    {
        mContext=context;
        mDatabaseReference=ref;
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Loading...");
        dialog.show();
        ChildEventListener childEventListener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                // A new comment has been added, add it to the displayed list
                OfferRideModelClass offerRide = dataSnapshot.getValue(OfferRideModelClass.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mRidesIds.add(dataSnapshot.getKey());
                mRideList.add(offerRide);
                notifyItemInserted(mRideList.size() - 1);
                dialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        };

        ref.addChildEventListener(childEventListener);
        mChildEventListener=childEventListener;
    }
    @Override
    public PastRideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.row_searched_results_layout,parent,false);
        return new PastRideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PastRideViewHolder holder, int position) {
        final OfferRideModelClass offerRide=mRideList.get(position);

        if (offerRide.homepickup==true) {
            holder.tvHomePickUp.setVisibility(View.VISIBLE);
        } else {
            holder.tvHomePickUp.setVisibility(View.INVISIBLE);
        }


        if (offerRide.imageUrl != null && offerRide.imageUrl.length() > 0) {
            Picasso.with(mContext).load(offerRide.imageUrl).resize(100, 100).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(holder.imgUser);
        } else {
            holder.imgUser.setImageResource(R.drawable.image);
        }

        if (offerRide.departureDateString != null && offerRide.departureDateString.length() > 0) {
            holder.tvDate.setText(offerRide.departureDateString.substring(0, offerRide.departureDateString.indexOf(" ")));
            holder.tvTime.setText(getFormatedTime(offerRide.departureDateString.substring(offerRide.departureDateString.indexOf(" "), offerRide.departureDateString.length())));
        } else {
            holder.tvDate.setText("");
            holder.tvTime.setText("");
        }

        if (offerRide.pricePerTraveller!=0){
            holder.tvPrice.setVisibility(View.VISIBLE);

            holder.tvPrice.setText("Price-"+ String.format("%.2f", offerRide.pricePerTraveller)+" INR");
        }else {
            holder.tvPrice.setVisibility(View.GONE);
        }
        holder.tvUserName.setText(offerRide.riderName);
        holder.tvRoute.setText(offerRide.source + " to " + offerRide.destination);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, VroomViewDetailsActivity.class);
                Gson gS = new Gson();
                mIntent.putExtra(ApplicationConstatns.RideDetails.RIDER_OBJ, gS.toJson(offerRide));
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRideList.size();
    }

    private String getFormatedTime(String time) {
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(time);
            System.out.println(_24HourDt);
            System.out.println(_12HourSDF.format(_24HourDt));
            return _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
class PastRideViewHolder extends RecyclerView.ViewHolder
{

    TextView tvHomePickUp, tvUserName, tvRoute, tvDate, tvTime ,tvPrice;
    ImageView imgUser;


    public PastRideViewHolder(View itemView) {
        super(itemView);
        imgUser = (ImageView) itemView.findViewById(R.id.img_profile);
        tvHomePickUp = (TextView) itemView.findViewById(R.id.tv_home_pickup);
        tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
        tvRoute = (TextView) itemView.findViewById(R.id.tv_route);
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvPrice = (TextView) itemView.findViewById(R.id.tv_price);

    }
}