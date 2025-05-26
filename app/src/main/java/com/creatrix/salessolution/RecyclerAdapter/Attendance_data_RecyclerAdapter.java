package com.creatrix.salessolution.RecyclerAdapter;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Model.Report_AttendanceViewModel;
import com.creatrix.salessolution.R;

import java.util.List;
import java.util.Locale;

public class Attendance_data_RecyclerAdapter extends RecyclerView.Adapter<Attendance_data_RecyclerAdapter.BookViewHolder> {
    private Context context;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    private List<Report_AttendanceViewModel> aList;
    int lastposition = -1;

    public Attendance_data_RecyclerAdapter(List<Report_AttendanceViewModel> nList) {
        this.aList = nList;
    }
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView attDate;
        public TextView attTime;
        public TextView attTxt;
        public TextView attType;
        public TextView attTimePunchOut;
        public BookViewHolder(View view) {
            super(view);
            attDate = (TextView) view.findViewById(R.id.attDate);
            attTime = (TextView) view.findViewById(R.id.attTime);
            attTxt = (TextView) view.findViewById(R.id.attTxt);
            attTimePunchOut = (TextView) view.findViewById(R.id.attTimePunchOut);
        }
    }

    @Override
    public Attendance_data_RecyclerAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zrv_attendance_report, parent, false);
        context = parent.getContext();
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Attendance_data_RecyclerAdapter.BookViewHolder holder, int position) {
        if (aList != null) {
            try {
                Report_AttendanceViewModel ratt = aList.get(position);
                holder.attDate.setText(ratt.getAttendanceDate());
                String addressD = getCompleteAddressString(Double.parseDouble(ratt.getPInLat()), Double.parseDouble(ratt.getPInLog()));
                holder.attTime.setText(ratt.getPunchInTime() + '[' + addressD + ']');
                if (ratt.getPunchOutTime() != null) {
                    try {
                        String addressDDD = getCompleteAddressString(Double.parseDouble(ratt.getPOutLat()), Double.parseDouble(ratt.getPOutLong()));

                        holder.attTimePunchOut.setText(ratt.getPunchOutTime() + '[' + addressDDD + ']');
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }


                } else {
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        //        if(aList.get(position).getAttendanceStatus() == "Approved"){
//            holder.attStatus.setTextColor(Color.parseColor("#00e676"));
//        }else{
//            holder.attStatus.setTextColor(Color.parseColor("#f05232"));
//        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        animation.setStartOffset(30 * position);//Provide delay here
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return aList.size();
    }
    private void setFadeAnimation(View view) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        view.startAnimation(animation);
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("address", strReturnedAddress.toString());
            } else {
                Log.w("address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("address", "Canont get Address!");
        }
        return strAdd;
    }

}
