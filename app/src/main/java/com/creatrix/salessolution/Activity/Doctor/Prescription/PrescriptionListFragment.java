package com.creatrix.salessolution.Activity.Doctor.Prescription;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._prescriptionList_Recycler;
import com.creatrix.salessolution.Services.ConstValue;
import com.creatrix.salessolution.Services.Constants;
import com.creatrix.salessolution.databinding.FragmentPrescriptionListBinding;

import java.util.List;

public class PrescriptionListFragment extends Fragment {

FragmentPrescriptionListBinding binding;
    private _prescriptionList_Recycler mAdapter;
    DBDoctorHelper dbDoctorHelper;
    DBCrudHelper dbCrudHelper;
    public PrescriptionListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentPrescriptionListBinding.inflate(getLayoutInflater());
        //View v= inflater.inflate(binding.getRoot(), container, false);
        dbCrudHelper=new DBCrudHelper(getActivity());
        LoadRecyclerView(dbCrudHelper.GetPrescriptionInfoFromDB(),"Local");
        return binding.getRoot();
    }

    public void LoadRecyclerView(List<PrescriptionSM> aList, String type) {

        if (aList != null) {
            mAdapter = new _prescriptionList_Recycler(aList,"PrescriptionListFragment");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            binding.rvOfflinepresc.setLayoutManager(mLayoutManager);
            binding.rvOfflinepresc.setItemAnimator(new DefaultItemAnimator());
            binding.rvOfflinepresc.setAdapter(mAdapter);
            binding.rvOfflinepresc.setItemAnimator(null);
            binding.rvOfflinepresc.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            LoadRecyclerView(dbCrudHelper.GetPrescriptionInfoFromDB(),"Local");
        } catch (Exception exception) {
           // exception.printStackTrace();
        }
    }
}