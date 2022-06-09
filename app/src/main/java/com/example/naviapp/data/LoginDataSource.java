package com.example.naviapp.data;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.data.model.LoggedInUser;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.entity.UserEntity;
import com.example.naviapp.utils.UtilHttp;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private static final String TAG = "tag";
    RetMessage retMessage=null;
    Exception exception;
    public Result<LoggedInUser> login(Context context,String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            AppCompatActivity aa=(AppCompatActivity)context;
            Datapp app =(Datapp)aa.getApplication();
            //Demo中这个地方直接返回fakeUser，我在这里加入了用户验证过程
            //HttpLogin httpLogin = new HttpLogin("http://172.30.97.1:8080/login",username,password);
            UserEntity user =new UserEntity();
//            user.setUserId(23);
            user.setUsername(username);
            user.setPassword(password);
//            user.setMobile("13909889898");
            String userjs= JSON.toJSONString(user);
            retMessage = UtilHttp.post(app.getIPAddr()+"/login", userjs);
            if (retMessage==null){
                return new Result.Error(exception);
            }
            if (retMessage.getCode()==200) {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                username,
                                retMessage.getToken()
                        );
                return new Result.Success<>(fakeUser);
            }
            else{
                return new Result.Error(new Exception(retMessage.getMsg()));
            }


        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout(Context context) {
        // TODO: revoke authentication
        try {
            AppCompatActivity aa=(AppCompatActivity)context;
            Datapp app =(Datapp)aa.getApplication();
            retMessage = UtilHttp.post(context,app.getIPAddr()+"/logout", "");
            if (retMessage==null){

            }
        } catch (Exception e) {

        }
    }

}