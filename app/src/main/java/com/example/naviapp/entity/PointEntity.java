package com.example.naviapp.entity;

public class PointEntity {

    private String keyword;
    /** 记录ID */
    private String facilityId;
    /** 设备国标ID */
      private String deviceId;

    /** 设备名称 */
      private String name;

    /** 设备厂商 */
      private String manufacturer;

    /** 设备归属 */
      private String owner;

    /** 设备型号 */
      private String model;

    /** 摄像头高度 */
       private String height;

    /** 照片 */
    private String picSrc;

    /** 车间班组 */
      private String civilCode;

    /** 安装地址 */
      private String address;

    /** 所在场站 */

    private String block;

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    /** 监视方位 */
      private String directionType;

    /** 摄像机类型 */
       private String ptzType;

    /** 摄像机安装位置属性，1室外，2室内 */
    private String roomType;

    /** 摄像机监控内容，行车室监控、关键点位站场视频、其他监控*/

    private String useType;

    /** 负责人 */
     private String onDuty;

    /** 联系电话 */
      private String dutyPhone;

    /** 设备口令 */
      private String password;

    /** 子设备 */
     private String parental;

    /** 父ID */
     private String parentId;

    /** 设备状态 */
      private String status;

    /** IP地址 */
      private String ipAddress;

    /** 信令安全模式，0：不采用，2：S/MIME签名方式，3：S/MIME加密签名同时采用方式，4：数字摘要方式 */
    private String safetyWay;

    /** 注册方式，1：符合IETF RFC3261标准的认证注册模式，2：基于口令的双向认证注册模式，3：基于数字证书的双向认证注册模式 */
    private String registerWay;

    /** 证书序列号 */
       private String deptId;

    /** 证书有效标识，0无效，1有效 */
    private String certifiable;

    /** 无效原因码 */
       private String errcode;

    /** 端口 */
     private String port;

    /** 保密属性 */
      private String secrecy;

    /** not necessary */
      private String positionType;

    /** 摄像机补光属性，1无补光，2红外补光，3白光补光 */
    private String supplyLightType;

    /** 分辨率 */
     private String resolution;

    /** 空域编码能力 */
        private String svcSpaceSupportMode;

    /** 下载倍速范围，各可选参数以”/”分隔，如”1/2/4”表示设备支持1，2，4倍速 */
    private String downloadSpeed;

    /** 时域编码能力，0：不支持，1：1级增强，2：2级增强，3：3级增强 */
    private String svcTimeSupportMode;

    /** 备注 */
    private String info;

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public String getCivilCode() {
        return civilCode;
    }

    public void setCivilCode(String civilCode) {
        this.civilCode = civilCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDirectionType() {
        return directionType;
    }

    public void setDirectionType(String directionType) {
        this.directionType = directionType;
    }

    public String getPtzType() {
        return ptzType;
    }

    public void setPtzType(String ptzType) {
        this.ptzType = ptzType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public String getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(String onDuty) {
        this.onDuty = onDuty;
    }

    public String getDutyPhone() {
        return dutyPhone;
    }

    public void setDutyPhone(String dutyPhone) {
        this.dutyPhone = dutyPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParental() {
        return parental;
    }

    public void setParental(String parental) {
        this.parental = parental;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSafetyWay() {
        return safetyWay;
    }

    public void setSafetyWay(String safetyWay) {
        this.safetyWay = safetyWay;
    }

    public String getRegisterWay() {
        return registerWay;
    }

    public void setRegisterWay(String registerWay) {
        this.registerWay = registerWay;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCertifiable() {
        return certifiable;
    }

    public void setCertifiable(String certifiable) {
        this.certifiable = certifiable;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(String secrecy) {
        this.secrecy = secrecy;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getSupplyLightType() {
        return supplyLightType;
    }

    public void setSupplyLightType(String supplyLightType) {
        this.supplyLightType = supplyLightType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getSvcSpaceSupportMode() {
        return svcSpaceSupportMode;
    }

    public void setSvcSpaceSupportMode(String svcSpaceSupportMode) {
        this.svcSpaceSupportMode = svcSpaceSupportMode;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(String downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public String getSvcTimeSupportMode() {
        return svcTimeSupportMode;
    }

    public void setSvcTimeSupportMode(String svcTimeSupportMode) {
        this.svcTimeSupportMode = svcTimeSupportMode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }



}
