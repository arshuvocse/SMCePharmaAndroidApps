package com.creatrix.salessolution.Manager;

import android.content.Context;

import com.creatrix.salessolution.UtilityHelper.PhotoSharePrefUtil;

public class ShareprefManager {
    private static final String PREF_LAST_SYNC_DATE = "PREF_LAST_SYNC_DATE";
    public static final String PREF_LAST_ATTENDANCE_TIME = "PREF_LAST_ATTENDANCE_TIME";
    private static final String PREF_IMAGE_PATH = "PREF_IMAGE_PATH";

    public static String getLastSyncDate(Context context) {
        return PhotoSharePrefUtil.getString(context, PREF_LAST_SYNC_DATE, null);
    }

    public static void setLastSyncDate(Context context, String value) {
        PhotoSharePrefUtil.setString(context, PREF_LAST_SYNC_DATE, value);
    }

    public static String getLastAttendanceTime(Context context) {
        return PhotoSharePrefUtil.getString(context, PREF_LAST_ATTENDANCE_TIME, null);
    }

    public static void setLastAttendanceTime(Context context, String value) {
        PhotoSharePrefUtil.setString(context, PREF_LAST_ATTENDANCE_TIME, value);
    }

    public static String getImagePath(Context context) {
        return PhotoSharePrefUtil.getString(context, PREF_IMAGE_PATH, null);
    }

    public static void setImagePath(Context context, String value) {
        PhotoSharePrefUtil.setString(context, PREF_IMAGE_PATH, value);
    }
}
