package com.example.inventarissarprassmkn1depok.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "barang_sarpras",
    foreignKeys = [
        ForeignKey(
            entity = KategoriEntity::class,
            parentColumns = ["id_kategori"],
            childColumns = ["id_kategori_fk"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["id_kategori_fk"]),
        Index(value = ["kode_barang"], unique = true)
    ]
)
data class BarangEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_barang")
    val idBarang: Int = 0,

    @ColumnInfo(name = "id_kategori_fk")
    val idKategoriFk: Int,

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
