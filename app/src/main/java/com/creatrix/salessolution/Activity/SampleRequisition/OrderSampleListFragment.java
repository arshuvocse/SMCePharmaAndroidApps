package com.creatrix.salessolution.Activity.SampleRequisition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creatrix.salessolution.Interface.IProduct;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Presenter.ProductPresenter;
import com.creatrix.salessolution.RecyclerAdapter._orederSampleRequiAdapter;
import com.creatrix.salessolution.UtilityHelper.SessionManagement;
import com.creatrix.salessolution.databinding.FragmentOrderSampleListBinding;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderSampleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderSampleListFragment extends Fragment implements IProduct.View {

    _orederSampleRequiAdapter mAdapter;
    ProductPresenter productPresenter;
   SessionManagement session;
    String userName,empId;
    FragmentOrderSampleListBinding binding;
    public OrderSampleListFragment() {
        // Required empty public constructor
    }

    public static OrderSampleListFragment newInstance(String param1, String param2) {
        OrderSampleListFragment fragment = new OrderSampleListFragment();
        Bundle args = new Bundle();
       /* args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManagement(getActivity());
        HashMap<String, String> user = session.getUserDetails();
         userName = user.get(SessionManagement.KEY_LoginName);
         empId = user.get(SessionManagement.KEY_EmpId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View v= inflater.inflate(R.layout.fragment_sample_requi, container, false);
        binding = FragmentOrderSampleListBinding.inflate(getLayoutInflater());

        productPresenter = new ProductPresenter(this, getActivity());
        productPresenter.getSampleProducts(Integer.parseInt(empId));
        //presenter.getRegularProducts(Integer.parseInt(empId));
        binding.swipSampleorder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //int empid = Integer.parseInt(empId);
                productPresenter.getSampleProducts(Integer.parseInt(empId));
                binding.swipSampleorder.setRefreshing(false);
            }
        });

        return binding.getRoot();
        //return v;
    }

    @Override
    public void OnError(String message) {

    }

    @Override
    public void onProductsGet(List<Product> aList) {
        if(aList !=null){
          //  Toast.makeText(getActivity(), "Data done", Toast.LENGTH_SHORT).show();

            mAdapter = new _orederSampleRequiAdapter(getActivity(),aList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            binding.rvSamplerequi.setLayoutManager(mLayoutManager);
            binding.rvSamplerequi.setItemAnimator(new DefaultItemAnimator());
            binding.rvSamplerequi.setAdapter(mAdapter);
          /*  binding.rvSamplerequi.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));*/
            binding.rvSamplerequi.setItemAnimator(null);
            binding.rvSamplerequi.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onProductSampleGet(List<ProductSample> aList) {

    }
}