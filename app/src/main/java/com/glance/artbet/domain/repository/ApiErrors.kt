package com.glance.artbet.domain.repository

enum class ApiErrors(val code: Int){
    ERROR_UNEXPECTED_CODE(666),
    ERROR_CONVERTING_DATA_CODE(601),
    ERROR_INTERNET_CONNECTION_CODE(500),
    ERROR_NOT_FOUND_CODE(404),
    ERROR_UNAUTHORIZED(401)
}