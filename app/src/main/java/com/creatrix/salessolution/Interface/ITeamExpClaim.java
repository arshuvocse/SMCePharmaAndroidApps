package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Activity.Expense.Model.ApproveExpRQ;
import com.creatrix.salessolution.Activity.Expense.Model.ExpListTeam;
import com.creatrix.salessolution.Model.ExpenseClaimMaster;
import com.creatrix.salessolution.Model.ExpenseTypeMaster;

import java.util.List;
import java.util.Map;

public interface ITeamExpClaim{

    interface  Presenter{
        void GetExpenseCTeamList(String param, Map<String, String> filter);
        void SaveExpenseCTeam(ApproveExpRQ approveExpRQ);

    }
    interface View{
        void onExpenseCTeamListGet(List<ExpListTeam> aList);
        void onSaveSuccess(String message);
        void onSaveError(String message);
    }
}
