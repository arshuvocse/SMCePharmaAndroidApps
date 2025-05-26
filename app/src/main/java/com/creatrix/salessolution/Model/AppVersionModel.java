package com.creatrix.salessolution.Model;

public class AppVersionModel {
    private int AppVersionId;
    private int Version;
    private String VersionName;


    public int getAppVersionId() {
        return AppVersionId;
    }

    public void setAppVersionId(int appVersionId) {
        AppVersionId = appVersionId;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }
}
