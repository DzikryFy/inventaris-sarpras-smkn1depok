package com.example.inventarissarprassmkn1depok.data.local.model

import androidx.room.ColumnInfo

data class PeminjamanDetail(
    @ColumnInfo(name = "id_pinjam")
    val idPinjam: Int,

    @ColumnInfo(name = "id_barang_fk")
    val idBarangFk: Int,

    @ColumnInfo(name = "nama_barang")
    val namaBarang: String,

    @ColumnInfo(name = "kode_barang")
    val kodeBarang: String,

    @ColumnInfo(name = "nama_kategori")
    val namaKategori: String,

    @ColumnInfo(name = "id_admin_fk")
    val idAdminFk: Int?,

    @ColumnInfo(name = "nama_petugas")
    val namaPetugas: String?,

    @ColumnInfo(name = "nomor_identitas")
    val nomorIdentitas: String,

    @ColumnInfo(name = "nama_peminjam")
    val namaPeminjam: String,

    @ColumnInfo(name = "tipe_peminjam")
    val tipePeminjam: String,

    @ColumnInfo(name = "keperluan_kegiatan")
    val keperluanKegiatan: String,

    @ColumnInfo(name = "jumlah_pinjam")
    val jumlahPinjam: Int,

    @ColumnInfo(name = "tgl_pinjam")
    val tglPinjam: String,

    @ColumnInfo(name = "batas_kembali")
    val batasKembali: String,

    @ColumnInfo(name = "tgl_kembali_real")
    val tglKembaliReal: String?,

    @ColumnInfo(name = "kondisi_kembali")
    val kondisiKembali: String?,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "catatan")
    val catatan: String?
)
