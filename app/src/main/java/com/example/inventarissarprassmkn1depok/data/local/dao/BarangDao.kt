package com.example.inventarissarprassmkn1depok.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventarissarprassmkn1depok.data.local.entity.BarangEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
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

    @Query("UPDATE barang_sarpras SET stok_tersedia = stok_tersedia - :qty WHERE id_barang = :idBarang AND stok_tersedia >= :qty")
    suspend fun reduceStok(idBarang: Int, qty: Int): Int

    @Query("UPDATE barang_sarpras SET stok_tersedia = MIN(stok_total, stok_tersedia + :qty) WHERE id_barang = :idBarang")
    suspend fun restoreStok(idBarang: Int, qty: Int): Int

    @Query("UPDATE barang_sarpras SET kondisi = :kondisi WHERE id_barang = :idBarang")
    suspend fun updateKondisiBarang(idBarang: Int, kondisi: String): Int

    @Query("SELECT COUNT(*) FROM barang_sarpras")
    suspend fun getBarangCount(): Int
}
