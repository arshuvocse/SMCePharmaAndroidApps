package com.creatrix.salessolution.Model;

public class NotificationViewModel {
    int NotificationId;
    String NotificationPrimaryText;
    String NotificationRestText;
    boolean IsRead;
    int PrimaryId;


    public int getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(int notificationId) {
        NotificationId = notificationId;
    }

    public String getNotificationPrimaryText() {
        return NotificationPrimaryText;
    }

    public void setNotificationPrimaryText(String notificationPrimaryText) {
        NotificationPrimaryText = notificationPrimaryText;
    }

    public String getNotificationRestText() {
        return NotificationRestText;
    }

    public void setNotificationRestText(String notificationRestText) {
        NotificationRestText = notificationRestText;
    }

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }

    public int getPrimaryId() {
        return PrimaryId;
    }

    public void setPrimaryId(int primaryId) {
        PrimaryId = primaryId;
    }
}
