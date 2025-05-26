package com.creatrix.salessolution.Model;

public class ExpenseTypeDetails {
    int ExpenseTypDetailsId;
    String FieldName;
    boolean IsRequied;
    String ValueText;
    String ExpenseAmount;
    boolean isFixed;

    public String getValueText() {
        return ValueText;
    }

    public void setValueText(String valueText) {
        ValueText = valueText;
    }

    public int getExpenseTypDetailsId() {
        return ExpenseTypDetailsId;
    }

    public void setExpenseTypDetailsId(int expenseTypDetailsId) {
        ExpenseTypDetailsId = expenseTypDetailsId;
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public boolean isRequied() {
        return IsRequied;
    }

    public void setRequied(boolean requied) {
        IsRequied = requied;
    }

    public String getExpenseAmount() {
        return ExpenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        ExpenseAmount = expenseAmount;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    @Override
    public String toString() {
        return "ExpenseTypeDetails{" +
                "FieldName='" + FieldName + '\'' +
                ", ValueText='" + ValueText + '\'' +
                '}';
    }
}
