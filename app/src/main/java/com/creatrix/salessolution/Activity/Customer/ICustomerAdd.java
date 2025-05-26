package com.creatrix.salessolution.Activity.Customer;

import com.creatrix.salessolution.Model.CustomerType;
import com.creatrix.salessolution.Model.DistrictVM;
import com.creatrix.salessolution.Model.DivisionVM;
import com.creatrix.salessolution.Model.Doctor.ProgramType;
import com.creatrix.salessolution.Model.MarketViewModel;
import com.creatrix.salessolution.Model.ModelProviderType;
import com.creatrix.salessolution.Model.ModelSMCType;
import com.creatrix.salessolution.Model.StationType;
import com.creatrix.salessolution.Model.TerritoryViewModel;
import com.creatrix.salessolution.Model.ThanaVM;

import java.util.List;

public interface ICustomerAdd {
    interface Presenter {
        // void GetTerritory(int empId);
        // void GetMarket(int territoryId);
        // void GetDivision();
        // void GetDistrict();
        // void GetThana(int id);
        void GetProgramType();
        void GetProviderType();
        void GetSMCType();
        void GetCustomerType();
        void GetStationType();
        void SaveCustomer(CustomerSvModel aInfo,String who);

    }

    interface View {
        /* void onTerritoryReceived(List<TerritoryViewModel> aList);
         void onMarketReceived(List<MarketViewModel> aList);
         void onDivisionReceived(List<DivisionVM> aList);
         void onDistrictReceived(List<DistrictVM> aList);
         void onThanaReceived(List<ThanaVM> aList);*/
        void onProgramType(List<ProgramType> ptype);
        void onProviderType(List<ModelProviderType> ptype);
        void onSMCType(List<ModelSMCType> ptype);


        void onCustomerTypeReceived(List<CustomerType> aList);
        void onStationReceived(List<StationType> aList);
        void onSubmitSuccess(String mesg,String who);
        void onSubmitError(String mesg);

    }

}
