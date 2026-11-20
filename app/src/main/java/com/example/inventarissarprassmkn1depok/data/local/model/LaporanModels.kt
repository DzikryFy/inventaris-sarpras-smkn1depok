package com.example.inventarissarprassmkn1depok.data.local.model

data class LokasiDistribusi(
    val lokasiSimpan: String,
    val totalUnit: Int,
    val itemJenisCount: Int
)

data class KategoriSirkulasiStat(
    val namaKategori: String,
    val totalDipinjam: Int,
    val totalItem: Int
)

data class AuditSummary(
    val totalAset: Int = 0,
    val siapDipinjam: Int = 0,
    val sedangDipinjam: Int = 0,
    val perluServis: Int = 0,
    val persentaseKetersediaan: Int = 0
)
