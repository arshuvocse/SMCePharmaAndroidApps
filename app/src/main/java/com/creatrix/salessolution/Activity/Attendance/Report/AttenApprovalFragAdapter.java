package com.creatrix.salessolution.Activity.Attendance.Report;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AttenApprovalFragAdapter extends FragmentStateAdapter {
    Context context;

    public AttenApprovalFragAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context context) {
        super(fragmentManager, lifecycle);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
      //hide punchout
        if (position == 1) {
            return new PunchOutReportFragment();
        }
        // return new ReportsDcrActivity();
        return new PunchInReportFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
