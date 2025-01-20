package com.example.finalprojectpam.Repository

import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.service_api.PendapatanApiService

interface PendapatanRepository {
    suspend fun getPendapatan(): List<Pendapatan>
    suspend fun insertPendapatan(pendapatan: Pendapatan)
    suspend fun updatePendapatan(id_pendapatan: String, pendapatan: Pendapatan)
    suspend fun deletePendapatan(id_pendapatan: String)
}

class NetworkPendapatanRepository(
    private val pendapatanApiService: PendapatanApiService
) : PendapatanRepository {
    override suspend fun getPendapatan(): List<Pendapatan> =
        pendapatanApiService.getPendapatan()

    override suspend fun insertPendapatan(pendapatan: Pendapatan) {
        pendapatanApiService.insertPendapatan(pendapatan)
    }

    override suspend fun updatePendapatan(id_pendapatan: String, pendapatan: Pendapatan) {
        pendapatanApiService.updatePendapatan(id_pendapatan, pendapatan)
    }

    override suspend fun deletePendapatan(id_pendapatan: String) {
        try {
            val response = pendapatanApiService.deletePendapatan(id_pendapatan)
            if (!response.isSuccessful) {
                throw Exception("Failed to delete Pendapatan. HTTP Status Code: ${response.code()}")
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