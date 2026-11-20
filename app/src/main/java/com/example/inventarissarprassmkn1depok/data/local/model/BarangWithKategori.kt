package com.example.inventarissarprassmkn1depok.data.local.model

import androidx.room.ColumnInfo

data class BarangWithKategori(
    @ColumnInfo(name = "id_barang")
    val idBarang: Int,

    @ColumnInfo(name = "id_kategori_fk")
    val idKategoriFk: Int,

    @ColumnInfo(name = "nama_kategori")
    val namaKategori: String,

    @ColumnInfo(name = "kode_barang")
    val kodeBarang: String,

    @ColumnInfo(name = "nama_barang")
    val namaBarang: String,

    @ColumnInfo(name = "merk_spesifikasi")
    val merkSpesifikasi: String,

    @ColumnInfo(name = "stok_total")
    val stokTotal: Int,

    @ColumnInfo(name = "stok_tersedia")
    val stokTersedia: Int,

    @ColumnInfo(name = "kondisi")
    val kondisi: String,

    @ColumnInfo(name = "lokasi_simpan")
    val lokasiSimpan: String,

    @ColumnInfo(name = "sumber_dana")
    val sumberDana: String
)
