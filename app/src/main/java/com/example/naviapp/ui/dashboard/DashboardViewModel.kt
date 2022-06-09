package com.example.naviapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.naviapp.paging3.Repository
import com.example.naviapp.paging3.model.ApiResult
import com.example.naviapp.paging3.model.PointInfo
import com.google.gson.JsonArray
import kotlinx.coroutines.flow.Flow

class DashboardViewModel : ViewModel() {
//    private val mText: MutableLiveData<String>
//    val text: LiveData<String>
//        get() = mText
//
//    init {
//        mText = MutableLiveData()
//        mText.value = "This is dashboard fragment"
//    }

    fun getPagingData(pointInfo: HashMap<String,String>,token:String): Flow<PagingData<PointInfo>> {
        return Repository.getPagingData(pointInfo,token).cachedIn(viewModelScope)
    }
    fun getDict(token: String,dataType:String):Flow<ApiResult<JsonArray>>{
        return Repository.getDict(token,dataType)
    }
    fun getDeptList(token: String,deptId:String):Flow<ApiResult<JsonArray>>{
        return Repository.getDeptList(token,deptId)
    }
}