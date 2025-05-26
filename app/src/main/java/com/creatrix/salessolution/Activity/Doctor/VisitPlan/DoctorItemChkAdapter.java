package com.creatrix.salessolution.Activity.Doctor.VisitPlan;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creatrix.salessolution.Interface.Rcv_TourPlanListener;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.DoctorTourPlanMaster;
import com.creatrix.salessolution.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorItemChkAdapter extends RecyclerView.Adapter<DoctorItemChkAdapter.DICH> {
    Context c;
    private final static int FADE_DURATION = 500; //FADE_DURATION in milliseconds
    List<DoctorListViewModel> docList;
    ChkedItemListener itmCkListener;

    boolean[] checkBoxState;
    boolean isChecked;
    String check;
    ListView users;
    int position;

    Activity ac;
    AppCompatActivity aca;
    Fragment fg;
    FragmentManager fragmentManager;

    //Doc adds
    String[] listDoctor;
    boolean[] checkedItems;
    List<Integer> mUserItems = new ArrayList<>();
    List<DoctorListViewModel> chkedItemList = new ArrayList<>();
    List<DoctorListViewModel> unchkedItemList = new ArrayList<>();

    public DoctorItemChkAdapter(Context c, List<DoctorListViewModel> docList, ChkedItemListener itmCkListener) {
        this.c = c;
        this.docList = docList;
        this.itmCkListener = itmCkListener;
        //  Toast.makeText(c, "data "+docList.toString(), Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public DICH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_doclist_checkbox, parent, false);
        c = parent.getContext();
        return new DICH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DICH holder, int position) {
        DoctorListViewModel dl = docList.get(position);
        Log.d("TAG", "onBindViewHolder: " + dl);
        if (dl != null && docList.size()>0) {
            DoctorListViewModel dll=new DoctorListViewModel();
            //holder.tv_docName.setText(dl.getDoctorName());
            holder.chkitm.setText(dl.getDoctorName());

            holder.chkitm.setOnClickListener(v -> {
                if (holder.chkitm.isChecked()) {
                    dll.setDoctorName(dl.getDoctorName());
                    dll.setDoctorId(dl.getDoctorId());
                    dll.setDocTPDetailsId(dl.getDocTPDetailsId());
                    chkedItemList.add(dll);
                    itmCkListener.ckdItemName(chkedItemList,position);
                } if(!holder.chkitm.isChecked()) {
                    //chkedItemList.remove(dl.getDoctorName());
                    chkedItemList.remove(dll);
                   itmCkListener.unckdItemName(chkedItemList,position);
                }
               // itmCkListener.ckdItemName(chkedItemList,position);
            });
        } else {

            Toast.makeText(c, "No Doctors ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return docList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class DICH extends RecyclerView.ViewHolder {
        TextView tv_docName, tv_date;
        CheckBox chkitm;
        public DICH(@NonNull View v) {
            super(v);
            chkitm = v.findViewById(R.id.chkitm);
            tv_docName = v.findViewById(R.id.docname);
        }
    }

    public void filterListFun(List<DoctorListViewModel> DocList) {
        this.docList = DocList;
    }
}

