package com.creatrix.salessolution.Model.Order;

import com.creatrix.salessolution.Model.OrderViewModel;

import java.util.List;

public class OrderTrackigMaster {
   String TotalNetAmount;
   List<OrderViewModel>Order_Lists;

    public String getTotalNetAmount() {
        return TotalNetAmount;
    }

    public void setTotalNetAmount(String totalNetAmount) {
        TotalNetAmount = totalNetAmount;
    }

    public List<OrderViewModel> getOrder_Lists() {
        return Order_Lists;
    }

    public void setOrder_Lists(List<OrderViewModel> order_Lists) {
        Order_Lists = order_Lists;
    }
}
