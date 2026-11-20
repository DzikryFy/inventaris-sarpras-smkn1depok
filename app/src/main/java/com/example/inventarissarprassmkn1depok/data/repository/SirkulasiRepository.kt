package com.example.inventarissarprassmkn1depok.data.repository

import com.example.inventarissarprassmkn1depok.data.local.dao.AdminDao
import com.example.inventarissarprassmkn1depok.data.local.dao.BarangDao
import com.example.inventarissarprassmkn1depok.data.local.dao.PeminjamanDao
import com.example.inventarissarprassmkn1depok.data.local.entity.BarangEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.PeminjamanEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SirkulasiRepository(
    private val peminjamanDao: PeminjamanDao,
    private val barangDao: BarangDao,
    private val adminDao: AdminDao
) {
    /**
     * Mengambil daftar seluruh peminjaman sarpras yang masih berstatus aktif ('Dipinjam')
     */
    fun getPeminjamanAktif(): Flow<List<PeminjamanDetail>> =
        peminjamanDao.getPeminjamanByStatus("Dipinjam")

    /**
     * Mengambil daftar riwayat seluruh transaksi peminjaman yang sudah selesai ('Kembali')
     */
    fun getRiwayatPeminjaman(): Flow<List<PeminjamanDetail>> =
        peminjamanDao.getPeminjamanByStatus("Kembali")

    /**
     * Mengambil daftar seluruh barang inventaris untuk dropdown pilihan barang di form peminjaman
     */
    fun getAllBarang(): Flow<List<BarangWithKategori>> =
        barangDao.getAllBarangWithKategori()

    /**
     * Mencari barang berdasarkan kode unik QR/Barcode
     */
    suspend fun getBarangByKode(kode: String): BarangEntity? =
        barangDao.getBarangByKode(kode)

    /**
     * Mengambil data admin/petugas yang sedang aktif
     */
    suspend fun getActiveAdminId(): Int? {
        val admin = adminDao.getAdminByUsername("admin")
        return admin?.idAdmin
    }

    /**
     * Transaksi atomik untuk mencatat peminjaman baru
     * Memvalidasi ketersediaan stok fisik barang sebelum insert transaksi
     */
    suspend fun pinjamBarang(peminjaman: PeminjamanEntity): Result<Unit> {
        return try {
            val barang = barangDao.getBarangById(peminjaman.idBarangFk)
                ?: return Result.failure(IllegalArgumentException("Barang dengan ID ${peminjaman.idBarangFk} tidak ditemukan."))

            if (barang.stokTersedia < peminjaman.jumlahPinjam) {
                return Result.failure(
                    IllegalStateException(
                        "Stok barang '${barang.namaBarang}' tidak mencukupi (Tersedia: ${barang.stokTersedia}, Diminta: ${peminjaman.jumlahPinjam})."
                    )
                )
            }

            val success = peminjamanDao.pinjamBarang(peminjaman, barangDao)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(RuntimeException("Gagal memproses transaksi peminjaman di database."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Transaksi atomik untuk mengonfirmasi penerimaan barang kembali
     * Memulihkan kuantitas stok_tersedia dan memperbarui kondisi fisik barang jika rusak
     */
    suspend fun kembalikanBarang(
        idPinjam: Int,
        tglKembaliReal: String,
        kondisiKembali: String,
        catatan: String?
    ): Result<Unit> {
        return try {
            val success = peminjamanDao.kembalikanBarang(
                idPinjam = idPinjam,
                tglKembaliReal = tglKembaliReal,
                kondisiKembali = kondisiKembali,
                catatanTambahan = catatan,
                barangDao = barangDao
            )

            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(IllegalStateException("Transaksi peminjaman #$idPinjam sudah diproses atau tidak ditemukan."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
