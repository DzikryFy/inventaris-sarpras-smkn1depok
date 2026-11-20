package com.example.inventarissarprassmkn1depok.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "admin_sarpras",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["nip_petugas"], unique = true)
    ]
)
data class AdminEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_admin")
    val idAdmin: Int = 0,

    @ColumnInfo(name = "nip_petugas")
    val nipPetugas: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "nama_lengkap")
    val namaLengkap: String,

    @ColumnInfo(name = "kontak")
    val kontak: String
)
