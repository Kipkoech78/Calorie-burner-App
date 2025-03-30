package com.fitnessapp.models


data class NetworkError(
    val error: ApiError,
    val t: Throwable? = null
)

enum class ApiError(val message: String) {
    NetworkError("Network error"), UnknownResponse("Unknown response"), UnknownError("Unknown Error Occurred")
}