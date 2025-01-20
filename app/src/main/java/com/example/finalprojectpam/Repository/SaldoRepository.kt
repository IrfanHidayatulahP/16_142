package com.example.finalprojectpam.Repository

import com.example.finalprojectpam.service_api.SaldoApiService

interface SaldoRepository {
    suspend fun getSaldo()
}

class NetworkSaldoRepository(
    private val saldoApiService: SaldoApiService
) : SaldoRepository {
    override suspend fun getSaldo() {
        saldoApiService.getSaldo()
    }
}