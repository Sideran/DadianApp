package com.example.naviapp.ui.notifications;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.naviapp.utils.Datapp;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.utils.UtilHttp;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<RetMessage> retMessage = new MutableLiveData<>();
    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    LiveData<RetMessage> getRetMessage() {
        return retMessage;
    }
    public void logout(Context context) {
        // TODO: revoke authentication
        AppCompatActivity aa=(AppCompatActivity)context;
        Datapp app =(Datapp)aa.getApplication();
        RetMessage ret;
        try {
            ret = UtilHttp.post(context,app.getIPAddr()+"/logout", "");
            app.setLogin(false);
            app.setToken("");
            //app.setUserName("");
            if (ret!=null){
                retMessage.postValue(ret);
            }
        } catch (Exception e) {
            app.setLogin(false);
            app.setToken("");
            //app.setUserName("");
        }
    }
}