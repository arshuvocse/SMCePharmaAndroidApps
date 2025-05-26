package com.creatrix.salessolution.Interface;
import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalData;
import com.creatrix.salessolution.Activity.Approval.Order.OrderApprovalSaveBody;
import com.creatrix.salessolution.Model.ExpenseClaimMaster;

import java.util.List;
import java.util.Map;



public interface IOrderApproval {
    interface  Presenter{
        void getOrderApprovalList(String pram,Map<String,String> mapparam);
        void SaveOrderApproval(OrderApprovalSaveBody aInfo);
    }
    interface  View{
        void onGetOrderSuccess(List<OrderApprovalData> aList);
        void onSaveSuccess(String message);
        void onError(String message);
    }
}
