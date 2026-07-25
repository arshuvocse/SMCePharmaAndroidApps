package com.creatrix.salessolution.Presenter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import com.creatrix.salessolution.Activity.Doctor.DCR.NonEffectiveReason;
import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.Interface.ISyncMaster;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
import com.creatrix.salessolution.Model.Doctor.Brand;
import com.creatrix.salessolution.Model.Doctor.ContactTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorCategory;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberName;
import com.creatrix.salessolution.Model.Doctor.DoctorChamberTypeVM;
import com.creatrix.salessolution.Model.Doctor.DoctorDegreeViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorDesignation;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorSpecialityViewModel;
import com.creatrix.salessolution.Model.Doctor.DoctorTypeVM;
import com.creatrix.salessolution.Model.Doctor.SpecialDay;
import com.creatrix.salessolution.Model.ExpenseTypeMaster;
import com.creatrix.salessolution.Model.Gift;
import com.creatrix.salessolution.Model.InstitutionVM;
import com.creatrix.salessolution.Model.LeaveTypeInfo;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.ASM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.MIO;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.NSM;
import com.creatrix.salessolution.Model.MarketStructure.AssignEmpTable.RSM;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Area;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Group;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Market;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Region;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.SubTeritorry;
import com.creatrix.salessolution.Model.MarketStructure.StructureTable.Teritorry;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.PrescriptionTYpe;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.QuotedPrice;
import com.creatrix.salessolution.Model.StationType;

