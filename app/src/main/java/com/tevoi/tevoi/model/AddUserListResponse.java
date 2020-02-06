package com.tevoi.tevoi.model;

import java.util.List;

public class AddUserListResponse extends IResponse {
    private UserListObject userList;

    public UserListObject getUserList() {
        return userList;
    }

    public void setuserList(UserListObject userList) {
        this.userList = userList;
    }

}
