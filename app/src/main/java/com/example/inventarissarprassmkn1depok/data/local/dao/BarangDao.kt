package com.example.inventarissarprassmkn1depok.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventarissarprassmkn1depok.data.local.entity.BarangEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.data.local.model.KategoriSirkulasiStat
import com.example.inventarissarprassmkn1depok.data.local.model.LokasiDistribusi
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarang(barang: BarangEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(barangList: List<BarangEntity>)

    @Update
    suspend fun updateBarang(barang: BarangEntity)

    @Delete
    suspend fun deleteBarang(barang: BarangEntity)

    @Query("SELECT * FROM barang_sarpras WHERE id_barang = :id LIMIT 1")
    suspend fun getBarangById(id: Int): BarangEntity?

    @Query("SELECT * FROM barang_sarpras WHERE kode_barang = :kode LIMIT 1")
    suspend fun getBarangByKode(kode: String): BarangEntity?

    @Query("""
        SELECT b.id_barang, b.id_kategori_fk, k.nama_kategori, b.kode_barang, 
               b.nama_barang, b.merk_spesifikasi, b.stok_total, b.stok_tersedia, 
               b.kondisi, b.lokasi_simpan, b.sumber_dana 
        FROM barang_sarpras b
        INNER JOIN kategori k ON b.id_kategori_fk = k.id_kategori
        ORDER BY b.nama_barang ASC
    """)
    fun getAllBarangWithKategori(): Flow<List<BarangWithKategori>>

    @Query("""
        SELECT b.id_barang, b.id_kategori_fk, k.nama_kategori, b.kode_barang, 
               b.nama_barang, b.merk_spesifikasi, b.stok_total, b.stok_tersedia, 
               b.kondisi, b.lokasi_simpan, b.sumber_dana 
        FROM barang_sarpras b
        INNER JOIN kategori k ON b.id_kategori_fk = k.id_kategori
        WHERE b.id_kategori_fk = :kategoriId
        ORDER BY b.nama_barang ASC
    """)
    fun getBarangByKategori(kategoriId: Int): Flow<List<BarangWithKategori>>

    @Query("""
        SELECT b.id_barang, b.id_kategori_fk, k.nama_kategori, b.kode_barang, 
               b.nama_barang, b.merk_spesifikasi, b.stok_total, b.stok_tersedia, 
               b.kondisi, b.lokasi_simpan, b.sumber_dana 
        FROM barang_sarpras b
        INNER JOIN kategori k ON b.id_kategori_fk = k.id_kategori
        WHERE b.nama_barang LIKE '%' || :query || '%' 
           OR b.kode_barang LIKE '%' || :query || '%'
           OR b.merk_spesifikasi LIKE '%' || :query || '%'
        ORDER BY b.nama_barang ASC
    """)
    fun searchBarang(query: String): Flow<List<BarangWithKategori>>

    @Query("""
        SELECT b.id_barang, b.id_kategori_fk, k.nama_kategori, b.kode_barang, 
               b.nama_barang, b.merk_spesifikasi, b.stok_total, b.stok_tersedia, 
               b.kondisi, b.lokasi_simpan, b.sumber_dana 
        FROM barang_sarpras b
        INNER JOIN kategori k ON b.id_kategori_fk = k.id_kategori
        WHERE b.id_kategori_fk = :kategoriId 
          AND (b.nama_barang LIKE '%' || :query || '%' 
               OR b.kode_barang LIKE '%' || :query || '%'
               OR b.merk_spesifikasi LIKE '%' || :query || '%')
        ORDER BY b.nama_barang ASC
    """)
    fun searchBarangByKategori(query: String, kategoriId: Int): Flow<List<BarangWithKategori>>

    @Query("UPDATE barang_sarpras SET stok_tersedia = :stokTersedia WHERE id_barang = :idBarang")
    suspend fun updateStokTersedia(idBarang: Int, stokTersedia: Int)

    @Query("""
        UPDATE barang_sarpras 
        SET stok_tersedia = CASE 
            WHEN (stok_tersedia - :qty) < 0 THEN 0 
            ELSE (stok_tersedia - :qty) 
        END 
        WHERE id_barang = :idBarang AND stok_tersedia >= :qty
    """)
    suspend fun reduceStok(idBarang: Int, qty: Int): Int

    @Query("""
        UPDATE barang_sarpras 
        SET stok_tersedia = CASE 
            WHEN (stok_tersedia + :qty) > stok_total THEN stok_total 
            ELSE (stok_tersedia + :qty) 
        END 
        WHERE id_barang = :idBarang
    """)
    suspend fun restoreStok(idBarang: Int, qty: Int): Int

    @Query("UPDATE barang_sarpras SET kondisi = :kondisi WHERE id_barang = :idBarang")
    suspend fun updateKondisiBarang(idBarang: Int, kondisi: String): Int

    @Query("""
        UPDATE barang_sarpras 
        SET stok_tersedia = (stok_total - (
            SELECT COALESCE(SUM(p.jumlah_pinjam), 0) 
            FROM peminjaman_sarpras p 
            WHERE p.id_barang_fk = barang_sarpras.id_barang AND p.status = 'Dipinjam'
        ))
        WHERE (kondisi = 'Baik' OR kondisi = 'baik') 
          AND stok_tersedia = 0 
          AND (
              SELECT COALESCE(SUM(p2.jumlah_pinjam), 0) 
              FROM peminjaman_sarpras p2 
              WHERE p2.id_barang_fk = barang_sarpras.id_barang AND p2.status = 'Dipinjam'
          ) < stok_total
    """)
    suspend fun autoSyncGoodConditionStock(): Int

    @Query("SELECT COUNT(*) FROM barang_sarpras")
    suspend fun getBarangCount(): Int

    @Query("""
        SELECT lokasi_simpan AS lokasiSimpan, 
               COALESCE(SUM(stok_total), 0) AS totalUnit, 
               COUNT(*) AS itemJenisCount 
        FROM barang_sarpras 
        GROUP BY lokasi_simpan 
        ORDER BY totalUnit DESC
    """)
    fun getDistribusiLokasi(): Flow<List<LokasiDistribusi>>

    @Query("""
        SELECT k.nama_kategori AS namaKategori, 
               COALESCE(SUM(p.jumlah_pinjam), 0) AS totalDipinjam, 
               COUNT(DISTINCT b.id_barang) AS totalItem
        FROM kategori k
        LEFT JOIN barang_sarpras b ON k.id_kategori = b.id_kategori_fk
        LEFT JOIN peminjaman_sarpras p ON b.id_barang = p.id_barang_fk
        GROUP BY k.id_kategori
        ORDER BY totalDipinjam DESC
    """)
    fun getStatistikKategori(): Flow<List<KategoriSirkulasiStat>>
}
