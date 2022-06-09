package com.example.naviapp.ui.login;

import androidx.annotation.Nullable;

import com.example.naviapp.data.Result;
import com.example.naviapp.data.model.LoggedInUser;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private LoggedInUser loggedInUser;
    @Nullable
    private Integer error;
    @Nullable
    private String errorMsg;

    LoginResult(@Nullable String errorMsg){
        this.errorMsg=errorMsg;
    }
    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }
    LoginResult(@Nullable LoggedInUserView success,@Nullable LoggedInUser loggedInUser) {
        this.success = success;
        this.loggedInUser=loggedInUser;
    }

//    public LoginResult(Result.Error result) {
//        this.errorMsg=result.toString();
//    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }
    LoggedInUser getLoggedInUser(){return loggedInUser;}
    @Nullable
    Integer getError() {
        return error;
    }
    @Nullable
    String getErrorMsg(){return errorMsg;}
}