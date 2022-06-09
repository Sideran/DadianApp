package com.example.naviapp.activity.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.naviapp.entity.UserInfoEntity
import com.example.naviapp.paging3.Repository
import com.example.naviapp.paging3.model.ApiResult
import com.google.gson.JsonArray
import kotlinx.coroutines.flow.Flow

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
    fun modifyInfo(token: String,userInfo:UserInfoEntity): Flow<ApiResult<JsonArray>> {
        return Repository.modifyInfo(token,userInfo)
    }
    fun modifPwd(token: String,oldPwd:String,newPwd:String): Flow<ApiResult<JsonArray>>{
        return  Repository.modifyPwd(token,oldPwd,newPwd)
    }
}