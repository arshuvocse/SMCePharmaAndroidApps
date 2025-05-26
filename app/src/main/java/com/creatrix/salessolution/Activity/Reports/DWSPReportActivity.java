package com.creatrix.salessolution.Activity.Reports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.creatrix.salessolution.Activity.Reports.Adapter.AdapterDwspReport;
import com.creatrix.salessolution.Activity.Reports.Model.ModelDWSPReport;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.UserProcessAPI;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityDwspreportBinding;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DWSPReportActivity extends AppCompatActivity {
    ActivityDwspreportBinding binding;
    String yearList[] = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
    String monthNameArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    SessionManagement session;
    String RoleType,empid;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDwspreportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pd=new ProgressDialog(DWSPReportActivity.this);
        session = new SessionManagement(DWSPReportActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        empid = Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId));
        RoleType = user.get(SessionManagement.KEY_EmpRoleType);

        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.yearSpinner.setAdapter(dataAdapter);
        int yearPos = dataAdapter.getPosition(String.valueOf(year));
        binding.yearSpinner.setSelection(yearPos);

        ArrayAdapter<String> dataAdapterMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthNameArray);
        dataAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.monthSpinner.setAdapter(dataAdapterMonth);

        String monName = monthNameArray[month];
        int monthPos = dataAdapterMonth.getPosition(monName);
        binding.monthSpinner.setSelection(monthPos);

        /*HashMap<String,String> filter = new HashMap<>();
        filter.put("MonthValue", String.valueOf(monthPos+1));
        filter.put("Year", String.valueOf(yearPos+1));
        filter.put("Role", RoleType);
        filter.put("EmpId", empid);
        hitApi(filter);*/

        binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int yearV = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                int monthV = i + 1;
                HashMap<String,String> filter = new HashMap<>();
                filter.put("MonthValue", String.valueOf(monthV));
                filter.put("Year", String.valueOf(yearV));
                filter.put("Role", RoleType);
                filter.put("EmpId", empid);
                hitApi(filter);
                //initCount++;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        binding.yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              /*  if (initCount > 0) {
                    //  List<MonthDate> aMondateList = new ArrayList<>();
                    int monthV = binding.monthSpinner.getSelectedItemPosition();
                    int yearV = Integer.parseInt(yearList[position].toString());
                    // aMondateList = printDatesInMonth(yearV, (monthV + 1));
                    HashMap<String,String> filter2 = new HashMap<>();
                    filter2.put("MonthValue", String.valueOf(monthV));
                    filter2.put("Year", String.valueOf(yearV));
                    filter2.put("Role", RoleType);
                    filter2.put("EmpId", empid);
                    hitApi(filter2);
                }*/
                int monthV = binding.monthSpinner.getSelectedItemPosition();
                int yearV = Integer.parseInt(yearList[position].toString());
                HashMap<String,String> filter2 = new HashMap<>();
                filter2.put("MonthValue", String.valueOf(monthV+1));
                filter2.put("Year", String.valueOf(yearV));
                filter2.put("Role", RoleType);
                filter2.put("EmpId", empid);
                hitApi(filter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void hitApi( HashMap<String,String> filter) {
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        UserProcessAPI service = RetrofitClientInstance.getRetrofitInstance().create(UserProcessAPI.class);
        Call<List<ModelDWSPReport>>calls = service.GetDWSPReport(filter);
        calls.enqueue(new Callback<List<ModelDWSPReport>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelDWSPReport>> call, @NonNull Response<List<ModelDWSPReport>> response) {
                if(pd!=null||pd.isShowing())
                {
                    pd.dismiss();
                }
                if(response.body()!=null)
                {
                    List<ModelDWSPReport> data= response.body();
                   setView(data);
                }
                else {
                    setView(null);
                    viewError("No Data Found");
                }
            }

            @Override
            public void onFailure(@NonNull Call <List<ModelDWSPReport>> call, @NonNull Throwable t) {
                if(pd!=null||pd.isShowing())
                {
                    pd.dismiss();
                }
                viewError("Failed To Get Report");
            }
        });
    }

    private void viewError(String no_data_found) {
        SnackBarManagement._error_CustomMessage(binding.getRoot(),no_data_found);
    }

    private void setView(List<ModelDWSPReport> data) {
        if(data!=null)
        {
            AdapterDwspReport mAdapter = new AdapterDwspReport(data,DWSPReportActivity.this);
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setAdapter(mAdapter);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.recyclerView.setLayoutManager(layoutManager);
            mAdapter.notifyDataSetChanged();
        }

    }
}