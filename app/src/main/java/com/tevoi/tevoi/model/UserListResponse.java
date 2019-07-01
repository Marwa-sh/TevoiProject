package com.tevoi.tevoi.model;

import java.util.List;

public class UserListResponse extends IResponse {
    private List<UserListObject> lstUserList;
    private int TotalRowCount;


    public int getTotalRowCount() {
        return TotalRowCount;
    }
    public void setTotalRowCount(int totalRowCount) {
        TotalRowCount = totalRowCount;
    }
    public List<UserListObject> getLstUserList() {
        return lstUserList;
    }

    public void setLstUserList(List<UserListObject> lstUserList) {
        this.lstUserList = lstUserList;
    }


}
