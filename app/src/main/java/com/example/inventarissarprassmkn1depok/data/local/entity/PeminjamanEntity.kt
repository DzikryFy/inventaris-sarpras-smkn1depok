package com.example.inventarissarprassmkn1depok.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "peminjaman_sarpras",
    foreignKeys = [
        ForeignKey(
            entity = BarangEntity::class,
            parentColumns = ["id_barang"],
            childColumns = ["id_barang_fk"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AdminEntity::class,
            parentColumns = ["id_admin"],
            childColumns = ["id_admin_fk"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["id_barang_fk"]),
        Index(value = ["id_admin_fk"]),
        Index(value = ["status"]),
        Index(value = ["batas_kembali"])
    ]
)
data class PeminjamanEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_pinjam")
    val idPinjam: Int = 0,

    @ColumnInfo(name = "id_barang_fk")
    val idBarangFk: Int,

    @ColumnInfo(name = "id_admin_fk")
    val idAdminFk: Int? = null,

    @ColumnInfo(name = "nomor_identitas")
    val nomorIdentitas: String,

    @ColumnInfo(name = "nama_peminjam")
    val namaPeminjam: String,

    @ColumnInfo(name = "tipe_peminjam")
    val tipePeminjam: String, // Misal: Siswa, Guru, OSIS, Toolman

    @ColumnInfo(name = "keperluan_kegiatan")
    val keperluanKegiatan: String,

    @ColumnInfo(name = "jumlah_pinjam")
    val jumlahPinjam: Int,

    @ColumnInfo(name = "tgl_pinjam")
    val tglPinjam: String,

    @ColumnInfo(name = "batas_kembali")
    val batasKembali: String,

    @ColumnInfo(name = "tgl_kembali_real")
    val tglKembaliReal: String? = null,

    @ColumnInfo(name = "kondisi_kembali")
    val kondisiKembali: String? = null,

    @ColumnInfo(name = "status")
    val status: String, // Misal: Dipinjam, Kembali, Terlambat

    @ColumnInfo(name = "catatan")
    val catatan: String? = null
)
