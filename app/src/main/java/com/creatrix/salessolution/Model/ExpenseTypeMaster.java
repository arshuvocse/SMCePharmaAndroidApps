package com.creatrix.salessolution.Model;

public class ExpenseTypeMaster {

    int ExpenseTypeId;
    String ExpenseTypeName;
    boolean ImageRequired;
    String ExpenseAmount;
    boolean isFixed;

    public int getExpenseTypeId() {
        return ExpenseTypeId;
    }

    public void setExpenseTypeId(int expenseTypeId) {
        ExpenseTypeId = expenseTypeId;
    }

    public String getExpenseTypeName() {
        return ExpenseTypeName;
    }

    public void setExpenseTypeName(String expenseTypeName) {
        ExpenseTypeName = expenseTypeName;
    }

    public boolean isImageRequired() {
        return ImageRequired;
    }

    public void setImageRequired(boolean imageRequired) {
        ImageRequired = imageRequired;
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
        return ExpenseTypeName;
    }
}
