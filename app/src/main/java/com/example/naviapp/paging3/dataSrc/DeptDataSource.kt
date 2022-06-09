package com.example.naviapp.paging3.dataSrc

import com.example.naviapp.paging3.api.GetPointPage
import com.example.naviapp.paging3.model.ApiResult
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.lang.Exception

class DeptDataSource(private val getPointPage: GetPointPage) {
    suspend fun getDeptList(token: String,deptId:String): ApiResult<JsonArray> {
        return try {
            val response=getPointPage.getDeptList(token,deptId)
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