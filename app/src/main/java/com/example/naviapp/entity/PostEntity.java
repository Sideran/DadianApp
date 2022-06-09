package com.example.naviapp.entity;

public class PostEntity {
    String postCode;
    String postName;
    int postSort;
    String status;

    public PostEntity(String postCode, String postName, int postSort, String status) {
        this.postCode = postCode;
        this.postName = postName;
        this.postSort = postSort;
        this.status = status;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public int getPostSort() {
        return postSort;
    }

    public void setPostSort(int postSort) {
        this.postSort = postSort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
