package com.example.finalprojectpam.service_api

import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.model.Pengeluaran
import com.example.finalprojectpam.model.Saldo
import retrofit2.http.GET
import retrofit2.http.Headers

interface SaldoApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("getsaldo.php")
    suspend fun getSaldo(): Saldo
}