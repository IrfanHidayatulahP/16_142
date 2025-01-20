package com.example.finalprojectpam.service_api

import retrofit2.http.GET
import retrofit2.http.Headers

interface SaldoApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("saldo.php")
    suspend fun getSaldo()
}