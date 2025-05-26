package com.creatrix.salessolution.Interface;

import android.content.Context;

public interface LoginInterface {
    interface View {
        void OnSuccess(int userId, String userName, String empMasterco, String loginName, String password, int empId, String userCo, int IsImeiMatch, String versionName, String role, int roleTypeid, String roleType, int isAccepted, int isForward, String DesigName);
        void OnError(String message);
        // void onVersionGet(String VersionName);

    }

    interface Presenter {
        void doLogin(String userId, String password, Context context, String imeiNo, String token, String Device, String AppVersion,String OS, String OS_Version);
        // void GetActiveVersion();
    }
}
