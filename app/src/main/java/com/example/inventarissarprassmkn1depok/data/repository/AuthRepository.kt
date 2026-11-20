package com.example.inventarissarprassmkn1depok.data.repository

import com.example.inventarissarprassmkn1depok.data.local.dao.AdminDao
import com.example.inventarissarprassmkn1depok.data.local.entity.AdminEntity

class AuthRepository(private val adminDao: AdminDao) {

    private var currentAdmin: AdminEntity? = null

    suspend fun login(identifier: String, password: String): Result<AdminEntity> {
        val cleanId = identifier.trim()
        val cleanPass = password.trim()

        if (cleanId.isBlank() || cleanPass.isBlank()) {
            return Result.failure(IllegalArgumentException("NIP / Username dan Kata Sandi tidak boleh kosong"))
        }

        val admin = adminDao.login(cleanId, cleanPass)
        return if (admin != null) {
            currentAdmin = admin
            Result.success(admin)
        } else {
            Result.failure(IllegalArgumentException("Kredensial tidak ditemukan atau kata sandi salah"))
        }
    }

    fun getLoggedInAdmin(): AdminEntity? = currentAdmin

    fun logout() {
        currentAdmin = null
    }
}
