package com.tevoi.tevoi.model;

import java.util.List;

public class CategoryResponseList
{
    private List<CategoryObject> CategoriesList;

    public void setCategoriesList(List<CategoryObject> categoriesList) {
        CategoriesList = categoriesList;
    }

    public List<CategoryObject> getCategoriesList() {
        return CategoriesList;
    }
}
