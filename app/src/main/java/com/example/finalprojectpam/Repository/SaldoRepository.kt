package com.example.finalprojectpam.Repository

import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.model.Pengeluaran
import com.example.finalprojectpam.model.Saldo
import com.example.finalprojectpam.service_api.SaldoApiService

interface SaldoRepository {
    suspend fun getPendapatan(): Pendapatan
    suspend fun getPengeluaran(): Pengeluaran
    suspend fun getSaldo(): Saldo
}

class NetworkSaldoRepository(
    private val saldoApiService: SaldoApiService
) : SaldoRepository {
    override suspend fun getPendapatan(): Pendapatan {
        return saldoApiService.getPendapatan()
    }

    override suspend fun getPengeluaran(): Pengeluaran {
        return saldoApiService.getPengeluaran()
    }

    override suspend fun getSaldo(): Saldo {
        return saldoApiService.getSaldo()
    }

}