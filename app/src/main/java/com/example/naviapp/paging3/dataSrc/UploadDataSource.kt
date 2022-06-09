package com.example.naviapp.paging3.dataSrc

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.naviapp.paging3.api.GetPointPage
import com.example.naviapp.paging3.model.ApiResult
import com.example.naviapp.paging3.model.PointInfo
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class UploadDataSource(private val getPointPage: GetPointPage, private val point:HashMap<String, String>, private val pic: MultipartBody.Part) {


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun uploadPoint(token: String): ApiResult<String> {
        return try {
            var str= JSON.toJSONString(point)
            var js=JSONObject.parseObject(str)
            val response=getPointPage.uploadPoint(token,js,pic)
            if (response.get("code").asInt==200) ApiResult.Success(response.get("msg").asString)
            else {
                ApiResult.Error(response.get("msg").asString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(e.message.toString())
        }
    }
}