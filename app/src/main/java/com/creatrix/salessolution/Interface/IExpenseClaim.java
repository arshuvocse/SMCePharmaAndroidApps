package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.ExpenseClaimMaster;
import com.creatrix.salessolution.Model.ExpenseTypeDetails;
import com.creatrix.salessolution.Model.ExpenseTypeMaster;

import java.util.List;

public interface IExpenseClaim {
    interface  Presenter{
        void GetExpenseType(String roleType, String empId);
        void GetExpenseDetails(int id);
        void SaveExpenseClaim(ExpenseClaimMaster aInfo);


    }
    interface View{
        void onExpenseTypeGet(List<ExpenseTypeMaster> aList);
        void onExpenseTypeDetailsGet(List<ExpenseTypeDetails> aList);
        void onSaveSuccess(String message);
        void onSaveError(String message);
    }
}
