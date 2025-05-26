package com.creatrix.salessolution.Activity.Expense.Report;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Activity.SelfReports.ExpenseSummery.Model.ExpenseSummery;

import java.util.List;
@Keep public class ExpenseSummeryData {
    private String GrandTotal;
    private String Allowences;
    private List<ExpenseSummery> Expense_Lists = null;

    public String getGrandTotal() {
        return GrandTotal;
    }

    public String getAllowences() {
        return Allowences;
    }

    public void setAllowences(String allowences) {
        Allowences = allowences;
    }

    public void setGrandTotal(String grandTotal) {
        GrandTotal = grandTotal;
    }

    public List<ExpenseSummery> getExpense_Lists() {
        return Expense_Lists;
    }

    public void setExpense_Lists(List<ExpenseSummery> expense_Lists) {
        Expense_Lists = expense_Lists;
    }
}
