package com.example.naviapp.paging3.dataSrc

import com.example.naviapp.entity.UserInfoEntity
import com.example.naviapp.paging3.api.GetPointPage
import com.example.naviapp.paging3.model.ApiResult
import com.google.gson.JsonArray
import java.lang.Exception

class ModifyInfoDataSource(private val getPointPage: GetPointPage) {
    suspend fun modifyInfo(token: String,userinfo:UserInfoEntity): ApiResult<JsonArray> {
        return try {
            val response=getPointPage.modifyInfo(token,userinfo)
            if (response.get("code").asInt==200)
                ApiResult.Success(response.get("data").asJsonArray)
            else {
                ApiResult.Error(response.get("msg").asString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(e.message.toString())
        }
    }
    suspend fun modifyPwd(token: String,oldPwd:String,newPwd:String): ApiResult<JsonArray> {
        return try {
            val response=getPointPage.modifyPwd(token,oldPwd,newPwd)
            if (response.get("code").asInt==200)
                ApiResult.Success(response.get("data").asJsonArray)
            else {
                ApiResult.Error(response.get("msg").asString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(e.message.toString())
        }
    }
}