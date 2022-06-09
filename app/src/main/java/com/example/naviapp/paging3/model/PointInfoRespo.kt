package com.example.naviapp.paging3.model

import com.google.gson.annotations.SerializedName

class PointInfoRespo (
    @SerializedName("rows") val items: List<PointInfo> = emptyList(),
    @SerializedName("code") val code:  Int,
    @SerializedName("total") val total: Int,
    @SerializedName("msg") val msg:String

    )