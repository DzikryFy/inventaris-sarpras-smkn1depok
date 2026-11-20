package com.example.inventarissarprassmkn1depok.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventarissarprassmkn1depok.data.local.entity.AdminEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminEntity): Long

    @Update
    suspend fun updateAdmin(admin: AdminEntity)

    @Delete
    suspend fun deleteAdmin(admin: AdminEntity)

    @Query("SELECT * FROM admin_sarpras WHERE username = :username LIMIT 1")
    suspend fun getAdminByUsername(username: String): AdminEntity?

    @Query("SELECT * FROM admin_sarpras WHERE nip_petugas = :nip LIMIT 1")
    suspend fun getAdminByNip(nip: String): AdminEntity?

    @Query("SELECT * FROM admin_sarpras WHERE (username = :identifier OR nip_petugas = :identifier) AND password = :password LIMIT 1")
    suspend fun login(identifier: String, password: String): AdminEntity?

    @Query("SELECT * FROM admin_sarpras WHERE id_admin = :id LIMIT 1")
    suspend fun getAdminById(id: Int): AdminEntity?

    @Query("SELECT * FROM admin_sarpras ORDER BY nama_lengkap ASC")
    fun getAllAdmins(): Flow<List<AdminEntity>>

    @Query("SELECT COUNT(*) FROM admin_sarpras")
    suspend fun getAdminCount(): Int
}
