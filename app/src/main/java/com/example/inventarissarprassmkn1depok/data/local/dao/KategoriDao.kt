package com.example.inventarissarprassmkn1depok.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventarissarprassmkn1depok.data.local.entity.KategoriEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKategori(kategori: KategoriEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kategoriList: List<KategoriEntity>)

    @Update
    suspend fun updateKategori(kategori: KategoriEntity)

    @Delete
    suspend fun deleteKategori(kategori: KategoriEntity)

    @Query("SELECT * FROM kategori WHERE id_kategori = :id LIMIT 1")
    suspend fun getKategoriById(id: Int): KategoriEntity?

    @Query("SELECT * FROM kategori ORDER BY nama_kategori ASC")
    fun getAllKategori(): Flow<List<KategoriEntity>>

    @Query("SELECT COUNT(*) FROM kategori")
    suspend fun getKategoriCount(): Int
}
