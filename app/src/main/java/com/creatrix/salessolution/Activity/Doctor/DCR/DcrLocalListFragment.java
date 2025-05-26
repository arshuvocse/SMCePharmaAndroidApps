package com.creatrix.salessolution.Activity.Doctor.DCR;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.RecyclerAdapter._pending_dcrListAdapter;
import com.creatrix.salessolution.databinding.FragmentLocalDcrListBinding;
import com.google.gson.Gson;

import java.util.List;

public class DcrLocalListFragment extends Fragment {

    FragmentLocalDcrListBinding binding;
    private _pending_dcrListAdapter mAdapter;
    DBDoctorHelper dbDoctorHelper;
    DBCrudHelper dbCrudHelper;
    public DcrLocalListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dcr_list, container, false);
        binding=FragmentLocalDcrListBinding.inflate(getLayoutInflater());
        //View v= inflater.inflate(binding.getRoot(), container, false);
        dbCrudHelper=new DBCrudHelper(getActivity());
        LoadRecyclerView(getActivity(),dbCrudHelper.GetDcrInfoFromDB());

        return binding.getRoot();
    }

    public void LoadRecyclerView(FragmentActivity activity, List<DcrSM> aList) {

        if (aList != null) {
            Gson gson = new Gson();
            String data = gson.toJson(aList);
            System.out.println("offline get"+data);

            mAdapter = new _pending_dcrListAdapter(aList,activity);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            binding.rvOfflinedcr.setLayoutManager(mLayoutManager);
            binding.rvOfflinedcr.setItemAnimator(new DefaultItemAnimator());
            binding.rvOfflinedcr.setAdapter(mAdapter);
            binding.rvOfflinedcr.setItemAnimator(null);
            binding.rvOfflinedcr.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            LoadRecyclerView(getActivity(),dbCrudHelper.GetDcrInfoFromDB());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
