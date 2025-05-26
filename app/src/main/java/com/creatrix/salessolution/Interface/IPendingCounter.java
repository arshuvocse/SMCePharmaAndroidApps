package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.DcrSM;
import com.creatrix.salessolution.Model.OrderDetailSample;
import com.creatrix.salessolution.Model.OrderDetails;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.PrescriptionSM;
import com.creatrix.salessolution.Model.ProductSample;

import java.util.List;

public interface IPendingCounter {
    interface  Presenter{
        void totalDcr();
        void totalPresc();
        void totalSample();
        void totalOrder();
        void totalOrderMaster();

    }
    interface  View{
        void totalDcr(List<DcrSM> dcrList);
        void totalPresc(List<PrescriptionSM> preList);
        void totalSample(List<OrderDetailSample> soList);
        void totalOrder(List<OrderDetails>oList);
        void totalOrderMaster(List<OrderMaster>oList);
    }
}
