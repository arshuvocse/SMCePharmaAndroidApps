package com.creatrix.salessolution.Activity.OrderProcess;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creatrix.salessolution.Activity.OrderProcess.Adapter.DraftOrderAdapter;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.Interface.DeleteListeners;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._prescriptionList_Recycler;
import com.creatrix.salessolution.databinding.FragmentHomeBinding;
import com.creatrix.salessolution.databinding.FragmentOrderCommercialListBinding;

import java.util.List;

public class OrderCommercialListFragment extends Fragment implements DeleteListeners {
    FragmentOrderCommercialListBinding binding;
    DraftOrderAdapter draftOrderAdapter;
    DBCrudHelper dbCrudHelper;
    public OrderCommercialListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderCommercialListBinding.inflate(getLayoutInflater());
        dbCrudHelper=new DBCrudHelper(getActivity());
        LoadRecyclerView(dbCrudHelper.getOrderListFromSQLite(),"Local");
        return binding.getRoot();
    }
    public void LoadRecyclerView(List<OrderMaster> aList, String type) {


      //  Toast.makeText(getActivity(), ""+, Toast.LENGTH_SHORT).show();
        if (aList != null) {
            draftOrderAdapter=new DraftOrderAdapter(aList,this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            binding.rvCommercial.setLayoutManager(mLayoutManager);
            binding.rvCommercial.setItemAnimator(new DefaultItemAnimator());
            binding.rvCommercial.setAdapter(draftOrderAdapter);
            binding.rvCommercial.setItemAnimator(null);
            binding.rvCommercial.scrollToPosition(0);
            draftOrderAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public boolean onLongClick(int position,int localID) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure wants to delete the Item ?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(

                "Delete",
                (dialog, id) -> {
                    try {
                        draftOrderAdapter.notifyItemRemoved(position);
                        draftOrderAdapter.notifyDataSetChanged();
                        //Todo:When Delete any product
                        dbCrudHelper.DeleteOldOrder_OrderTable_SQLite(localID);

                        dialog.cancel();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();

        return true;

    }
}