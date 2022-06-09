package com.example.naviapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import com.example.naviapp.data.LoginRepository;
import com.example.naviapp.data.Result;
import com.example.naviapp.data.model.LoggedInUser;
import com.example.naviapp.R;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.utils.Util;
import com.example.naviapp.utils.UtilHttp;

import java.io.IOException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<RetMessage> userInfo=new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    public LiveData<RetMessage> getRetInfo(){return userInfo;}
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
    public void logout(Context context){
        loginRepository.logout(context);
    }
    public void login(Context context,String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(context,username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUserName()),data));
        } else {
            loginResult.postValue(new LoginResult(((Result.Error)result).getError().getMessage()));//new LoginResult(R.string.login_failed)
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    public void getUserInfo(Context context){
        AppCompatActivity activity=(AppCompatActivity)context;
        Datapp app=(Datapp)activity.getApplication();
        try {
            RetMessage retMessage= UtilHttp.get(context,app.getIPAddr()+"/RecordCamera/attribute/android/getUserInfo");
            userInfo.postValue(retMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}