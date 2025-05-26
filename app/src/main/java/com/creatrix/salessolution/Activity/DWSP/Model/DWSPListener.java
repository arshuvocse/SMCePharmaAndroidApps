package com.creatrix.salessolution.Activity.DWSP.Model;

import android.content.Intent;

public interface DWSPListener {
    void dwspAdd(int month,int year,String date, boolean hit, int pos,String fcb,String cam,String gen);
    void deleteItem(int pos);
}
