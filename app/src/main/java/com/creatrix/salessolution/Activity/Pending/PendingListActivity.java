package com.creatrix.salessolution.Activity.Pending;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.creatrix.salessolution.Activity.OrderProcess.Adapter.DraftOrderAdapter;
import com.creatrix.salessolution.Interface.DeleteListeners;
import com.creatrix.salessolution.Interface.IPendingCounter;
import com.creatrix.salessolution.Interface.NotifyListener;
import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.OrderDetailSample;
import com.creatrix.salessolution.Model.OrderDetails;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Presenter.PendingCounterPresenter;
import com.creatrix.salessolution.R;
import com.creatrix.salessolution.RecyclerAdapter._pending_dcrListAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.ActivityPendingListBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PendingListActivity extends AppCompatActivity implements IPendingCounter.View, NotifyListener, DeleteListeners {
    // public static NotifyListener notifyListener;
    ActivityPendingListBinding binding;
    DraftOrderAdapter orderAdapter;
    _pending_dcrListAdapter dcrAdapter;
    SessionManagement session;
    PendingCounterPresenter presenter;
    HashMap<String, String> user;
    String empId;

    TabLayout tabLayout;
    FragmentAdapter adapter;
    ViewPager2 viewPager;

    //OrderListVM orderListVM;
    String tdayshow;
    FragmentManager fm;
    String countDCR, countPrs, countSO, countCO;
    TextView tabtag, tabitemcounts;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingListBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_pending_list);
        setContentView(binding.getRoot());


        try {
            presenter = new PendingCounterPresenter(this, this);
            presenter.totalDcr();
            presenter.totalPresc();
            //presenter.totalSample();
            presenter.totalOrder();
            presenter.totalOrderMaster();

            orderAdapter = new DraftOrderAdapter(this, this, this, this);
            orderAdapter.notifyDataSetChanged();
            dcrAdapter = new _pending_dcrListAdapter(this, this);
            dcrAdapter.notifyDataSetChanged();

            session = new SessionManagement(getApplicationContext());
            user = session.getUserDetails();
            empId = user.get(SessionManagement.KEY_EmpId);
            //TODO: DCR,SampleOrder,CommercialOrder --Lists
            tdayshow = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //orderListVM = new ViewModelProvider(this).get(OrderListVM.class);

      /*  orderListVM.AllOrderApiCall();
        orderListVM.OrdergetSaleListApiCall();
        orderListVM.OrdergetInprocessListApiCall();
        orderListVM.OrdergetCancleListApiCall();*/

        prepareTab();
    }

    public void prepareTab() {
        tabLayout = (TabLayout) findViewById(R.id.pendingtab);
        viewPager = (ViewPager2) findViewById(R.id.pendingViewpager);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle(), this/*,tabname,tcc*/);
        viewPager.setAdapter(adapter);
        final List<String> colors = new ArrayList<String>() {
            {
                //add("#DADADA");
                add("#EEF8FA");
                add("#FFFFFF");
                //  add("#FFFFFF");
                add("#FFFFFF");
            }
        };
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        LayoutInflater layoutInflater = LayoutInflater.from(PendingListActivity.this);
                        View tabView = layoutInflater.inflate(R.layout.tab_custom_layout, null, false);
                        // ConstraintLayout selection = (ConstraintLayout) tabView.findViewById(R.id.selection);
                        tabtag = (TextView) tabView.findViewById(R.id.tabtagz);
                        tabitemcounts = (TextView) tabView.findViewById(R.id.tabitemcounts);
                        tab.setCustomView(tabView);

                        tab.view.setBackgroundColor(Color.parseColor(colors.get(position)));
                        switch (position) {
                            case 0:
                                try {
                                    tabtag.setText("DCR");
                                    tabitemcounts.setText(countDCR);
                                } catch (Exception ex) {
                                }

                                break;
                            case 1:
                                try {
                                    tabtag.setText("Prescription");
                                    tabitemcounts.setText(countPrs);
                                } catch (Exception ex) {
                                }

                                break;
                          /*  case 2:
                                tabtag.setText("Sample");
                                tabitemcounts.setText(countSO);
                                break;*/
                            case 2:
                                try {
                                    tabtag.setText("Order");
                                    tabitemcounts.setText(countCO);
                                } catch (Exception ex) {
                                }
                                break;
                        }

                    }
                }).attach();
        prepareViewpager();
    }

    private void prepareViewpager() {
        //Selected Shade Color
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                final List<String> colors = new ArrayList<String>() {
                    {
                        add("#EEF8FA");
                        add("#F0FBF6");
                        add("#EFD8D7");
                    }
                };

                if (tab.getPosition() == 0) {
                    // tab.view.setBackgroundColor(Color.parseColor("#DADADA"));
                    tab.view.setBackgroundColor(Color.parseColor(colors.get(0)));
                } else {
                    tab.view.setBackgroundColor(Color.parseColor(colors.get(tab.getPosition())));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                final List<String> white = new ArrayList<String>() {
                    {
                        add("#FFFFFF");
                        add("#FFFFFF");
                        add("#FFFFFF");
                    }
                };
                //tab.view.setAlpha(1);
                tab.view.setBackgroundColor(Color.parseColor(white.get(tab.getPosition())));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    @Override
    public void totalDcr(List<DcrSM> dcrList) {
        try {
            if (dcrList != null) {
                int icountAD = dcrList.size();
                countDCR = String.valueOf(icountAD);
            } else {
                countDCR.equals("0");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void totalPresc(List<PrescriptionSM> preList) {

        try {
            if (preList != null) {
                int icountAP = preList.size();
                countPrs = String.valueOf(icountAP);
            } else {
                countPrs.equals("0");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void totalSample(List<OrderDetailSample> soList) {
        try {
            if (soList != null) {
                int icountASO = soList.size();
                countSO = String.valueOf(icountASO);

            } else {
                countSO.equals("0");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void totalOrder(List<OrderDetails> oList) {
     /*   try {
            if(oList!=null)
            {
                int icountAO = oList.size();
                countCO = String.valueOf(icountAO);
            }
            else {
                //tabitemcounts.setText("0");
                countCO.equals("0");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/
    }

    @Override
    public void totalOrderMaster(List<OrderMaster> oList) {
        try {
            if (oList != null) {
                int icountAO = oList.size();
                countCO = String.valueOf(icountAO);
            } else {
                //tabitemcounts.setText("0");
                countCO.equals("0");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       // Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.totalDcr();
        presenter.totalPresc();
        presenter.totalSample();
        presenter.totalOrder();
        presenter.totalOrderMaster();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.totalDcr();
        presenter.totalPresc();
        presenter.totalSample();
        presenter.totalOrder();
        presenter.totalOrderMaster();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.totalDcr();
        presenter.totalPresc();
        presenter.totalSample();
        presenter.totalOrder();
        presenter.totalOrderMaster();
    }

    @Override
    public void onNotify(boolean a) {
       /* Toast.makeText(this, "hitted", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "" + a, Toast.LENGTH_SHORT).show();*/
        onRestart();
        onResume();
    }

    @Override
    public boolean onLongClick(int position, int local) {
    /*    Toast.makeText(this, "hitted", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "posx : " + String.valueOf(position), Toast.LENGTH_SHORT).show();*/
        onResume();
        return true;
    }
}