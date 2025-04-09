package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.modelclasses.FindRideResponse;
import com.squareup.picasso.Picasso;
import com.car.R;
import com.widgets.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Balvant on 7/21/2015.
 */
public class SearchedPlacesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ViewHolder holder;
    private ArrayList<FindRideResponse.RideDetailsList> rideList;
    private Context mContext;

    public SearchedPlacesAdapter(Context mContext, ArrayList<FindRideResponse.RideDetailsList> rideList) {
        this.mContext = mContext;
        this.rideList = rideList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return rideList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_searched_results_layout, parent, false);
            holder = new ViewHolder();
            holder.imgUser = (ImageView) convertView.findViewById(R.id.img_profile);
            holder.tvHomePickUp = (TextView) convertView.findViewById(R.id.tv_home_pickup);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tvRoute = (TextView) convertView.findViewById(R.id.tv_route);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        FindRideResponse.RideDetailsList rideObj = rideList.get(position);

        if (rideObj.homepickup==true) {
            holder.tvHomePickUp.setVisibility(View.VISIBLE);
        } else {
            holder.tvHomePickUp.setVisibility(View.INVISIBLE);
        }


        if (rideObj.imageUrl != null && rideObj.imageUrl.length() > 0) {
            Picasso.with(mContext).load(rideObj.imageUrl).resize(100, 100).centerCrop().transform(new CircleTransform()).placeholder(R.drawable.image).into(holder.imgUser);
        } else {
            holder.imgUser.setImageResource(R.drawable.image);
        }

        if (rideObj.departureDateString != null && rideObj.departureDateString.length() > 0) {
            holder.tvDate.setText(rideObj.departureDateString.substring(0, rideObj.departureDateString.indexOf(" ")));
            holder.tvTime.setText(getFormatedTime(rideObj.departureDateString.substring(rideObj.departureDateString.indexOf(" "), rideObj.departureDateString.length())));
        } else {
            holder.tvDate.setText("");
            holder.tvTime.setText("");
        }

        if (rideObj.pricePerTraveller!=0){
            holder.tvPrice.setVisibility(View.VISIBLE);

            holder.tvPrice.setText("Price-"+ String.format("%.2f", rideObj.pricePerTraveller)+" INR");
        }else {
            holder.tvPrice.setVisibility(View.GONE);
        }
        holder.tvUserName.setText(rideObj.riderName);
        holder.tvRoute.setText(rideObj.source + " to " + rideObj.destination);

        return convertView;
    }

    public class ViewHolder {
        TextView tvHomePickUp, tvUserName, tvRoute, tvDate, tvTime ,tvPrice;
        ImageView imgUser;
        RelativeLayout relMainLayout;

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
