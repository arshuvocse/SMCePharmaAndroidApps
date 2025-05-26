package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.OrderProcess.Model.OrderMasterModel;
import com.creatrix.salessolution.Model.CordinateUpdate;
import com.creatrix.salessolution.Model.OrderMaster;
import com.creatrix.salessolution.Model.OrderMasterNew;

public interface IOrderManagement {
    interface  Presenter{
        void makeOrder(OrderMaster aOrder);
        void draftOrder(OrderMaster aOrder);
        void SyncOrder(OrderMaster aOrder,int localOrderId);
        void UpdateCustomerLocation(CordinateUpdate location);
        void makeOrder2(OrderMasterModel aOrder,String Who);
      //  void makeOrder2(OrderMasterNew aOrder);
        void draftOrder2(OrderMasterNew aOrder);

    }
    interface  View{
        void onOrderSuccess(String message,String Who);
        void onOrderDraftSuccess(String message);
        void onOrderError(String message);
        void onGenericSuccess(String message);
        void onGenericError(String message);
    }
}
