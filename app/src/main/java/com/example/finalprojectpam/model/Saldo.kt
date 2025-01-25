package com.example.finalprojectpam.model

import kotlinx.serialization.Serializable

@Serializable
data class Saldo(
    val total_pendapatan: Int,
    val total_pengeluaran: Int,
    val saldo: Int
)