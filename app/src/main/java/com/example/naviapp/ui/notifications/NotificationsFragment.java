package com.example.naviapp.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.naviapp.R;
import com.example.naviapp.activity.AnalseActivity;
import com.example.naviapp.databinding.FragmentNotificationsBinding;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.activity.SettingsActivity;
import com.example.naviapp.entity.RetMessage;
import com.example.naviapp.ui.login.LoginActivity;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    // Session Manager Class
    Datapp app;
    Context context;
    Button btnLogout;
    View progress_item,sys_item,notification_item,collection_item,elitemse;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        app=(Datapp)this.getActivity().getApplication();
        context=this.getActivity();
        btnLogout = binding.buttonLogout;
        progress_item=binding.include2.progressItem;
        sys_item=binding.sysItem1.sysItem;
        notification_item=binding.notificationItem1.notificationItem;
        collection_item=binding.include.collectiobItem;
        elitemse=binding.elitemse.elseItem;
        setOnClickListener();
        /**
         * Logout button click event
         * */
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                if (app.isLogin()) {
                    new Thread(new Runnable() {
                        public void run() {
                            notificationsViewModel.logout(context);
                        }
                    }).start();
                }
                else {
                    Intent i = new Intent(context, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(i, 3);
                }
            }
        });
        notificationsViewModel.getRetMessage().observe(this.getActivity(), new Observer<RetMessage>() {
            @Override
            public void onChanged(@Nullable RetMessage retMessage) {
                if (retMessage == null) {
                    //return;
                }
                btnLogout.setText("登录");
                Toast.makeText(context.getApplicationContext(), retMessage.getMsg(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(i, 3);
            }
        });

        binding.buttonSettingTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Intent i = new Intent(context, ScrollingActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(i);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                String signature = sharedPreferences.getString("signature", "hello!");
                Toast.makeText(context.getApplicationContext(), signature, Toast.LENGTH_LONG).show();

            }
        });
        binding.buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, SettingsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        return root;
    }
    private void setOnClickListener(){
        NotifyOnClickListener listener = new NotifyOnClickListener();
        progress_item.setOnClickListener(listener);
        sys_item.setOnClickListener(listener);
        notification_item.setOnClickListener(listener);
        collection_item.setOnClickListener(listener);
        elitemse.setOnClickListener(listener);
    }

    class NotifyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.progress_item:
                    startActivity(new Intent(getActivity(), AnalseActivity.class));
                    break;
                case R.id.sys_item:
                    break;
                case R.id.notification_item:
                    break;
                case R.id.collectiob_item:
                    break;
                case R.id.else_item:
                    break;
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if (app.isLogin()){
            btnLogout.setText("取消登录");
        }
        else{
            btnLogout.setText("登录");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3 ) {
            Log.e("tag",""+resultCode);
            //if (resultCode!= Activity.RESULT_OK) getActivity().finish();
        }
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }
}