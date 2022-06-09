package com.example.naviapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.naviapp.R;
import com.example.naviapp.utils.Datapp;
import com.example.naviapp.utils.Util;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("设置");
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private Preference cache;
        private Datapp app;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            EditTextPreference height=findPreference("height");
            app=(Datapp)this.getActivity().getApplication();
            cache=findPreference("cache");
        cache.setOnPreferenceClickListener(preference -> {
            delete(app.getFileUrl(),getContext());

                return true;
            });
        }
        /** 删除文件，可以是文件或文件夹
         * @param delFile 要删除的文件夹或文件名
         * @return 删除成功返回true，否则返回false
         */
        private  boolean delete(String delFile, Context context) {
            File file = new File(delFile);
            if (!file.exists()) {
//                Toast.makeText(context, "删除文件失败:" + delFile + "不存在！", Toast.LENGTH_SHORT).show();
                Util.showToast(context,"已清空!");
                return false;
            } else {
                if (file.isFile())
                    return deleteSingleFile(context,delFile);
                else
                    return deleteDirectory(context,delFile);
            }
        }

        /** 删除单个文件
         * @param filePath$Name 要删除的文件的文件名
         * @return 单个文件删除成功返回true，否则返回false
         */
        private  boolean deleteSingleFile(Context context,String filePath$Name) {
            File file = new File(filePath$Name);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");

                    return true;
                } else {
                    Toast.makeText(context, "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(context, "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        /** 删除目录及目录下的文件
         * @param filePath 要删除的目录的文件路径
         * @return 目录删除成功返回true，否则返回false
         */
        private  boolean deleteDirectory(Context context,String filePath) {
            // 如果dir不以文件分隔符结尾，自动添加文件分隔符
            if (!filePath.endsWith(File.separator))
                filePath = filePath + File.separator;
            File dirFile = new File(filePath);
            // 如果dir对应的文件不存在，或者不是一个目录，则退出
            if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
                Toast.makeText(context, "删除目录失败：" + filePath + "不存在！", Toast.LENGTH_SHORT).show();
                return false;
            }
            boolean flag = true;
            // 删除文件夹中的所有文件包括子目录
            File[] files = dirFile.listFiles();
            for (File file : files) {
                // 删除子文件
                if (file.isFile()) {
                    flag = deleteSingleFile(context,file.getAbsolutePath());
                    if (!flag)
                        break;
                }
                // 删除子目录
                else if (file.isDirectory()) {
                    flag = deleteDirectory(context,file.getAbsolutePath());
                    if (!flag)
                        break;
                }
            }
            if (!flag) {
                Toast.makeText(context, "删除目录失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
            // 删除当前目录
            if (dirFile.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
                Util.showToast(context,"已清空");
                return true;
            } else {
                Toast.makeText(context, "删除目录：" + filePath + "失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }


}