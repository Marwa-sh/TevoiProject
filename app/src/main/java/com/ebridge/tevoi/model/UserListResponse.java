package com.ebridge.tevoi.model;

import java.util.ArrayList;
import java.util.List;

public class UserListResponse {
    private List<UserListObject> lstUserList;

    public List<UserListObject> getLstUserList() {
        return lstUserList;
    }

    public void setLstUserList(List<UserListObject> lstUserList) {
        this.lstUserList = lstUserList;
    }
}
