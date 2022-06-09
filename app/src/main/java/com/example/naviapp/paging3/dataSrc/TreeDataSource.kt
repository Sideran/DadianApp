package com.example.naviapp.paging3.dataSrc

import com.example.naviapp.paging3.api.GetPointPage
import com.example.naviapp.paging3.model.ApiResult
import com.example.naviapp.paging3.model.PointInfo
import com.google.gson.JsonObject
import java.lang.Exception

class TreeDataSource (private val getPointPage: GetPointPage) {
    suspend fun getDeptTree(token:String): ApiResult<JsonObject> {
        return try {
            val response = getPointPage.getDeptTree(token)
            if (response.get("code").asInt!=200) ApiResult.Error(response.get("msg").asString)
            else {
                ApiResult.Success(response.get("data").asJsonArray.get(0).asJsonObject)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResult.Error(e.message.toString())
        }
    }

}