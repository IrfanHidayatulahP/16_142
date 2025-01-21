package com.example.finalprojectpam.service_api

import com.example.finalprojectpam.model.Pengeluaran
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface PengeluaranApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )
    @GET("bacapengeluaran.php")
    suspend fun getPengeluaran(): List<Pengeluaran>

    @GET("getpengeluaran.php")
    suspend fun getPengeluaran(pengeluaran: Pengeluaran)

    @POST("insertpengeluaran.php")
    suspend fun insertPengeluaran(@Body pengeluaran: Pengeluaran)

    @PUT("editpengeluaran.php")
    suspend fun updatePengeluaran(@Query("id_pengeluaran")id_pengeluaran: String, @Body pengeluaran: Pengeluaran)

    @DELETE("deletepengeluaran.php")
    suspend fun deletePengeluaran(@Query("id_pengeluaran")id_pengeluaran: String): Response<Void>
}