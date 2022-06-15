package com.example.naviapp.paging3

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.naviapp.entity.UserInfoEntity
import com.example.naviapp.paging3.api.GetPointPage
import com.example.naviapp.paging3.dataSrc.*
import com.example.naviapp.paging3.model.ApiResult
import com.example.naviapp.paging3.model.PointInfo
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import java.util.*
import kotlin.collections.HashMap

object Repository {
    private const val PAGE_SIZE = 10

    private var getPointPage = GetPointPage.create()
    fun reCreate(){
        getPointPage= GetPointPage.create()
    }
    fun getPagingData(pointInfo: HashMap<String,String>,token:String): Flow<PagingData<PointInfo>> {
        return Pager(
                config = PagingConfig(PAGE_SIZE),
                pagingSourceFactory = { PointPagingSource(getPointPage,pointInfo,token) }
        ).flow
    }
    fun getDeptTree(token:String): Flow<ApiResult<JsonObject>> {
        return flow {
            emit(ApiResult.Loading("正在下载"))
            //delay(5*1000L)
            val data=TreeDataSource(getPointPage).getDeptTree(token)
            emit(data)
        }.flowOn(Dispatchers.IO) // 通过 flowOn 切换到 io 线程
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun uploadPoint(token: String, point:HashMap<String, String>, pic: MultipartBody.Part):Flow<ApiResult<String>>{
        return flow {
            emit(ApiResult.Loading("正在上传"))
            val data = UploadDataSource(getPointPage,point,pic).uploadPoint(token)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }
    fun getDict(token: String,dataType:String):Flow<ApiResult<JsonArray>>{
        return flow {
            emit(ApiResult.Loading("加载中"))
            val data = DictDataSource(getPointPage,dataType).getDict(token)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }
    fun getDeptList(token: String,deptId:String):Flow<ApiResult<JsonArray>>{
        return flow {
            emit(ApiResult.Loading("加载中"))
            val data = DeptDataSource(getPointPage).getDeptList(token,deptId)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }
    fun modifyInfo(token: String,userInfo:UserInfoEntity):Flow<ApiResult<String>>{
        return flow {
            emit(ApiResult.Loading("加载中"))
            val data = ModifyInfoDataSource(getPointPage).modifyInfo(token,userInfo)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }
    fun modifyPwd(token: String,oldPwd:String,newPwd:String):Flow<ApiResult<String>>{
        return flow {
            emit(ApiResult.Loading("加载中"))
            val data = ModifyInfoDataSource(getPointPage).modifyPwd(token,oldPwd,newPwd)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }
}