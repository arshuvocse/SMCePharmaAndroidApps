package com.creatrix.salessolution.Activity.Approval.Prescription;

import androidx.annotation.Keep;

import com.creatrix.salessolution.Model.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@Keep public class PrescriptionMaster {
    @SerializedName("PrescriptionId")
    @Expose
    private Integer prescriptionId;
    @SerializedName("PrescriptionDate")
    @Expose
    private String prescriptionDate;
    @SerializedName("PrescriptionType")
    @Expose
    private String prescriptionType;
    @SerializedName("DoctorName")
    @Expose
    private String doctorName;
    @SerializedName("ChemberName")
    @Expose
    private String chemberName;
    @SerializedName("DcrDate")
    @Expose
    private String dcrDate;
    @SerializedName("TourTypeId")
    @Expose
    private Integer tourTypeId;
    @SerializedName("ChemberId")
    @Expose
    private Integer chemberId;
    @SerializedName("EntryBy")
    @Expose
    private Integer entryBy;
    @SerializedName("EntryDate")
    @Expose
    private Integer entryDate;
    @SerializedName("UpdateBy")
    @Expose
    private Integer updateBy;
    @SerializedName("UpdateDate")
    @Expose
    private String updateDate;
    @SerializedName("IsApproved")
    @Expose
    private Object isApproved;
    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("RegionName")
    @Expose
    private String regionName;
    @SerializedName("AreaName")
    @Expose
    private String areaName;
    @SerializedName("TerritoryName")
    @Expose
    private String territoryName;
    @SerializedName("SubTerritoryName")
    @Expose
    private String subTerritoryName;
    @SerializedName("MarketName")
    @Expose
    private String marketName;
    @SerializedName("ImageString")
    @Expose
    private String imageString;


    @SerializedName("DoctorId")
    @Expose
    private Integer doctorId;
    @SerializedName("DocTPDetailsId")
    @Expose
    private Integer docTPDetailsId;
    @SerializedName("GroupId")
    @Expose
    private Integer groupId;
    @SerializedName("RegionId")
    @Expose
    private Integer regionId;
    @SerializedName("AreaId")
    @Expose
    private Integer areaId;
    @SerializedName("SubTerritoryId")
    @Expose
    private Integer subTerritoryId;
    @SerializedName("TerritoryId")
    @Expose
    private Integer territoryId;
    @SerializedName("MarketId")
    @Expose
    private Integer marketId;
    @SerializedName("IsNonEffectiveReason")
    @Expose
    private Integer isNonEffectiveReason;
    @SerializedName("ReasonId")
    @Expose
    private Integer reasonId;
    @SerializedName("EntryDate_Apps")
    @Expose
    private String entryDateApps;
    @SerializedName("ApprovalStatus")
    @Expose
    private Object approvalStatus;
    @SerializedName("aPrescriptionDtlsDAO")
    @Expose
    private List<Product> aPrescriptionDtlsDAO = null;

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getPrescriptionType() {
        return prescriptionType;
    }

    public void setPrescriptionType(String prescriptionType) {
        this.prescriptionType = prescriptionType;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getChemberName() {
        return chemberName;
    }

    public void setChemberName(String chemberName) {
        this.chemberName = chemberName;
    }

    public String getDcrDate() {
        return dcrDate;
    }

    public void setDcrDate(String dcrDate) {
        this.dcrDate = dcrDate;
    }

    public Integer getTourTypeId() {
        return tourTypeId;
    }

    public void setTourTypeId(Integer tourTypeId) {
        this.tourTypeId = tourTypeId;
    }

    public Integer getChemberId() {
        return chemberId;
    }

    public void setChemberId(Integer chemberId) {
        this.chemberId = chemberId;
    }

    public Integer getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(Integer entryBy) {
        this.entryBy = entryBy;
    }

    public Integer getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Integer entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Object getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Object isApproved) {
        this.isApproved = isApproved;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public String getSubTerritoryName() {
        return subTerritoryName;
    }

    public void setSubTerritoryName(String subTerritoryName) {
        this.subTerritoryName = subTerritoryName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }


    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDocTPDetailsId() {
        return docTPDetailsId;
    }

    public void setDocTPDetailsId(Integer docTPDetailsId) {
        this.docTPDetailsId = docTPDetailsId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getSubTerritoryId() {
        return subTerritoryId;
    }

    public void setSubTerritoryId(Integer subTerritoryId) {
        this.subTerritoryId = subTerritoryId;
    }

    public Integer getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Integer territoryId) {
        this.territoryId = territoryId;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public Integer getIsNonEffectiveReason() {
        return isNonEffectiveReason;
    }

    public void setIsNonEffectiveReason(Integer isNonEffectiveReason) {
        this.isNonEffectiveReason = isNonEffectiveReason;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getEntryDateApps() {
        return entryDateApps;
    }

    public void setEntryDateApps(String entryDateApps) {
        this.entryDateApps = entryDateApps;
    }

    public Object getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Object approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<Product> getaPrescriptionDtlsDAO() {
        return aPrescriptionDtlsDAO;
    }

    public void setaPrescriptionDtlsDAO(List<Product> aPrescriptionDtlsDAO) {
        this.aPrescriptionDtlsDAO = aPrescriptionDtlsDAO;
    }
/* public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getPrescriptionType() {
        return prescriptionType;
    }

    public void setPrescriptionType(String prescriptionType) {
        this.prescriptionType = prescriptionType;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getChemberName() {
        return chemberName;
    }

    public void setChemberName(String chemberName) {
        this.chemberName = chemberName;
    }

    public Object getDcrDate() {
        return dcrDate;
    }

    public void setDcrDate(String dcrDate) {
        this.dcrDate = dcrDate;
    }

    public Object getTourTypeId() {
        return tourTypeId;
    }

    public void setTourTypeId(Object tourTypeId) {
        this.tourTypeId = tourTypeId;
    }

    public Object getChemberId() {
        return chemberId;
    }

    public void setChemberId(Object chemberId) {
        this.chemberId = chemberId;
    }

    public Object getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(Object entryBy) {
        this.entryBy = entryBy;
    }

    public Object getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Object entryDate) {
        this.entryDate = entryDate;
    }

    public Object getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Object updateBy) {
        this.updateBy = updateBy;
    }

    public Object getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Object updateDate) {
        this.updateDate = updateDate;
    }

    public Object getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Object isApproved) {
        this.isApproved = isApproved;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Object getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public String getSubTerritoryName() {
        return subTerritoryName;
    }

    public void setSubTerritoryName(String subTerritoryName) {
        this.subTerritoryName = subTerritoryName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getImagePreName() {
        return imagePreName;
    }

    public void setImagePreName(String imagePreName) {
        this.imagePreName = imagePreName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Object getDocTPDetailsId() {
        return docTPDetailsId;
    }

    public void setDocTPDetailsId(Object docTPDetailsId) {
        this.docTPDetailsId = docTPDetailsId;
    }

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    public Object getRegionId() {
        return regionId;
    }

    public void setRegionId(Object regionId) {
        this.regionId = regionId;
    }

    public Object getAreaId() {
        return areaId;
    }

    public void setAreaId(Object areaId) {
        this.areaId = areaId;
    }

    public Object getSubTerritoryId() {
        return subTerritoryId;
    }

    public void setSubTerritoryId(Object subTerritoryId) {
        this.subTerritoryId = subTerritoryId;
    }

    public Object getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Object territoryId) {
        this.territoryId = territoryId;
    }

    public Object getMarketId() {
        return marketId;
    }

    public void setMarketId(Object marketId) {
        this.marketId = marketId;
    }

    public Object getIsNonEffectiveReason() {
        return isNonEffectiveReason;
    }

    public void setIsNonEffectiveReason(Object isNonEffectiveReason) {
        this.isNonEffectiveReason = isNonEffectiveReason;
    }

    public Object getReasonId() {
        return reasonId;
    }

    public void setReasonId(Object reasonId) {
        this.reasonId = reasonId;
    }

    public Object getEntryDateApps() {
        return entryDateApps;
    }

    public void setEntryDateApps(Object entryDateApps) {
        this.entryDateApps = entryDateApps;
    }

    public Object getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Object approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<Product> getaPrescriptionDtlsDAO() {
        return aPrescriptionDtlsDAO;
    }

    public void setaPrescriptionDtlsDAO(List<Product> aPrescriptionDtlsDAO) {
        this.aPrescriptionDtlsDAO = aPrescriptionDtlsDAO;
    }*/

}
