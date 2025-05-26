package com.creatrix.salessolution.Activity.Notice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.creatrix.salessolution.Interface.INotice;
import com.creatrix.salessolution.Model.Notice;
import com.creatrix.salessolution.Presenter.NoticePresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._recycler_NoticeAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.UtilityHelper.SnackBarManagement;
import com.creatrix.salessolution.databinding.ActivityNoticeBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NoticeActivity extends AppCompatActivity implements INotice.View {
    ActivityNoticeBinding binding;
    INotice.Presenter presenter;
    View aMasterlayout;
    SessionManagement session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoticeBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_notice);
        setContentView(binding.getRoot());
        presenter = new NoticePresenter(this,this);
        aMasterlayout = findViewById(R.id.aMasterlayout);
        session = new SessionManagement(getApplicationContext());
      //  session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        int empId = Integer.parseInt(Objects.requireNonNull(user.get(SessionManagement.KEY_EmpId)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_custom);
        toolbar.setNavigationOnClickListener(v -> finish());
        presenter.getNotices(empId);
        binding.attRefresh.setOnClickListener(v -> presenter.getNotices(empId));
        binding.swipNotice.setOnRefreshListener(() -> {
            presenter.getNotices(empId);
            binding.swipNotice.setRefreshing(false);
        });
    }

    @Override
    public void onSuccess(List<Notice> arrayList) {
        if(arrayList.size() > 0){
            binding.nodta.setVisibility(View.GONE);
            _recycler_NoticeAdapter mAdapter = new _recycler_NoticeAdapter(NoticeActivity.this, arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerView.setAdapter(mAdapter);
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    DividerItemDecoration.VERTICAL));
            binding.recyclerView.setItemAnimator(null);
            binding.recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();

        }
        else {
            binding.nodta.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onError(String message) {
        SnackBarManagement._error_CustomMessage(aMasterlayout,message);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}