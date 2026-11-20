package com.example.inventarissarprassmkn1depok.data.repository

import com.example.inventarissarprassmkn1depok.data.local.dao.BarangDao
import com.example.inventarissarprassmkn1depok.data.local.dao.DashboardDao
import com.example.inventarissarprassmkn1depok.data.local.dao.KategoriDao
import com.example.inventarissarprassmkn1depok.data.local.dao.PeminjamanDao
import com.example.inventarissarprassmkn1depok.data.local.entity.KategoriEntity
import com.example.inventarissarprassmkn1depok.data.local.entity.PeminjamanEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.data.local.model.DashboardStats
import com.example.inventarissarprassmkn1depok.data.local.model.PeminjamanDetail
import kotlinx.coroutines.flow.Flow

class SarprasRepository(
    private val barangDao: BarangDao,
    private val kategoriDao: KategoriDao,
    private val peminjamanDao: PeminjamanDao,
    private val dashboardDao: DashboardDao
) {
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
    suspend fun pinjamBarang(peminjaman: PeminjamanEntity): Boolean =
        peminjamanDao.pinjamBarang(peminjaman, barangDao)

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
}
