package com.example.finalprojectpam.service_api

import com.example.finalprojectpam.model.Pendapatan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface PendapatanApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacapendapatan.php")
    suspend fun getPendapatan(): List<Pendapatan>

    @GET("detailpendapatan.php/{id_pendapatan}")
    suspend fun detailPendapatan(@Query("id_pendapatan")id_pendapatan: String) : Pendapatan

    @POST("insertpendapatan.php")
    suspend fun insertPendapatan(@Body pendapatan: Pendapatan)

    @PUT("editpendapatan.php")
    suspend fun updatePendapatan(@Query("id_pendapatan")id_pendapatan: String, @Body pendapatan: Pendapatan)

    @DELETE("deletependapatan.php")
    suspend fun deletePendapatan(@Query("id_pendapatan")id_pendapatan: String): Response<Void>
}