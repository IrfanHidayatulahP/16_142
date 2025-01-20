package com.example.finalprojectpam.Repository

import com.example.finalprojectpam.model.Kategori
import com.example.finalprojectpam.service_api.KategoriApiService

interface KategoriRepository {
    suspend fun getKategori(): List<Kategori>
    suspend fun insertKategori(kategori: Kategori)
    suspend fun updateKategori(id_kategori: String, kategori: Kategori)
    suspend fun deleteKategori(id_kategori: String)
}

class NetworkKategoriRepository(
    private val kategoriApiService: KategoriApiService
) : KategoriRepository {
    override suspend fun getKategori(): List<Kategori> =
        kategoriApiService.getKategori()

    override suspend fun insertKategori(kategori: Kategori) {
        kategoriApiService.insertKategori(kategori)
    }

    override suspend fun updateKategori(id_kategori: String, kategori: Kategori) {
        kategoriApiService.updateKategori(id_kategori, kategori)
    }

    override suspend fun deleteKategori(id_kategori: String) {
        try {
            val response = kategoriApiService.deleteKategori(id_kategori)
            if (!response.isSuccessful) {
                throw Exception("Failed to delete Kategori. HTTP Status Code: ${response.code()}")
            }
            else {
                response.message()
                println(response.message())
            }
        }
        catch (e: Exception) {
            throw e
        }
    }
}