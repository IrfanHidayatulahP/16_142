package com.example.finalprojectpam.service_api

import com.example.finalprojectpam.model.Kategori
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface KategoriApiService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("bacakategori.php")
    suspend fun getKategori(): List<Kategori>

    @GET("detailkategori/{id_kategori}")
    suspend fun detailKategori(@Query("id_kategori")id_kategori: String) : Kategori

    @POST("insertkategori.php")
    suspend fun insertKategori(@Body kategori: Kategori)

    @PUT("editkategori.php")
    suspend fun updateKategori(@Query("id_kategori")id_kategori: String, @Body kategori: Kategori)

    @DELETE("deletekategori.php")
    suspend fun deleteKategori(@Query("id_kategori")id_kategori: String): Response<Void>
}