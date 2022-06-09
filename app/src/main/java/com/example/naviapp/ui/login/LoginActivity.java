package com.example.naviapp.ui.login;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.naviapp.activity.AlertDialogManager;
import com.example.naviapp.databinding.ActivityLoginBinding;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.entity.UserInfoEntity;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.R;
import com.example.naviapp.utils.Util;

public class LoginActivity extends AppCompatActivity {
    Datapp app;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    AlertDialogManager alert ;
    Context context;
    AutoCompleteTextView atc;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton,findpwd;
    ImageButton loginback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(Datapp)this.getApplication();
        app.setLogin(false);//登录类激活，说明尚未登录
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        loginButton = binding.login;
        findpwd=binding.findpwd;
        final ProgressBar loadingProgressBar = binding.loading;
        atc = binding.editTextIPA;
        atc.setText(app.getIPAddr());
        atc.setVisibility(View.INVISIBLE);
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginback=binding.loginback;
        autoText();
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getErrorMsg() != null) {
                    showLoginFailed(loginResult.getErrorMsg());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    app.setLogin(true);
                    app.setUserName(loginResult.getLoggedInUser().getUserName());
                    app.setToken(loginResult.getLoggedInUser().getToken());
                    app.setPassword(passwordEditText.getText().toString());
                    app.setIPAddr(atc.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loginViewModel.getUserInfo(LoginActivity.this);
                        }
                    }).start();
//                    setResult(Activity.RESULT_OK);
//                    finish();
                }
            }
        });
        loginViewModel.getRetInfo().observe(this, new Observer<RetMessage>() {
            @Override
            public void onChanged(RetMessage retMessage) {
                if(retMessage.getCode()==200){
                    UserInfoEntity userInfo= JSON.parseObject(retMessage.getData(),UserInfoEntity.class);
                    setDatapp(userInfo);
                }
                else {
                    Display display= LoginActivity.this.getWindowManager().getDefaultDisplay();
                    Util.showCenter(LoginActivity.this,display,"获取用户详细信息失败");}
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    new Thread(new Runnable() {
                        public void run() {
                            loginViewModel.login(context,usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                        }
                    }).start();
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    public void run() {
                        loginViewModel.login(context,usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                    }
                }).start();
            }
        });
        findpwd.setOnClickListener(new View.OnClickListener() {
            Display display= LoginActivity.this.getWindowManager().getDefaultDisplay();
            @Override
            public void onClick(View v) {
                Util.showCenter(LoginActivity.this,display,"该功能正在维护中");
            }
        });
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        TextWatcher afterTextChangedListener2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                app.setIPAddr(atc.getText().toString());
            }
        };
        atc.addTextChangedListener(afterTextChangedListener2);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setDatapp(UserInfoEntity userInfo){
        if(userInfo.getUserId()!=null&&!"".equals(userInfo.getUserId()))app.setUserId(userInfo.getUserId());
        if(userInfo.getNickName()!=null&&!userInfo.getNickName().equals(""))app.setNickName(userInfo.getNickName());
        if(userInfo.getDeptName()!=null&&!userInfo.getDeptName().equals(""))app.setDeptName(userInfo.getDeptName());
        if(userInfo.getDataScope()!=null&&!"".equals(userInfo.getDataScope()))app.setDataScope(userInfo.getDataScope());
        if(userInfo.getDeptId()!=null&&!"".equals(userInfo.getDeptId()))app.setDeptId(userInfo.getDeptId());
        if(userInfo.getPhonenumber()!=null&&!"".equals(userInfo.getPhonenumber()) ) app.setPhoneNum(userInfo.getPhonenumber());
        if(userInfo.getEmail()!=null&&!"".equals(userInfo.getEmail()))app.setEmail(userInfo.getEmail());
        if(userInfo.getSex()!=null&&!"".equals(userInfo.getSex()))app.setSex(userInfo.getSex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.companyIP:
            case R.id.homeIP:
            case R.id.YuxiaoIP:
                app.setIPAddr(item.getTitle().toString());
                atc.setText(app.getIPAddr());
                break;
            case R.id.kongIP:
                app.setIPAddr("");
                atc.setText(app.getIPAddr());
                break;
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed( String errorString) {
//        Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP,0,0);
//        toast.show();
        alert= new AlertDialogManager();
        alert.showAlertDialog(this, "登录失败", errorString, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        usernameEditText.setText(app.getUserName());
        passwordEditText.setText(app.getPassword());

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        startActivityForResult(intent,0);
//        finish();
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    /**
     * 自动填充IP
     */
    private void autoText(){
        final String[] String=new String[]{
                "http://system.sideran.top/prod-api","http://172.17.33.30:8082","http://192.168.31.184:8082"
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,String);
        atc.setAdapter(adapter);


    }
    @Override
    protected void onDestroy() {
        app.setLogining(false);
        super.onDestroy();
    }
}