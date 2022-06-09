package com.example.naviapp.paging3.dataSrc

import com.example.naviapp.paging3.api.GetPointPage
import com.example.naviapp.paging3.model.ApiResult
import com.example.naviapp.paging3.model.PointInfo
import com.google.gson.JsonArray
import java.lang.Exception

class DictDataSource(private val getPointPage: GetPointPage,private val dataType:String) {
    suspend fun getDict(token: String): ApiResult<JsonArray> {
        return try {
            val response=getPointPage.getDict(token,dataType)
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