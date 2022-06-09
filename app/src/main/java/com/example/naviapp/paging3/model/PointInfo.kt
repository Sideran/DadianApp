package com.example.naviapp.paging3.model

import com.google.gson.annotations.SerializedName

data class PointInfo (



        @SerializedName("keyword")
        var keyword:String,
        /** 记录ID */
        @SerializedName("facilityId")
        val facilityId:String,

        /** 设备国标ID  */

        @SerializedName("deviceId")
        var deviceid: String,
        /** 设备名称  */
        @SerializedName("name")
        var name: String,

        /** 设备厂商  */
        @SerializedName("manufacturer")
        var manufacturer: String,

        /** 设备归属  */
        @SerializedName("owner")
        var owner: String,

//        /** 设备型号  */
//        @SerializedName("model")
//        var model: String,

        /** 安装地址  */
        @SerializedName("address")
        var address: String,

        /** 经度  */
        @SerializedName("longitude")
        var longitude: String,

        /** 纬度  */
        @SerializedName("latitude")
        var latitude: String,

        /** 设备状态，ON在线，OFF离线  */
        @SerializedName("status")
        var status: String,

        /** 摄像头高度  */
        @SerializedName("height")
        var height: String,

        /** 监视方位 */
        @SerializedName( "directionType")
        var directionType:String,
        /** 摄像机安装位置属性，1室外，2室内  */
        @SerializedName("roomType")
        var roomType: String,

        /** 摄像机监控内容，行车室监控、关键点位站场视频、其他监控 */
        @SerializedName("useType")
        var useType: String ,

        /** 负责人  */
        @SerializedName("onDuty")
        var onDuty: String,

        /** 联系电话  */
        @SerializedName("dutyPhone")
        var dutyPhone: String,

        /** 摄像机类型  */
        @SerializedName("ptzType")
        var ptzType: String,
        /** 监控范围  */
        @SerializedName("positionType")
        var positionType: String,

        /** IP地址  */
        @SerializedName("ipAddress")
        var ipAddress: String,
            /** 设备口令  */
    @SerializedName("password")
    var password: String,

    /** 是否有子设备，1有，0没有   */
    @SerializedName("parental")
    var parental: String,

    /** 父ID  */
    @SerializedName("parentId")
    var parentId: String,

    /** 行政区域  */
    @SerializedName("civilCode")
    var civilCode: String,


    /** 所在场站  */
    @SerializedName("block")
    var block: String,

    /** 信令安全模式  */
    @SerializedName("safetyWay")
    var safetyWay: String,

    /** 注册方式  */
    @SerializedName("registerWay")
    var registerWay: String,

    /** 证书序列号  */
    @SerializedName("deptId")
    var deptId: String,

    /** 证书有效标识，0无效，1有效  */
    @SerializedName("certifiable")
    var certifiable: String,

    /** 无效原因码  */
    @SerializedName("errcode")
    var errcode: String,

    /** 端口  */
    @SerializedName("port")
    var port: String,

    /** 保密属性，0不涉密，1涉密  */
    @SerializedName("secrecy")
    var secrecy: String,


    /** 补光属性  */
    @SerializedName("supplyLightType")
    var supplyLightType: String,


    /** 分辨率  */
    @SerializedName("resolution")
    var resolution: String,

    /** 空域编码能力，0：不支持，1：1级增强，2：2级增强，3：3级增强  */
    @SerializedName("svcSpaceSupportMode")
    var svcSpaceSupportMode: String,

    /** 下载倍速范围，各可选参数以”/”分隔，如”1/2/4”表示设备支持1，2，4倍速  */
    @SerializedName("downloadSpeed")
    var downloadSpeed: String,

    /** 时域编码能力，0：不支持，1：1级增强，2：2级增强，3：3级增强  */
    @SerializedName("svcTimeSupportMode")
    var svcTimeSupportMode: String,

    /** 备注  */
    @SerializedName("info")
    var info: String
        )



