package com.creatrix.salessolution.Model;

public class Notice {
    public int NoticeId;
    public String NoticeTitle;
    public String Announcement;
    public String FromDate;
    public String ToDate;
    public String FileLocation;
    public Boolean IsAppCheck;
    public String ImageString;
    public String ImagePreName;
    public String CreatedBy;
    public String AppSeenDate;


    public int getNoticeId() {
        return NoticeId;
    }

    public void setNoticeId(int noticeId) {
        NoticeId = noticeId;
    }

    public String getNoticeTitle() {
        return NoticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        NoticeTitle = noticeTitle;
    }

    public String getAnnouncement() {
        return Announcement;
    }

    public void setAnnouncement(String announcement) {
        Announcement = announcement;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getFileLocation() {
        return FileLocation;
    }

    public void setFileLocation(String fileLocation) {
        FileLocation = fileLocation;
    }

    public Boolean getAppCheck() {
        return IsAppCheck;
    }

    public void setAppCheck(Boolean appCheck) {
        IsAppCheck = appCheck;
    }

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String imageString) {
        ImageString = imageString;
    }

    public String getImagePreName() {
        return ImagePreName;
    }

    public void setImagePreName(String imagePreName) {
        ImagePreName = imagePreName;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getAppSeenDate() {
        return AppSeenDate;
    }

    public void setAppSeenDate(String appSeenDate) {
        AppSeenDate = appSeenDate;
    }
}
