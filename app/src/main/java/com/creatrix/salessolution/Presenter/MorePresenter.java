package com.creatrix.salessolution.Presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.creatrix.salessolution.DBAdapter.DBCrudHelper;
import com.creatrix.salessolution.DBAdapter.ProductSQLiteHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.DBInfo.DBHelperMain;
import com.creatrix.salessolution.Interface.IMore;
import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.Doctor.DoctorListViewModel;
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
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Network.ApiCustomerCall;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import com.creatrix.salessolution.Network.ApiMasterSync;
import com.creatrix.salessolution.Network.ProductApi;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
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

public class MorePresenter implements IMore.Presenter {
    IMore.View view;
    ProgressDialog progressDoalog;
    DBHelperMain dbHelper;
    DBCrudHelper crudHelper;
    SyncDb_Helper syncDbHelper;
    Context context;
    ProductSQLiteHelper productSQLiteHelper;
    boolean syncdone=false;
    boolean isMIODone, isASMDone, isRSMDone, isNSMDone;
    boolean isGroupDone;
    boolean isRegionDone;
    boolean isAreaDone;
    boolean isTeritorryDone;
    boolean isSubTeritorryDone;
    boolean isMarketDone;

    public MorePresenter(IMore.View view, Context context) {
        this.view = view;
        this.dbHelper = new DBHelperMain(context);
        this.crudHelper = new DBCrudHelper(context);
        this.syncDbHelper = new SyncDb_Helper(context);
        this.context = context;
        this.productSQLiteHelper = new ProductSQLiteHelper(context);
    }
    public void doCustomerSync(int empId) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Customer Syncing.... Please Wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            ApiCustomerCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiCustomerCall.class);
            Call<List<Customer>> call = service.GetCustomerByUser(empId);
            call.enqueue(new Callback<List<Customer>>() {
                @Override
                public void onResponse(@NonNull Call<List<Customer>> call, @NonNull Response<List<Customer>> response) {
                    try {
                       // syncDbHelper.InsertCustomerList(response.body());
                        List<Customer> custList = response.body();
                        if (custList != null) {
                            boolean isTrue = syncDbHelper.InsertCustomerList(custList);
                            if (isTrue) {
                                progressDoalog.dismiss();
                                view.onCustomerSync("Customer Sync is Successful");

                            } else {
                                progressDoalog.dismiss();
                                view.onError("Some Error Occurred Syncing Customer");
                            }
                        }
                        // crudHelper.InsertCustomerInfo_SQLite(response.body());
                        //view.onCustomerSync("Done");
                        Constants.LastSyncTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Customer>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    view.onError("Some Error Occurred Syncing Customer ");
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            //view.onError("Some Error Occurred Syncing Customer ");
            view.onError("Please Try Again");
        }

    }

    @Override
    public void doProductSync(int empId) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Product Syncing....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            ProductApi service = RetrofitClientInstance.getRetrofitInstance().create(ProductApi.class);
            Call<List<Product>> call = service.GetProductList(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                    List<Product> productList = response.body();
                    if (productList != null) {
                        boolean isTrue = syncDbHelper.InsertProducts(productList);
                        if (isTrue) {
                            progressDoalog.dismiss();
                            view.onProductSync("Product Sync is Successful");

                        } else {
                            progressDoalog.dismiss();
                            view.onProductSync("Product Sync is Successful");
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {

                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onError("Some Error Occurred Syncing Product ");
                    } else {
                        view.onError("Some Error occured");

                    }


                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public void doDoctorSync(int empId) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Doctor Syncing....");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            //All Doctor Api call
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<List<DoctorListViewModel>> call = service.GetDoctorList(empId);
            HttpUrl ds = call.request().url();
            call.enqueue(new Callback<List<DoctorListViewModel>>() {
                @Override
                public void onResponse(@NonNull Call<List<DoctorListViewModel>> call, @NonNull Response<List<DoctorListViewModel>> response) {
                    List<DoctorListViewModel> doctors = response.body();
                    if (doctors != null) {
                        boolean isTrue = syncDbHelper.InsertDoctorList(doctors);
                        progressDoalog.dismiss();
                        if (isTrue) {
                            view.onDoctorSync("Doctor Sync is Successful",true);

                        } else {
                            //DoctorInfoGetView("Error");
                            view.onDoctorSync("Slow Network detected",false);
                        }
                    }else {
                        view.onDoctorSync("Doctor Sync is Successful",true);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<DoctorListViewModel>> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onError("Some Error occured");
                    } else {
                        view.onError("Some Error occured");

                    }
                }
            });
        } catch (Exception ex) {
        }
    }

    @Override
    public void doOtherSync(String empcode, String emprole) {
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<NSM>> call = service.GetNSM(empcode, emprole);
            call.enqueue(new Callback<List<NSM>>() {
                @Override
                public void onResponse(@NonNull Call<List<NSM>> call, @NonNull Response<List<NSM>> response) {
                    List<NSM> teritories = response.body();
                    if (teritories != null) {
                        boolean isTrue = syncDbHelper.InsertNSMInfo(teritories);
                        syncdone=true;
                        if (isTrue) {
                            isNSMDone = true;
                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<NSM>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
        //RSM
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<RSM>> call = service.GetRSM(empcode, emprole);
            call.enqueue(new Callback<List<RSM>>() {
                @Override
                public void onResponse(@NonNull Call<List<RSM>> call, @NonNull Response<List<RSM>> response) {
                    List<RSM> rsm = response.body();
                    if (rsm != null) {
                        boolean isTrue = syncDbHelper.InsertRSMInfo(rsm);
                        syncdone=true;
                        if (isTrue) {
                            isRSMDone = true;
                            // viewBindings.teritoryTxt.setText("Done");
                        } else {
                            // viewBindings.teritoryTxt.setText("Error");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<RSM>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
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
                        boolean isTrue = syncDbHelper.InsertASMInfo(rsm);
                        syncdone=true;
                        if (isTrue) {
                            isASMDone = true;
                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ASM>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
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
                        boolean isTrue = syncDbHelper.InsertMIOInfo(mio);
                        syncdone=true;
                        if (isTrue) {
                            isMIODone = true;
                            //viewBindings.teritoryTxt.setText("Done");

                        } else {
                            // viewBindings.teritoryTxt.setText("Error");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<MIO>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }

        //Group
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Group>> call = service.GetGroupByUser(empcode, emprole);
            call.enqueue(new Callback<List<Group>>() {
                @Override
                public void onResponse(@NonNull Call<List<Group>> call, @NonNull Response<List<Group>> response) {
                    List<Group> group = response.body();
                    if (group != null) {
                        boolean isTrue = syncDbHelper.InsertGroup(group);
                        syncdone=true;
                        if (isTrue) {
                            isGroupDone = true;
                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Group>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
        //Region
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Region>> call = service.GetRegionByUser(empcode, emprole);
            call.enqueue(new Callback<List<Region>>() {
                @Override
                public void onResponse(@NonNull Call<List<Region>> call, @NonNull Response<List<Region>> response) {
                    List<Region> region = response.body();
                    if (region != null) {
                        boolean isTrue = syncDbHelper.InsertRegion(region);
                        syncdone=true;
                        if (isTrue) {
                            isRegionDone = true;
                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Region>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
        //Area
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Area>> call = service.GetAreaByUser(empcode, emprole);
            call.enqueue(new Callback<List<Area>>() {
                @Override
                public void onResponse(@NonNull Call<List<Area>> call, @NonNull Response<List<Area>> response) {
                    List<Area> area = response.body();
                    if (area != null) {
                        boolean isTrue = syncDbHelper.InsertArea(area);
                        syncdone=true;
                        if (isTrue) {
                            isAreaDone = true;
                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Area>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
        //Teritorry
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Teritorry>> call = service.GetTeritoryByUser(empcode, emprole);
            call.enqueue(new Callback<List<Teritorry>>() {
                @Override
                public void onResponse(@NonNull Call<List<Teritorry>> call, @NonNull Response<List<Teritorry>> response) {
                    List<Teritorry> teritories = response.body();
                    if (teritories != null) {
                        boolean isTrue = syncDbHelper.InsertTeritorry(teritories);
                        syncdone=true;
                        if (isTrue) {
                            isTeritorryDone = true;
                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Teritorry>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
        //Subteritorry
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<SubTeritorry>> call = service.GetSubTeritoryByUser(empcode, emprole);
            call.enqueue(new Callback<List<SubTeritorry>>() {
                @Override
                public void onResponse(@NonNull Call<List<SubTeritorry>> call, @NonNull Response<List<SubTeritorry>> response) {
                    List<SubTeritorry> subTeritorries = response.body();
                    if (subTeritorries != null) {
                        boolean isTrue = syncDbHelper.InsertSubTeritorry(subTeritorries);
                        syncdone=true;
                        if (isTrue) {
                            isSubTeritorryDone = true;
                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<SubTeritorry>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }
        //Market
        try {
            ApiMasterSync service = RetrofitClientInstance.getRetrofitInstance().create(ApiMasterSync.class);
            Call<List<Market>> call = service.GetMarketByUser(empcode, emprole);
            call.enqueue(new Callback<List<Market>>() {
                @Override
                public void onResponse(@NonNull Call<List<Market>> call, @NonNull Response<List<Market>> response) {
                    List<Market> markets = response.body();
                    if (markets != null) {
                        boolean isTrue = syncDbHelper.InsertMarket(markets);
                        syncdone=true;
                        if (isTrue) {
                            isMarketDone = true;
                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Market>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                    } else {
                    }
                }
            });

        } catch (Exception ex) {
        }


        try {
            if(syncdone=true)
            {
                if(isGroupDone && isRegionDone && isAreaDone && isTeritorryDone && isSubTeritorryDone && isMarketDone && isMIODone && isASMDone
                        && isRSMDone && isNSMDone)
                {
                    view.onOtherSync("Market Structure Sync Successful");
                }else {
                    view.onOtherSync("Market Structure Sync Successful");
                }
            }

        } catch (Exception exception) {
        }
    }


}
