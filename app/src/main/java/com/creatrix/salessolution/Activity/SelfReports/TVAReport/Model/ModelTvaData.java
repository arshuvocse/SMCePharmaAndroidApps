package com.creatrix.salessolution.Activity.SelfReports.TVAReport.Model;

import java.util.List;

public class ModelTvaData {
    String T_TargetValue;
    String T_OrderValue;
    String T_OrderAchiv;
    String T_InvoiceValue;
    String T_InvoiceAchiv;
    String T_SalesValue;
    String T_SalesAchiv;
    List<ModelTVADao> TVADaos;

    public ModelTvaData() {
    }

    public String getT_TargetValue() {
        return T_TargetValue;
    }

    public void setT_TargetValue(String t_TargetValue) {
        T_TargetValue = t_TargetValue;
    }

    public String getT_OrderValue() {
        return T_OrderValue;
    }

    public void setT_OrderValue(String t_OrderValue) {
        T_OrderValue = t_OrderValue;
    }

    public String getT_OrderAchiv() {
        return T_OrderAchiv;
    }

    public void setT_OrderAchiv(String t_OrderAchiv) {
        T_OrderAchiv = t_OrderAchiv;
    }

    public String getT_InvoiceValue() {
        return T_InvoiceValue;
    }

    public void setT_InvoiceValue(String t_InvoiceValue) {
        T_InvoiceValue = t_InvoiceValue;
    }

    public String getT_InvoiceAchiv() {
        return T_InvoiceAchiv;
    }

    public void setT_InvoiceAchiv(String t_InvoiceAchiv) {
        T_InvoiceAchiv = t_InvoiceAchiv;
    }

    public String getT_SalesValue() {
        return T_SalesValue;
    }

    public void setT_SalesValue(String t_SalesValue) {
        T_SalesValue = t_SalesValue;
    }

    public String getT_SalesAchiv() {
        return T_SalesAchiv;
    }

    public void setT_SalesAchiv(String t_SalesAchiv) {
        T_SalesAchiv = t_SalesAchiv;
    }

    public List<ModelTVADao> getTVADaos() {
        return TVADaos;
    }

    public void setTVADaos(List<ModelTVADao> TVADaos) {
        this.TVADaos = TVADaos;
    }
}
