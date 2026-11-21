package com.example.inventarissarprassmkn1depok.data.repository

import com.example.inventarissarprassmkn1depok.data.local.dao.AdminDao
import com.example.inventarissarprassmkn1depok.data.local.dao.BarangDao
import com.example.inventarissarprassmkn1depok.data.local.dao.DashboardDao
import com.example.inventarissarprassmkn1depok.data.local.dao.KategoriDao
import com.example.inventarissarprassmkn1depok.data.local.dao.PeminjamanDao
import com.example.inventarissarprassmkn1depok.data.local.entity.BarangEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.KategoriEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.PeminjamanEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.data.local.model.DashboardStats
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SarprasRepository(
    private val barangDao: BarangDao,
    private val kategoriDao: KategoriDao,
    private val peminjamanDao: PeminjamanDao,
    private val dashboardDao: DashboardDao,
    private val adminDao: AdminDao? = null
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                barangDao.autoSyncGoodConditionStock()
            } catch (_: Exception) {}
        }
    }

    // 1. Dashboard Metrics & Active Loans
    fun getDashboardStats(): Flow<DashboardStats> = dashboardDao.getDashboardStats()

    fun getJatuhTempoHariIni(today: String): Flow<List<PeminjamanDetail>> =
        dashboardDao.getJatuhTempoHariIni(today)

    fun getPeminjamanAktif(): Flow<List<PeminjamanDetail>> =
        peminjamanDao.getPeminjamanByStatus("Dipinjam")

    // 2. Inventory & Catalog
    fun getAllBarang(): Flow<List<BarangWithKategori>> =
        barangDao.getAllBarangWithKategori()

    fun getBarangByKategori(kategoriId: Int): Flow<List<BarangWithKategori>> =
        barangDao.getBarangByKategori(kategoriId)

    fun searchBarang(query: String, kategoriId: Int?): Flow<List<BarangWithKategori>> {
        val cleanQuery = query.trim()
        return when {
            kategoriId == null && cleanQuery.isEmpty() -> barangDao.getAllBarangWithKategori()
            kategoriId == null -> barangDao.searchBarang(cleanQuery)
            cleanQuery.isEmpty() -> barangDao.getBarangByKategori(kategoriId)
            else -> barangDao.searchBarangByKategori(cleanQuery, kategoriId)
        }
    }

    // 3. Categories
    fun getAllKategori(): Flow<List<KategoriEntity>> =
        kategoriDao.getAllKategori()

    // 4. Loan & Return Operations
    suspend fun getActiveAdminId(): Int? {
        val admin = adminDao?.getAdminByUsername("admin")
        return admin?.idAdmin ?: 1
    }

    suspend fun pinjamBarang(peminjaman: PeminjamanEntity): Result<Unit> {
        return try {
            val barang = barangDao.getBarangById(peminjaman.idBarangFk)
                ?: return Result.failure(IllegalArgumentException("Barang dengan ID ${peminjaman.idBarangFk} tidak ditemukan."))

            if (!barang.kondisi.equals("Baik", ignoreCase = true)) {
                return Result.failure(
                    IllegalStateException(
                        "Aset '${barang.namaBarang}' berstatus '${barang.kondisi}' sehingga tidak dapat dipinjam."
                    )
                )
            }

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

    suspend fun kembalikanBarang(
        idPinjam: Int,
        tglKembaliReal: String,
        kondisiKembali: String,
        catatan: String?
    ): Boolean = peminjamanDao.kembalikanBarang(
        idPinjam = idPinjam,
        tglKembaliReal = tglKembaliReal,
        kondisiKembali = kondisiKembali,
        catatanTambahan = catatan,
        barangDao = barangDao
    )

    // 5. CRUD Master Barang
    suspend fun tambahBarang(barang: BarangEntity): Result<Unit> {
        return try {
            val existing = barangDao.getBarangByKode(barang.kodeBarang)
            if (existing != null) {
                return Result.failure(
                    IllegalArgumentException("Kode barang '${barang.kodeBarang}' sudah terdaftar dalam sistem.")
                )
            }
            barangDao.insertBarang(barang)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBarang(barang: BarangEntity): Result<Unit> {
        return try {
            val existing = barangDao.getBarangById(barang.idBarang)
                ?: return Result.failure(IllegalArgumentException("Barang dengan ID ${barang.idBarang} tidak ditemukan."))

            val activeBorrowedQty = peminjamanDao.getActiveBorrowedQtyByBarang(barang.idBarang)
            val isBecomingGood = !existing.kondisi.equals("Baik", ignoreCase = true) &&
                    barang.kondisi.equals("Baik", ignoreCase = true)

            val newStokTersedia = when {
                // 1. Jika kondisi diubah ke selain 'Baik' (misal 'Perlu Servis' / 'Rusak'): stok siap pinjam = 0
                !barang.kondisi.equals("Baik", ignoreCase = true) -> 0

                // 2. Jika kondisi dipulihkan dari servis/rusak menjadi 'Baik':
                // Pulihkan stok siap pinjam ke kapasitas penuh dikurangi unit yang sedang aktif dipinjam
                isBecomingGood -> {
                    (barang.stokTotal - activeBorrowedQty).coerceIn(0, barang.stokTotal)
                }

                // 3. Jika kondisi 'Baik', namun stokTersedia sebelumnya 0 dan tidak ada yang meminjam aktif (anomali paska servis):
                existing.stokTersedia == 0 && activeBorrowedQty == 0 && barang.kondisi.equals("Baik", ignoreCase = true) -> {
                    barang.stokTotal
                }

                // 4. Jika stok total berubah: sesuaikan stokTersedia dengan selisih stok
                barang.stokTotal != existing.stokTotal -> {
                    val diff = barang.stokTotal - existing.stokTotal
                    (existing.stokTersedia + diff).coerceIn(0, (barang.stokTotal - activeBorrowedQty).coerceAtLeast(0))
                }

                // 5. Normal: pertahankan stokTersedia dengan batas aman
                else -> {
                    existing.stokTersedia.coerceIn(0, (barang.stokTotal - activeBorrowedQty).coerceAtLeast(0))
                }
            }

            val updatedBarang = barang.copy(stokTersedia = newStokTersedia)
            barangDao.updateBarang(updatedBarang)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hapusBarang(idBarang: Int): Result<Unit> {
        return try {
            val barang = barangDao.getBarangById(idBarang)
                ?: return Result.failure(IllegalArgumentException("Barang tidak ditemukan."))

            val activeLoans = peminjamanDao.countActiveLoansByBarang(idBarang)
            if (activeLoans > 0) {
                return Result.failure(
                    IllegalStateException(
                        "Barang '${barang.namaBarang}' masih tercatat dalam $activeLoans transaksi peminjaman aktif. Silakan proses pengembalian terlebih dahulu sebelum menghapus data aset."
                    )
                )
            }

            barangDao.deleteBarang(barang)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
