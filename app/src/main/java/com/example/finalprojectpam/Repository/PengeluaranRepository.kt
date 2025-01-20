package com.example.finalprojectpam.Repository

import com.example.finalprojectpam.model.Pengeluaran
import com.example.finalprojectpam.service_api.PengeluaranApiService

interface PengeluaranRepository {
    suspend fun getPengeluaran(): List<Pengeluaran>
    suspend fun insertPengeluaran(pengeluaran: Pengeluaran)
    suspend fun updatePengeluaran(id_pengeluaran: String, pengeluaran: Pengeluaran)
    suspend fun deletePengeluaran(id_pengeluaran: String)
}

class NetworkPengeluaranRepository(
    private val pengeluaranApiService: PengeluaranApiService
) : PengeluaranRepository {
    override suspend fun getPengeluaran(): List<Pengeluaran> =
        pengeluaranApiService.getPengeluaran()

    override suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        pengeluaranApiService.insertPengeluaran(pengeluaran)
    }

    override suspend fun updatePengeluaran(id_pengeluaran: String, pengeluaran: Pengeluaran) {
        pengeluaranApiService.updatePengeluaran(id_pengeluaran, pengeluaran)
    }

    override suspend fun deletePengeluaran(id_pengeluaran: String) {
        try {
            val response = pengeluaranApiService.deletePengeluaran(id_pengeluaran)
            if (!response.isSuccessful) {
                throw Exception("Failed to delete Mahasiswa. HTTP Status Code: ${response.code()}")
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