package com.example.finalprojectpam.Repository

import android.util.Log
import com.example.finalprojectpam.model.Saldo
import com.example.finalprojectpam.service_api.SaldoApiService

interface SaldoRepository {
    suspend fun getSaldo(): Saldo
}

class NetworkSaldoRepository(
    private val saldoApiService: SaldoApiService
) : SaldoRepository {
    override suspend fun getSaldo(): Saldo {
        return try {
            val response = saldoApiService.getSaldo()
            response
        } catch (e: Exception) {
            throw e
        }
    }
}