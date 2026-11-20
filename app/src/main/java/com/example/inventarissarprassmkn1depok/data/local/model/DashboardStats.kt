package com.example.inventarissarprassmkn1depok.data.local.model

import androidx.room.ColumnInfo

data class DashboardStats(
    @ColumnInfo(name = "totalAset")
    val totalAset: Int = 0,

    @ColumnInfo(name = "siapDipinjam")
    val siapDipinjam: Int = 0,

    @ColumnInfo(name = "sedangDipinjam")
    val sedangDipinjam: Int = 0,

    @ColumnInfo(name = "perluServis")
    val perluServis: Int = 0
)
