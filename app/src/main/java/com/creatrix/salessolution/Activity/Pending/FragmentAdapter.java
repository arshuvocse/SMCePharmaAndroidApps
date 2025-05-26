package com.creatrix.salessolution.Activity.Pending;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.creatrix.salessolution.Activity.Doctor.DCR.DcrLocalListFragment;
import com.creatrix.salessolution.Activity.OrderProcess.OrderCommercialListFragment;
import com.creatrix.salessolution.Activity.Doctor.Prescription.PrescriptionListFragment;
import com.creatrix.salessolution.Activity.SampleRequisition.OrderSampleListFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    Context context;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context context) {
        super(fragmentManager, lifecycle);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {

            case 1:
                return new PrescriptionListFragment();
          /*  case 2:
                return new OrderSampleListFragment();*/
            case 2:
                return new OrderCommercialListFragment();
        }
       // return new ReportsDcrActivity();
        return new DcrLocalListFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}