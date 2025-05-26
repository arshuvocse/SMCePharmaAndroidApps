package com.creatrix.salessolution.Presenter;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.creatrix.salessolution.DBAdapter.DBDoctor.DBDoctorHelper;
import com.creatrix.salessolution.DBAdapter.SyncDb_Helper;
import com.creatrix.salessolution.Interface.IDoctor;
import com.creatrix.salessolution.Model.Doctor.DoctorSM;
import com.creatrix.salessolution.Model.ResultInfo;
import com.creatrix.salessolution.Network.RetrofitClientInstance;
import com.creatrix.salessolution.Network.ApiDoctorCall;
import java.net.SocketTimeoutException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorPresenter implements IDoctor.Presenter {
    IDoctor.View view;
    Context context;
    DBDoctorHelper dbDoctorHelper;
    SyncDb_Helper syncDbHelper;

    public DoctorPresenter(IDoctor.View view, Context context) {
        this.view = view;
        this.context = context;
        dbDoctorHelper = new DBDoctorHelper(context);
        syncDbHelper = new SyncDb_Helper(context);
    }

    @Override
    public void GetDegree(int doctypeId) {

        try {
            view.onDegreeReceived(dbDoctorHelper.getDegreeListFromSQLite(doctypeId));

        } catch (Exception ex) {
        }

    }

    @Override
    public void GetSpeciality() {
        try {
            view.onSpecialityReceived(dbDoctorHelper.getSpecListFromSQLite());

        } catch (Exception ex) {
        }

    }

    @Override
    public void GetDoctorType(int id) {
        try {
            view.onDoctorTypeReceived(dbDoctorHelper.getDoctorTypeListFromSQLite(id));

        } catch (Exception ex) {
        }

    }

    @Override
    public void GetInstitute() {
        try {
            view.onInstituteReceived(dbDoctorHelper.getInstitutionListFromSQLite());
        } catch (Exception ex) {
        }

    }

    @Override
    public void GetBrand() {
        try {
            view.onBrandReceived(dbDoctorHelper.getBrandListFromSQLite());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void GetDoctorCategory(int id) {
        try {
            view.onDocCategoryReceived(dbDoctorHelper.getDocCategoryListFromSQLite(id));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void GetChamber(int id) {
        try {
           view.onChamberReceived(dbDoctorHelper.getChamberListFromSQLite(id));
        } catch (Exception ex) {
        }

    }

    @Override
    public void GetContactType(int id) {
        try {
            view.onContactTypeReceived(dbDoctorHelper.getContactTypeListFromSQLite(id));
        } catch (Exception ex) {
        }

    }
    @Override
    public void GetProviderType(int id) {
        try {
            view.onProviderTypeReceived(dbDoctorHelper.getProviderTypeListFromSQLite(id,1));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void GetSMCType(int id) {
        try {
            view.onSMCTypeReceived(dbDoctorHelper.getSMCTypeListForDocFromSQLite(0,1));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void GetSpecialType(int id) {
        try {
            view.onSpecialTypeReceived(dbDoctorHelper.getSpecialdayTypeListFromSQLite(id));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void SaveDoctor(DoctorSM aDoc,String type) {
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Doctor is Saving.... Please wait");
        progressDoalog.show();
        progressDoalog.setCanceledOnTouchOutside(false);
        try {
            ApiDoctorCall service = RetrofitClientInstance.getRetrofitInstance().create(ApiDoctorCall.class);
            Call<ResultInfo> call = service.Save(aDoc);
            call.enqueue(new Callback<ResultInfo>() {
                @Override
                public void onResponse(@NonNull Call<ResultInfo> call, @NonNull Response<ResultInfo> response) {
                    progressDoalog.dismiss();
                    ResultInfo info = response.body();
                   // Toast.makeText(context, "body "+info, Toast.LENGTH_SHORT).show();
                    if (info != null) {
                        if (info.getSuccess()) {

                            if(type.equals("Submit"))
                            {
                                view.onSubmitSuccess("Submit");
                            }
                            if(type.equals("Update"))
                            {
                                view.onSubmitSuccess("Update");
                            }

                        } else {
                            view.onSubmitError("Slow Internet Detected..Please try again");
                        }

                    } else {
                        view.onSubmitError("Slow Internet Detected..Please try again");

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultInfo> call, @NonNull Throwable t) {
                    progressDoalog.dismiss();
                    if (t instanceof SocketTimeoutException) {
                        view.onSubmitError("Slow Internet Detected..Please try again");
                    } else {
                        view.onSubmitError("Some error occurred..Please try again");
                    }
                }
            });

        } catch (Exception ex) {
            progressDoalog.dismiss();
            String str = ex.toString();
            Log.e("Exception", str);
            view.onSubmitError("Some error occurred..Please try again");

        }


    }

    @Override
    public void GetDoctorDesignation(int id) {
        try {
            view.onDoctorDesignationGet(dbDoctorHelper.getDesigListFromSQLite(id));
        } catch (Exception ex) {
        }
    }
}
