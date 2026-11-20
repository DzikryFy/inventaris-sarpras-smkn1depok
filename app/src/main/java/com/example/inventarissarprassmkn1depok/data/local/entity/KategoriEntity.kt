package com.example.inventarissarprassmkn1depok.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategori")
data class KategoriEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_kategori")
    val idKategori: Int = 0,

    @ColumnInfo(name = "nama_kategori")
    val namaKategori: String,

    @ColumnInfo(name = "keterangan")
    val keterangan: String
)
