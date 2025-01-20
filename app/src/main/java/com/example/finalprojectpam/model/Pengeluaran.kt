package com.example.finalprojectpam.model

import kotlinx.serialization.Serializable

@Serializable
data class Pengeluaran(
    val id_pengeluaran: String,
    val id_aset: String,
    val id_kategori: String,
    val tgl_transaksi: String,
    val total: String,
    val catatan: String
)
