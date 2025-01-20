package com.example.finalprojectpam.Repository

import com.example.finalprojectpam.service_api.AsetApiService
import com.example.finalprojectpam.service_api.KategoriApiService
import com.example.finalprojectpam.service_api.PendapatanApiService
import com.example.finalprojectpam.service_api.PengeluaranApiService
import com.example.finalprojectpam.service_api.SaldoApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val asetRepository: AsetRepository
    val kategoriRepository: KategoriRepository
    val pengeluaranRepository: PengeluaranRepository
    val pendapatanRepository: PendapatanRepository
    val saldoRepository: SaldoRepository
}

class PencatatanKeuangan : AppContainer {
    private val baseUrl = "http://10.0.2.2/umyTI/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val asetApiService: AsetApiService by lazy {
        retrofit.create(AsetApiService::class.java)
    }

    private val kategoriApiService: KategoriApiService by lazy {
        retrofit.create(KategoriApiService::class.java)
    }

    private val pendapatanApiService: PendapatanApiService by lazy {
        retrofit.create(PendapatanApiService::class.java)
    }

    private val pengeluaranApiService: PengeluaranApiService by lazy {
        retrofit.create(PengeluaranApiService::class.java)
    }

    private val saldoApiService: SaldoApiService by lazy {
        retrofit.create(SaldoApiService::class.java)
    }

    override val asetRepository: AsetRepository by lazy {
        NetworkAsetRepository(asetApiService)
    }

    override val kategoriRepository: KategoriRepository by lazy {
        NetworkKategoriRepository(kategoriApiService)
    }

    override val pendapatanRepository: PendapatanRepository by lazy {
        NetworkPendapatanRepository(pendapatanApiService)
    }

    override val pengeluaranRepository: PengeluaranRepository by lazy {
        NetworkPengeluaranRepository(pengeluaranApiService)
    }

    override val saldoRepository: SaldoRepository by lazy {
        NetworkSaldoRepository(saldoApiService)
    }
}