package com.creatrix.salessolution.Interface;

import com.creatrix.salessolution.Model.Customer;
import com.creatrix.salessolution.Model.Product;
import com.creatrix.salessolution.Model.ProductSample;
import com.creatrix.salessolution.Model.mCompanyUnit;

import java.util.ArrayList;
import java.util.List;

public interface IProduct {

    interface Presenter{
        void getProductsFromServer(int empId);
        void getCompanyUnitFromServer(int empId);
        void InsertIntoProductTable(List<Product> aProduct);
        void getProductFromDB(int empId);
        ArrayList<mCompanyUnit> getCompanyUnitFromDB();

        void getSampleProducts(int empId);
        void getRegularProducts(int empId);
    }

    interface View{
        void OnError(String message);
        void onProductsGet(List<Product> aList);
        void onProductSampleGet(List<ProductSample> aList);
    }
}
