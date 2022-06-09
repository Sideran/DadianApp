package com.example.naviapp.paging3.model

import com.google.gson.annotations.SerializedName

class Dict (

        /** 摄像头字典id */
        @SerializedName("dictId")
        var  dictId:Long,

        /** 部门名称 */
        @SerializedName("dataName")
       var dataName:String

)