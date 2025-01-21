package com.example.finalprojectpam.Repository

import android.util.Log
import com.example.finalprojectpam.model.Aset
import com.example.finalprojectpam.service_api.AsetApiService

interface AsetRepository {
    suspend fun getAset() : List<Aset>
    suspend fun insertAset(aset: Aset)
    suspend fun updateAset(id_aset: String, aset: Aset)
    suspend fun deleteAset(id_aset: String)
}

class NetworkAsetRepository (
    private val asetApiService: AsetApiService
) : AsetRepository {
    override suspend fun getAset(): List<Aset> {
        val asetList = asetApiService.getAset()
        Log.d("AsetRepository", "Received Aset: $asetList")
        return asetList
    }

    override suspend fun insertAset(aset: Aset) {
        asetApiService.insertAset(aset)
    }

    override suspend fun updateAset(id_aset: String, aset: Aset) {
        asetApiService.updateAset(id_aset, aset)
    }

    override suspend fun deleteAset(id_aset: String) {
        try {
            val response = asetApiService.deleteAset(id_aset)
            if (!response.isSuccessful) {
                throw Exception("Failed to delete Aset. HTTP Status Code: ${response.code()}")
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