package com.althaaf.fruitarians.core.data.network.retrofit

sealed class ApiResult<out R> private constructor() {

    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Error(val error: String): ApiResult<Nothing>()

    object Loading : ApiResult<Nothing>()
}