import com.creatrix.salessolution.Model.ThanaVM;
import com.creatrix.salessolution.Model.TourPurposeViewModel;
import com.creatrix.salessolution.Model.TourTypeViewModel;
import com.creatrix.salessolution.Model.Transport;
import com.creatrix.salessolution.Model.UserByRole;
import com.creatrix.salessolution.Model.UserRole;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.ApiMasterSync;
import com.creatrix.salessolution.Network.CalculationApiCall;
import com.creatrix.salessolution.Network.OrderProcessAPICALL;
import com.creatrix.salessolution.Network.ProductApi;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.RetrofitClientOrderProcessInstance;
import com.creatrix.salessolution.Network.TourApiCall;
import com.creatrix.salessolution.Network.UserProcessAPI;
import com.creatrix.salessolution.Network.apiSeedDataCall;
import com.creatrix.salessolution.Services.Constants;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncMasterPresenter implements ISyncMaster.Presenter {
    ISyncMaster.View view;
    Context context;
    DBCrudHelper dbCrudHelper;
    SyncDb_Helper syncDb_helper;

    public SyncMasterPresenter(ISyncMaster.View view, Context context) {
        this.view = view;
        this.context = context;
        dbCrudHelper = new DBCrudHelper(context);
        syncDb_helper = new SyncDb_Helper(context);
    }
/*
    public SyncMasterPresenter(ISyncMaster.View view, Context context) {
        this.view = view;
        this.context = context;
        dbCrudHelper = new DBCrudHelper(context);
        syncDb_helper = new SyncDb_Helper(context);
    }*/

    @Override
    public void cllNSM(String empcode, String emprole) {
        //NSM
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<NSM>> call = service.GetNSM(empcode, emprole);
            call.enqueue(new Callback<List<NSM>>() {
                @Override
                public void onResponse(@NonNull Call<List<NSM>> call, @NonNull Response<List<NSM>> response) {
                    List<NSM> teritories = response.body();
                    if (teritories != null) {
                        boolean isTrue = syncDb_helper.InsertNSMInfo(teritories);
                        if (isTrue) {
                            boolean isNSMDone = true;
                            view.onGetNSM("Done",true);
                        } else {
                            view.onGetNSM("Error",false);
                        }
                    }else {
                        view.onGetNSM("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<NSM>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetNSM("Error", false);
                }
            });
        } catch (Exception ex) {
        }

    }

    @Override
    public void cllDZSM(String empcode, String emprole) {
//RSM
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<RSM>> call = service.GetRSM(empcode, emprole);
            call.enqueue(new Callback<List<RSM>>() {
                @Override
                public void onResponse(@NonNull Call<List<RSM>> call, @NonNull Response<List<RSM>> response) {
                    List<RSM> rsm = response.body();
                    if (rsm != null) {
                        boolean isTrue = syncDb_helper.InsertRSMInfo(rsm);
                        if (isTrue) {
                            view.onGetDZSM("Done",true);
                        } else {
                            view.onGetDZSM("Error",false);
                        }
                    }else {
                        view.onGetDZSM("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<RSM>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetDZSM("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllAM(String empcode, String emprole) {
//ASM
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<ASM>> call = service.GetASM(empcode, emprole);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<ASM>>() {
                @Override
                public void onResponse(@NonNull Call<List<ASM>> call, @NonNull Response<List<ASM>> response) {
                    List<ASM> rsm = response.body();
                    if (rsm != null) {
                        boolean isTrue = syncDb_helper.InsertASMInfo(rsm);
                        if (isTrue) {
                            view.onGetAM("Done",true);
                        } else {
                            view.onGetAM("Error",false);
                        }
                    }else {
                        view.onGetAM("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ASM>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetAM("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllMIO(String empcode, String emprole) {
//MIO
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<MIO>> call = service.GetMio(empcode, emprole);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<MIO>>() {
                @Override
                public void onResponse(@NonNull Call<List<MIO>> call, @NonNull Response<List<MIO>> response) {
                    List<MIO> mio = response.body();
                    if (mio != null) {
                        boolean isTrue = syncDb_helper.InsertMIOInfo(mio);
                        if (isTrue) {
                            view.onGetMIO("Done",true);
                        } else {
                            view.onGetMIO("Error",false);
                        }
                    }else {
                        view.onGetMIO("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<MIO>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetMIO("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllGroup(String empcode, String emprole) {


        syncDb_helper.deleteAllMasterTables();
        //Group
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Group>> call = service.GetGroupByUser(empcode, emprole);
            call.enqueue(new Callback<List<Group>>() {
                @Override
                public void onResponse(@NonNull Call<List<Group>> call, @NonNull Response<List<Group>> response) {
                    List<Group> group = response.body();

                    if (group != null) {
                        boolean isTrue = syncDb_helper.InsertGroup(group);
                        if (isTrue) {
                            view.onGetGroup("Done",true);
                        } else {
                            view.onGetGroup("Error",false);
                        }
                    }else {
                        view.onGetGroup("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Group>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetGroup("Error", false);
                }
            });

        } catch (Exception ex) {
        }

        /*//Teritory
        try {
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<TerritoryViewModel>> call = service.GetTerritoryByEmpId(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<TerritoryViewModel>>() {
                @Override
                public void onResponse(Call<List<TerritoryViewModel>> call, Response<List<TerritoryViewModel>> response) {
                    List<TerritoryViewModel> teritories = response.body();
                    if (teritories != null) {
                        boolean isTrue = syncDb_helper.InsertTeritory(teritories);
                        if (isTrue) {
                            isTeritoryDone = true;
                            viewBindings.teritoryTxt.setText("Done");

                        } else {
                            //DoctorInfoGetView("Error");
                            viewBindings.teritoryTxt.setText("Error");
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<TerritoryViewModel>> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }*/
    }

    @Override
    public void cllZone(String empcode, String emprole) {
        //Region
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Region>> call = service.GetRegionByUser(empcode, emprole);
            call.enqueue(new Callback<List<Region>>() {
                @Override
                public void onResponse(@NonNull Call<List<Region>> call, @NonNull Response<List<Region>> response) {
                    List<Region> region = response.body();
                    if (region != null) {
                        boolean isTrue = syncDb_helper.InsertRegion(region);
                        if (isTrue) {
                            view.onGetZone("Done",true);
                        } else {
                            view.onGetZone("Error",false);
                        }
                    }else {
                        view.onGetZone("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Region>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetZone("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllArea(String empcode, String emprole) {
        //Area
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Area>> call = service.GetAreaByUser(empcode, emprole);
            call.enqueue(new Callback<List<Area>>() {
                @Override
                public void onResponse(@NonNull Call<List<Area>> call, @NonNull Response<List<Area>> response) {
                    List<Area> area = response.body();
                    if (area != null) {
                        boolean isTrue = syncDb_helper.InsertArea(area);
                        if (isTrue) {
                            view.onGetArea("Done",true);
                        } else {
                            view.onGetArea("Error",false);
                        }
                    }else {
                        view.onGetArea("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Area>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetArea("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllTeritory(String empcode, String emprole) {
//Teritorry
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Teritorry>> call = service.GetTeritoryByUser(empcode, emprole);
            call.enqueue(new Callback<List<Teritorry>>() {
                @Override
                public void onResponse(@NonNull Call<List<Teritorry>> call, @NonNull Response<List<Teritorry>> response) {
                    List<Teritorry> teritories = response.body();
                    if (teritories != null) {
                        boolean isTrue = syncDb_helper.InsertTeritorry(teritories);
                        if (isTrue) {
                            view.onGetTeritory("Done",true);
                        } else {
                            view.onGetTeritory("Error",false);
                        }
                    }else {
                        view.onGetTeritory("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Teritorry>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetTeritory("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllSTeritory(String empcode, String emprole) {
        //Subteritorry
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<SubTeritorry>> call = service.GetSubTeritoryByUser(empcode, emprole);
            call.enqueue(new Callback<List<SubTeritorry>>() {
                @Override
                public void onResponse(@NonNull Call<List<SubTeritorry>> call, @NonNull Response<List<SubTeritorry>> response) {
                    List<SubTeritorry> subTeritorries = response.body();
                    if (subTeritorries != null) {
                        boolean isTrue = syncDb_helper.InsertSubTeritorry(subTeritorries);
                        if (isTrue) {
                            view.onGetSTeritory("Done",true);
                        } else {
                            view.onGetSTeritory("Error",false);
                        }
                    }else {
                        view.onGetSTeritory("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<SubTeritorry>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetSTeritory("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllMarket(String empcode, String emprole) {
        //Market
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Market>> call = service.GetMarketByUser(empcode, emprole);
            call.enqueue(new Callback<List<Market>>() {
                @Override
                public void onResponse(@NonNull Call<List<Market>> call, @NonNull Response<List<Market>> response) {
                    List<Market> markets = response.body();
                    if (markets != null) {
                        boolean isTrue = syncDb_helper.InsertMarket(markets);
                        if (isTrue) {
                            view.onGetMarket("Done",true);
                        } else {
                            view.onGetMarket("Error",false);
                        }
                    }else {
                        view.onGetMarket("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Market>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetMarket("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllCustomer(int empid) {
        try {
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<Customer>> call = service.GetCustomerByUser(empid);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Customer>>() {
                @Override
                public void onResponse(@NonNull Call<List<Customer>> call, @NonNull Response<List<Customer>> response) {
                    List<Customer> customers = response.body();
                    if (customers != null &&customers.size()>0) {
                        boolean isTrue = syncDb_helper.InsertCustomerList(customers);
                        if (isTrue) {
                            view.onGetCustomer("Done",true);
                        } else {
                            view.onGetCustomer("Error",false);
                        }
                    }else {
                        view.onGetCustomer("Done",true);
                    }

                    Constants.LastSyncTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                }

                @Override
                public void onFailure(@NonNull Call<List<Customer>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetCustomer("Error", false);
                }
            });

        } catch (Exception ex) {
        }

    }

    @Override
    public void cllCustomerStation(int empid) {
//Customer Station
        try {
            apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
            Call<List<StationType>> call = service.GetStation();
            call.enqueue(new Callback<List<StationType>>() {
                @Override
                public void onResponse(@NonNull Call<List<StationType>> call, @NonNull Response<List<StationType>> response) {
                    List<StationType> station = response.body();
                    if (station != null) {
                        boolean isTrue = syncDb_helper.InsertCustomerStation(station);
                        if (isTrue){
                            view.onGetCustomerStation("Done",true);
                        } else {
                            view.onGetCustomerStation("Error",false);
                        }
                    }else {
                        view.onGetCustomerStation("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<StationType>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetCustomerStation("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllDoctor(int empid) {
        try {
            //All Doctor Api call
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorListViewModel>> call = service.GetDoctorList(empid);
            call.enqueue(new Callback<List<DoctorListViewModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorListViewModel>> call, @NonNull Response<List<DoctorListViewModel>> response) {
                    List<DoctorListViewModel> doctors = response.body();
                    if (doctors != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorList(doctors);
                        if (isTrue) {
                            view.onGetDoctor("Done",true);
                        } else {
                            view.onGetDoctor("Error",false);
                        }
                    }else {
                        view.onGetDoctor("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<DoctorListViewModel>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetDoctor("Error", false);
                }
            });
        } catch (Exception ex) {
        }

    }

    @Override
    public void cllDesignation(int empid) {
        //Designation
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorDesignation>> call = service.GetDoctorDesignation();
            call.enqueue(new Callback<List<DoctorDesignation>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorDesignation>> call, @NonNull Response<List<DoctorDesignation>> response) {
                    List<DoctorDesignation> docDesig = response.body();
                    if (docDesig != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorDesig(docDesig);
                        if (isTrue) {
                            view.onGetDesignation("Done",true);
                        } else {
                            view.onGetDesignation("Error",false);
                        }
                    }else {
                        view.onGetDesignation("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<DoctorDesignation>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetDesignation("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllDegree(int empid) {
        //Degree Api Call
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorDegreeViewModel>> call = service.GetDoctorDegree();
            call.enqueue(new Callback<List<DoctorDegreeViewModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorDegreeViewModel>> call, @NonNull Response<List<DoctorDegreeViewModel>> response) {
                    List<DoctorDegreeViewModel> docdeg = response.body();
                    if (docdeg != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorDegree(docdeg);
                        if (isTrue) {
                            view.onGetDegree("Done",true);
                        } else {
                            view.onGetDegree("Error",false);
                        }
                    }else {
                        view.onGetDegree("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<DoctorDegreeViewModel>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetDegree("Error", false);
                }
            });
        } catch (Exception ex) {
        }
    }

    @Override
    public void cllSpeciality(int empid) {
        //Speciality
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorSpecialityViewModel>> call = service.GetDoctorSpeciality();
            call.enqueue(new Callback<List<DoctorSpecialityViewModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorSpecialityViewModel>> call, @NonNull Response<List<DoctorSpecialityViewModel>> response) {
                    List<DoctorSpecialityViewModel> docSpc = response.body();
                    if (docSpc != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorSpecility(docSpc);
                        if (isTrue){
                            view.onGetSpeciality("Done",true);
                        } else {
                            view.onGetSpeciality("Error",false);
                        }
                    }else {
                        view.onGetSpeciality("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<DoctorSpecialityViewModel>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetSpeciality("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllSpecialday(int empid) {
        //Special day
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<SpecialDay>> call = service.GetDoctorSpecialday();
            call.enqueue(new Callback<List<SpecialDay>>() {
                @Override
                public void onResponse(@NonNull Call<List<SpecialDay>> call, @NonNull Response<List<SpecialDay>> response) {
                    List<SpecialDay> docSpc = response.body();
                    if (docSpc != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorSpecialDay(docSpc);
                        if (isTrue){
                            view.onGetSpecialday("Done",true);
                        } else {
                            view.onGetSpecialday("Error",false);
                        }
                    }else {
                        view.onGetSpecialday("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<SpecialDay>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetSpecialday("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllInstitution(int empid) {
        //Institution
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<InstitutionVM>> call = service.GetDoctorInstitution();
            call.enqueue(new Callback<List<InstitutionVM>>() {
                @Override
                public void onResponse(@NonNull Call<List<InstitutionVM>> call, @NonNull Response<List<InstitutionVM>> response) {
                    List<InstitutionVM> docIns = response.body();
                    if (docIns != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorInstitution(docIns);
                        if (isTrue) {
                            view.onGetInstitution("Done",true);
                        } else {
                            view.onGetInstitution("Error",false);
                        }
                    }else {
                        view.onGetInstitution("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<InstitutionVM>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetInstitution("Error", false);
                }
            });

        } catch (Exception ex) {
        }

    }

    @Override
    public void cllChamberType(int empid) {
//chamberType
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorChamberTypeVM>> call = service.GetDoctorChamber();
            call.enqueue(new Callback<List<DoctorChamberTypeVM>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorChamberTypeVM>> call, @NonNull Response<List<DoctorChamberTypeVM>> response) {
                    List<DoctorChamberTypeVM> docChm = response.body();
                    if (docChm != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorChembar(docChm);
                        if (isTrue){
                            view.onGetChamberType("Done",true);
                        } else {
                            view.onGetChamberType("Error",false);
                        }
                    }else {
                        view.onGetChamberType("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<DoctorChamberTypeVM>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetChamberType("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllChamberName(int empid) {
//chamberName
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorChamberName>> call = service.GetChemberName(empid);
            call.enqueue(new Callback<List<DoctorChamberName>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorChamberName>> call, @NonNull Response<List<DoctorChamberName>> response) {
                    List<DoctorChamberName> docChm = response.body();
                    if (docChm != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorChembarName(docChm);
                        if (isTrue){
                            view.onGetChamberName("Done",true);
                        } else {
                            view.onGetChamberName("Error",false);
                        }
                    }else {
                        view.onGetChamberName("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<DoctorChamberName>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetChamberName("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllBrand(int empid) {
        //brand
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<Brand>> call = service.GetDoctorBradType();
            call.enqueue(new Callback<List<Brand>>() {
                @Override
                public void onResponse(@NonNull Call<List<Brand>> call, @NonNull Response<List<Brand>> response) {
                    List<Brand> docChm = response.body();
                    if (docChm != null) {
                        boolean isTrue = syncDb_helper.InsertBrand(docChm);
                        if (isTrue) {
                            view.onGetBrand("Done",true);
                        } else {
                            view.onGetBrand("Error",false);
                        }
                    }else {
                        view.onGetBrand("Done",true);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<Brand>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetBrand("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllDoccategory(int empid) {
        //Doc Category
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorCategory>> call = service.GetDoctorCategory();
            call.enqueue(new Callback<List<DoctorCategory>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorCategory>> call, @NonNull Response<List<DoctorCategory>> response) {
                    List<DoctorCategory> docChm = response.body();
                    if (docChm != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorCategory(docChm);
                        if (isTrue){
                            view.onGetDoccategory("Done",true);
                        } else {
                            view.onGetDoccategory("Error",false);
                        }
                    }else {
                        view.onGetDoccategory("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<DoctorCategory>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetDoccategory("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllProgramtypey(int empid) {
        //program type
      /*  try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<ProgramType>> call = service.GetDoctorProgramType();
            call.enqueue(new Callback<List<ProgramType>>() {
                @Override
                public void onResponse(Call<List<ProgramType>> call, Response<List<ProgramType>> response) {
                    List<ProgramType> docChm = response.body();
                    if (docChm != null) {
                        boolean isTrue = syncDb_helper.InsertProgramType(docChm);
                        if (isTrue) {
                            view.onGetProgramtypey("Done",true);
                        } else {
                            view.onGetProgramtypey("Error",false);
                        }
                    }else {
                        view.onGetProgramtypey("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<ProgramType>> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }*/
    }

    @Override
    public void cllUserRole(int empid) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<UserRole>> call = service.GetUserRole();
            call.enqueue(new Callback<List<UserRole>>() {
                @Override
                public void onResponse(Call<List<UserRole>> call, Response<List<UserRole>> response) {
                    List<UserRole> Role = response.body();
                    if (Role != null) {
                        boolean isTrue = syncDb_helper.InsertRole(Role);
                        if (isTrue) {
                            view.onGetUserRole("Done",true);
                        } else {
                            view.onGetUserRole("Error",false);
                        }
                    }else {
                        view.onGetUserRole("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<UserRole>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetUserRole("Error", false);
                }
            });

        } catch (Exception ex) {
        }

    }

    @Override
    public void cllUserByRole(int empid) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<UserByRole>> call = service.GetUserByRole(empid);
            HttpUrl ds = call.request().url();

            call.enqueue(new Callback<List<UserByRole>>() {
                @Override
                public void onResponse(Call<List<UserByRole>> call, Response<List<UserByRole>> response) {
                    List<UserByRole> userByRole = response.body();
                    if (userByRole != null) {
                        boolean isTrue = syncDb_helper.InsertRoleUser(userByRole);
                        if (isTrue){
                            view.onGetUserByRole("Done",true);
                        } else {
                            view.onGetUserByRole("Error",false);
                        }
                    }else {
                        view.onGetUserByRole("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<UserByRole>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetUserByRole("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllProduct(int empid) {
        //product
        try {
            ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<Product>> call = service.GetProductList(empid);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    List<Product> productList = response.body();
                    if (productList != null) {
                        boolean isTrue = syncDb_helper.InsertProducts(productList);
                        if (isTrue) {
                            view.onGetProduct("Done",true);
                        } else {
                            view.onGetProduct("Error",false);
                        }
                    }else {
                        view.onGetProduct("Done",true);
                    }

                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetProduct("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllProductSample(int empid) {
        //productSample
        try {
            ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<ProductSample>> call = service.GetSampleProductsList(empid);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<ProductSample>>() {
                @Override
                public void onResponse(@NonNull Call<List<ProductSample>> call, @NonNull Response<List<ProductSample>> response) {
                    List<ProductSample> productList = response.body();
                    if (productList != null) {
                        boolean isTrue = syncDb_helper.InsertProductSample(productList);
                        if (isTrue) {
                            view.onGetProductSample("Done",true);
                        } else {
                            view.onGetProductSample("Error",false);
                        }
                    }else {
                        view.onGetProductSample("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ProductSample>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetProductSample("Error", false);
                }
            });

        } catch (Exception ex) {

        }
    }

    @Override
    public void cllProductGift(int empid) {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<Gift>> call = service.GetProductForDcrByType(empid, "Gift");

            call.enqueue(new Callback<List<Gift>>() {
                @Override
                public void onResponse(Call<List<Gift>> call, Response<List<Gift>> response) {
                    List<Gift> giftList = response.body();
                    if (giftList != null) {
                        boolean isTrue = syncDb_helper.InsertProductGift(giftList);
                        if (isTrue){
                            view.onGetProductGift("Done",true);
                        } else {
                            view.onGetProductGift("Error",false);
                        }
                    }else {
                        view.onGetProductGift("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<Gift>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetProductGift("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllQuotedPrice(int empid) {
//Quoted Price
        try {
            OrderProcessAPICALL service = RetrofitClientOrderProcessInstance.getRetrofitInstance().create(OrderProcessAPICALL.class);
            Call<List<QuotedPrice>> call = service.GetQuotedPrice();
            call.enqueue(new Callback<List<QuotedPrice>>() {
                @Override
                public void onResponse(@NonNull Call<List<QuotedPrice>> call, @NonNull Response<List<QuotedPrice>> response) {
                    List<QuotedPrice> aList = response.body();
                    if (aList != null) {
                        boolean isTrue = dbCrudHelper.InsertQuotedPriceInfo_SQLite(aList);
                        if (isTrue){
                            view.onGetQuotedPrice("Done",true);
                        } else {
                            view.onGetQuotedPrice("Error",false);
                        }
                    }else {
                        view.onGetQuotedPrice("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<QuotedPrice>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetQuotedPrice("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllDoctorType(int empId, int year) {
        //Doctor Type Api Call
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorTypeVM>> call = service.GetDoctorType();
            call.enqueue(new Callback<List<DoctorTypeVM>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorTypeVM>> call, Response<List<DoctorTypeVM>> response) {
                    List<DoctorTypeVM> doctyps = response.body();
                    if (doctyps != null) {
                        boolean isTrue = syncDb_helper.InsertDoctorType(doctyps);
                        if (isTrue) {
                            view.onGetDoctorType("Done",true);
                        } else {
                            view.onGetDoctorType("Error",false);
                        }
                    }else {
                        view.onGetDoctorType("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<DoctorTypeVM>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetDoctorType("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllCustomerType(int empId, int year) {
        //Customer Type
        try {
            apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
            Call<List<CustomerType>> call = service.GetCustomerType();
            call.enqueue(new Callback<List<CustomerType>>() {
                @Override
                public void onResponse(@NonNull Call<List<CustomerType>> call, @NonNull Response<List<CustomerType>> response) {
                    List<CustomerType> custtyps = response.body();
                    if (custtyps != null) {
                        boolean isTrue = syncDb_helper.InsertCustomerType(custtyps);
                        if (isTrue) {
                            view.onGetCustomerType("Done",true);
                        } else {
                            view.onGetCustomerType("Error",false);
                        }
                    }else {
                        view.onGetCustomerType("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<CustomerType>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetCustomerType("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllDoctorContactType(int empId, int yeard) {
//Doctor Contact Type
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<ContactTypeVM>> call = service.GetDoctorContactType();
            call.enqueue(new Callback<List<ContactTypeVM>>() {
                @Override
                public void onResponse(Call<List<ContactTypeVM>> call, Response<List<ContactTypeVM>> response) {
                    List<ContactTypeVM> contatyps = response.body();
                    if (contatyps != null) {
                        boolean isTrue = syncDb_helper.InsertContType(contatyps);
                        if (isTrue) {
                            view.onGetDoctorContactType("Done",true);
                        } else {
                            view.onGetDoctorContactType("Error",false);
                        }
                    }else {
                        view.onGetDoctorContactType("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<ContactTypeVM>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetDoctorContactType("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllExpenseType(int empId, int year,String role) {
//Expense Type
        try {
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<ExpenseTypeMaster>> call = service.Get_ExpenseType(role,String.valueOf(empId));
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<ExpenseTypeMaster>>() {
                @Override
                public void onResponse(Call<List<ExpenseTypeMaster>> call, Response<List<ExpenseTypeMaster>> response) {
                    List<ExpenseTypeMaster> exptyps = response.body();
                    if (exptyps != null) {
                        boolean isTrue = syncDb_helper.InsertExpType(exptyps);
                        if (isTrue) {
                            view.onGetExpenseType("Done",true);
                        } else {
                            view.onGetExpenseType("Error",false);
                        }
                    }else {
                        view.onGetExpenseType("Done",true);
                    }

                }

                @Override
                public void onFailure(Call<List<ExpenseTypeMaster>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetExpenseType("Error", false);
                }
            });

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void cllLeaveType(int empId, int year) {
        //Leave Type
        try {
            UserProcessAPI service = RetrofitClientInstance.getRetrofitInstance().create(UserProcessAPI.class);
            Call<List<LeaveTypeInfo>> call = service.GetLeaveType(empId, year);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<LeaveTypeInfo>>() {
                @Override
                public void onResponse(Call<List<LeaveTypeInfo>> call, Response<List<LeaveTypeInfo>> response) {
                    List<LeaveTypeInfo> leavetyps = response.body();
                    if (leavetyps != null) {
                        boolean isTrue = syncDb_helper.InsertLeaveType(leavetyps);
                        if (isTrue) {
                            view.onGetLeaveType("Done",true);
                        } else {
                            view.onGetLeaveType("Error",false);
                        }
                    }else {
                        view.onGetLeaveType("Done",true);
                    }

                }

                @Override
                public void onFailure(Call<List<LeaveTypeInfo>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetLeaveType("Error", false);
                }
            });

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void cllPrescriptionType(int empId, int year) {

        //Prescription Type
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<PrescriptionTYpe>> call = service.GetPrescriptionType(empId);
            call.enqueue(new Callback<List<PrescriptionTYpe>>() {
                @Override
                public void onResponse(Call<List<PrescriptionTYpe>> call, Response<List<PrescriptionTYpe>> response) {
                    List<PrescriptionTYpe> prestype = response.body();
                    if (prestype != null) {
                        boolean isTrue = syncDb_helper.InsertPrescripType(prestype);
                        if (isTrue){
                            view.onGetPrescriptionType("Done",true);
                        } else {
                            view.onGetPrescriptionType("Error",false);
                        }
                    }else {
                        view.onGetPrescriptionType("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<PrescriptionTYpe>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetPrescriptionType("Error", false);
                }
            });

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void cllDivision() {

        //Prescription Type
        try {
            apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
            Call<List<DivisionVM>> call = service.GetDivisionAll();
            call.enqueue(new Callback<List<DivisionVM>>() {
                @Override
                public void onResponse(Call<List<DivisionVM>> call, Response<List<DivisionVM>> response) {
                    List<DivisionVM> prestype = response.body();
                    if (prestype != null) {
                        boolean isTrue = syncDb_helper.InsertDivision(prestype);
                        if (isTrue){
                            view.onGetPrescriptionType("Done",true);
                        } else {
                            view.onGetPrescriptionType("Error",false);
                        }
                    }else {
                        view.onGetPrescriptionType("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<DivisionVM>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetPrescriptionType("Error", false);
                }
            });

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void cllDistrict() {

        //Prescription Type
        try {
            apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
            Call<List<DistrictVM>> call = service.GetDistrict();
            call.enqueue(new Callback<List<DistrictVM>>() {
                @Override
                public void onResponse(Call<List<DistrictVM>> call, Response<List<DistrictVM>> response) {
                    List<DistrictVM> prestype = response.body();
                    if (prestype != null) {
                        boolean isTrue = syncDb_helper.InsertDistrict(prestype);
                        if (isTrue){
                            view.onGetPrescriptionType("Done",true);
                        } else {
                            view.onGetPrescriptionType("Error",false);
                        }
                    }else {
                        view.onGetPrescriptionType("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<DistrictVM>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetPrescriptionType("Error", false);
                }
            });

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void cllThana() {

        //Prescription Type
        try {
            apiSeedDataCall service = RetrofitClientInstance.getRetrofitInstance().create(apiSeedDataCall.class);
            Call<List<ThanaVM>> call = service.GetThana();
            call.enqueue(new Callback<List<ThanaVM>>() {
                @Override
                public void onResponse(Call<List<ThanaVM>> call, Response<List<ThanaVM>> response) {
                    List<ThanaVM> prestype = response.body();
                    if (prestype != null) {
                        boolean isTrue = syncDb_helper.InsertThana(prestype);
                        if (isTrue){
                            view.onGetPrescriptionType("Done",true);
                        } else {
                            view.onGetPrescriptionType("Error",false);
                        }
                    }else {
                        view.onGetPrescriptionType("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<ThanaVM>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetPrescriptionType("Error", false);
                }
            });

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void cllNonEffectivereason(int empId, int year) {
        //Non Effective reason
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<NonEffectiveReason>> call = service.GetNonEffectiveReason();
            call.enqueue(new Callback<List<NonEffectiveReason>>() {
                @Override
                public void onResponse(Call<List<NonEffectiveReason>> call, Response<List<NonEffectiveReason>> response) {
                    List<NonEffectiveReason> neffec = response.body();
                    if (neffec != null) {
                        boolean isTrue = syncDb_helper.InsertNonEffectiveReason(neffec);
                        if (isTrue) {
                            view.onGetNonEffectivereason("Done",true);
                        } else {
                            view.onGetNonEffectivereason("Error",false);
                        }
                    }else {
                        view.onGetNonEffectivereason("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<NonEffectiveReason>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetNonEffectivereason("Error", false);
                }
            });

        } catch (Exception ex) {
            Log.e("TAG", "onFailure: ", ex);
        }
    }

    @Override
    public void cllTransportList(int empId, int year) {
        //TransportList
        try {
            CalculationApiCall service = RetrofitClientInstance.getRetrofitInstance().create(CalculationApiCall.class);
            Call<List<Transport>> call = service.GetTransportList();
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Transport>>() {
                @Override
                public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                    List<Transport> transportList = response.body();
                    if (transportList != null) {
                        boolean isTrue = syncDb_helper.InsertTransport(transportList);
                        if (isTrue) {
                            view.onGetTransportList("Done",true);
                        } else {
                            view.onGetTransportList("Error",false);
                        }
                    }else {
                        view.onGetTransportList("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<Transport>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetTransportList("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllTourPurpose(int empId, int year) {
        //Tour Purpose
        try {
            TourApiCall service = RetrofitClientInstance.getRetrofitInstance().create(TourApiCall.class);
            Call<List<TourPurposeViewModel>> call = service.GetPurpose(empId);
            call.enqueue(new Callback<List<TourPurposeViewModel>>() {
                @Override
                public void onResponse(Call<List<TourPurposeViewModel>> call, Response<List<TourPurposeViewModel>> response) {
                    List<TourPurposeViewModel> tourPurposeList = response.body();
                    if (tourPurposeList != null) {
                        boolean isTrue = syncDb_helper.InsertTourPlanPurpose(tourPurposeList);
                        if (isTrue) {
                            view.onGetTourPurpose("Done",true);
                        } else {
                            view.onGetTourPurpose("Error",false);
                        }
                    }else {
                        view.onGetTourPurpose("Done",true);
                    }

                }

                @Override
                public void onFailure(Call<List<TourPurposeViewModel>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetTourPurpose("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void cllVisitType(int empId, int year) {
        //Visit Type
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<TourTypeViewModel>> call = service.GetVisitType();
            call.enqueue(new Callback<List<TourTypeViewModel>>() {
                @Override
                public void onResponse(Call<List<TourTypeViewModel>> call, Response<List<TourTypeViewModel>> response) {
                    List<TourTypeViewModel> visitType = response.body();
                    if (visitType != null) {
                        boolean isTrue = syncDb_helper.InsertVisitType(visitType);
                        if (isTrue) {
                            view.onGetVisitType("Done",true);
                        } else {
                            view.onGetVisitType("Error",false);
                        }

                    }else {
                        view.onGetVisitType("Done",true);
                    }
                }

                @Override
                public void onFailure(Call<List<TourTypeViewModel>> call, Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetVisitType("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void callProviderType() {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<ModelProviderType>> call = service.GetProviderType();
            call.enqueue(new Callback<List<ModelProviderType>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelProviderType>> call, @NonNull Response<List<ModelProviderType>> response) {
                    List<ModelProviderType> providerType = response.body();
                    if (providerType != null) {
                        boolean isTrue = syncDb_helper.InsertProviderType(providerType);
                        if (isTrue) {
                            view.onGetProviderType("Done",true);
                        } else {
                            view.onGetProviderType("Error",false);
                        }

                    }else {
                        view.onGetProviderType("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelProviderType>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetProviderType("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void callSMCType() {
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<ModelSMCType>> call = service.GetSMCType();
            call.enqueue(new Callback<List<ModelSMCType>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelSMCType>> call, @NonNull Response<List<ModelSMCType>> response) {
                    List<ModelSMCType> smcType = response.body();
                    if (smcType != null) {
                        boolean isTrue = syncDb_helper.InsertSMCType(smcType);
                        if (isTrue) {
                            view.onGetSMCType("Done",true);
                        } else {
                            view.onGetSMCType("Error",false);
                        }

                    }else {
                        view.onGetSMCType("Done",true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelSMCType>> call, @NonNull Throwable t) {
                    Log.e("DBEX", t.toString());
                    view.onGetSMCType("Error", false);
                }
            });

        } catch (Exception ex) {
        }
    }
}
