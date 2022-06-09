package com.example.naviapp.paging3.model

sealed class ApiResult <out T> (val status: ApiStatus, val data: T?, val message:String?) {

    data class Success<out R>(val _data: R?): ApiResult<R>(
            status = ApiStatus.SUCCESS,
            data = _data,
            message = null
    )

    data class Error(val exception: String): ApiResult<Nothing>(
            status = ApiStatus.ERROR,
            data = null,
            message = exception
    )
    //data class Loading<out R>(val _data: R?, val isLoading: Boolean): ApiResult<R>(
    data class Loading(val msg:String): ApiResult<Nothing>(
            status = ApiStatus.LOADING,
            data = null,
            message = msg
    )
}
