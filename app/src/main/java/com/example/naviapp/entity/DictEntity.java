package com.example.naviapp.entity;

public class DictEntity {
    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    /** 摄像头字典id */
    private Long dictId;
    /** 部门名称 */

    private String dataName;
}
