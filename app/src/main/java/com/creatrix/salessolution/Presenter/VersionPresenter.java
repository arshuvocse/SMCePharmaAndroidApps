package com.creatrix.salessolution.Presenter;
import androidx.annotation.NonNull;

import com.creatrix.salessolution.Interface.IVersionUpdate;
import com.creatrix.salessolution.Model.AppVersionModel;
import com.creatrix.salessolution.Network.GetDataService;
import com.creatrix.salessolution.Network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionPresenter implements IVersionUpdate.Presenter {
    IVersionUpdate.View versionview;
    public VersionPresenter(IVersionUpdate.View versionview) {
        this.versionview=versionview;
    }
    @Override
    public void GetActiveVersion() {
        try{
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<AppVersionModel> call = service.GetAppVersionInfo();
            call.enqueue(new Callback<AppVersionModel>() {
                @Override
                public void onResponse(@NonNull Call<AppVersionModel> call, @NonNull Response<AppVersionModel> response) {
                    AppVersionModel aInfo = response.body();
                    assert aInfo != null;
                    versionview.onVersionGet(aInfo.getVersionName());
                }
                @Override
                public void onFailure(@NonNull Call<AppVersionModel> call, @NonNull Throwable t) {
                }
            });
        }catch (Exception ex){
        }
    }
}
