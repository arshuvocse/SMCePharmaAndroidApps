package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.OrderMaster;

public interface RecyclerViewActionListener {
    void onEditTextFocusChange(int position,int value);
    boolean onLongClick(int position);
    void OrderSync(OrderMaster orderMaster);
}

