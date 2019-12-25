package com.tevoi.tevoi.model;

import java.util.ArrayList;
import java.util.List;

public class RegisterDataResponse extends IResponse{
    List<CountryObject> lstCountry;
    List<GenderObject> lstGender;

    public RegisterDataResponse(ArrayList<CountryObject> countryObjects, ArrayList<GenderObject> genderObjects) {
        this.lstCountry = countryObjects;
        this.lstGender = genderObjects;
    }

    public List<CountryObject> getLstCountry() {
        return lstCountry;
    }

    public void setLstCountry(ArrayList<CountryObject> lstCountry) {
        this.lstCountry = lstCountry;
    }

    public List<GenderObject> getLstGender() {
        return lstGender;
    }

    public void setLstGender(ArrayList<GenderObject> lstGender) {
        this.lstGender = lstGender;
    }
}
