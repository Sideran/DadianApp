package com.example.naviapp.ui.record

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.naviapp.paging3.Repository
import com.example.naviapp.paging3.model.ApiResult
import com.example.naviapp.paging3.model.PointInfo
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.util.*
import kotlin.collections.HashMap

class RecordViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    fun getDeptTree(token:String): Flow<ApiResult<JsonObject>> {
        return Repository.getDeptTree(token)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun uploadPoint(token: String, point:HashMap<String,String>, pic: MultipartBody.Part):Flow<ApiResult<String>>{
         return Repository.uploadPoint(token,point,pic)
    }
    fun getDict(token: String,dataType:String):Flow<ApiResult<JsonArray>>{
        return Repository.getDict(token,dataType)
    }
    fun getDeptList(token: String,deptId:String):Flow<ApiResult<JsonArray>>{
        return Repository.getDeptList(token,deptId)
    }
}