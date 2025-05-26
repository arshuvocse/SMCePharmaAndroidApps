package com.creatrix.salessolution.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Training {
    @SerializedName("TrainningId")
    @Expose
    private Integer trainningId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("TrainningMeterial")
    @Expose
    private String trainningMeterial;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;

    @SerializedName("CreateAt")
    @Expose
    private String CreateAt;

    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;

    @SerializedName("IsAppCheck")
    @Expose
    private Boolean isAppCheck;
    @SerializedName("FileLocation")
    @Expose
    private String fileLocation;

    public Integer getTrainningId() {
        return trainningId;
    }

    public void setTrainningId(Integer trainningId) {
        this.trainningId = trainningId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrainningMeterial() {
        return trainningMeterial;
    }

    public void setTrainningMeterial(String trainningMeterial) {
        this.trainningMeterial = trainningMeterial;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public Boolean getIsAppCheck() {
        return isAppCheck;
    }

    public void setIsAppCheck(Boolean isAppCheck) {
        this.isAppCheck = isAppCheck;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

}

