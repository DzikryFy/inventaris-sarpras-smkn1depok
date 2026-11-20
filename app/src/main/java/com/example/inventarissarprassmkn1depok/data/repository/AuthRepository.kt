package com.example.inventarissarprassmkn1depok.data.repository

import com.example.inventarissarprassmkn1depok.data.local.dao.AdminDao
import com.example.inventarissarprassmkn1depok.data.local.entity.AdminEntity

enum class UserRole {
    STAFF,
    GURU,
    SISWA
}

class AuthRepository(private val adminDao: AdminDao) {

    companion object {
        @Volatile
        private var currentAdmin: AdminEntity? = null

        val OFFICIAL_DEMO_ACCOUNTS = listOf(
            AdminEntity(
                idAdmin = 1,
                nipPetugas = "198507152010011002",
                username = "admin",
                password = "admin123",
                namaLengkap = "Petugas Sarpras SMKN 1 Depok",
                kontak = "081234567890"
            ),
            AdminEntity(
                idAdmin = 2,
                nipPetugas = "22231045",
                username = "siswa",
                password = "siswa-pplg-smkn1",
                namaLengkap = "Rizky Pratama (Siswa XII PPLG 2)",
                kontak = "085712345678"
            ),
            AdminEntity(
                idAdmin = 3,
                nipPetugas = "197903122005011003",
                username = "guru",
                password = "guru123",
                namaLengkap = "Dra. Sri Wahyuni, M.Pd (Guru Produktif PPLG)",
                kontak = "081398765432"
            ),
            AdminEntity(
                idAdmin = 4,
                nipPetugas = "199204182019031008",
                username = "toolman",
                password = "toolman123",
                namaLengkap = "Ahmad Fauzi (Toolman Lab Komputer)",
                kontak = "089612345678"
            )
        )
    }

    suspend fun login(identifier: String, password: String): Result<AdminEntity> {
        val cleanId = identifier.trim()
        val cleanPass = password.trim()

        if (cleanId.isBlank() || cleanPass.isBlank()) {
            return Result.failure(IllegalArgumentException("NIP / Username dan Kata Sandi tidak boleh kosong"))
        }

        // 1. Coba login langsung via Room Database
        var admin = adminDao.login(cleanId, cleanPass)

        // 2. Self-healing fallback: Jika akun belum tersimpan di DB lokal (instalasi lama/demo), cocokkan dengan akun resmi
        if (admin == null) {
            val matchingDemo = OFFICIAL_DEMO_ACCOUNTS.find { demo ->
                val idMatch = demo.username.equals(cleanId, ignoreCase = true) || demo.nipPetugas == cleanId
                val passMatch = demo.password == cleanPass ||
                        (demo.username == "siswa" && cleanPass in listOf("siswa123", "siswa-pplg-smkn1")) ||
                        (demo.username == "guru" && cleanPass in listOf("guru123", "guru-smkn1"))
                idMatch && passMatch
            }

            if (matchingDemo != null) {
                // Simpan akun ke SQLite agar tersinkronisasi permanen
                try {
                    adminDao.insertAdmin(matchingDemo)
                } catch (_: Exception) {
                    // Abaikan jika sudah ada conflict, lanjutkan login
                }
                admin = matchingDemo
            }
        }

        return if (admin != null) {
            currentAdmin = admin
            Result.success(admin)
        } else {
            Result.failure(IllegalArgumentException("Kredensial tidak ditemukan atau kata sandi salah"))
        }
    }

    suspend fun ensureDemoAccountsSeeded() {
        try {
            OFFICIAL_DEMO_ACCOUNTS.forEach { demo ->
                val existing = adminDao.getAdminByUsername(demo.username)
                    ?: adminDao.getAdminByNip(demo.nipPetugas)
                if (existing == null) {
                    adminDao.insertAdmin(demo)
                }
            }
        } catch (_: Exception) {
            // Safe fallback
        }
    }

    fun getLoggedInAdmin(): AdminEntity? = currentAdmin

    fun getCurrentUserRole(): UserRole {
        val admin = currentAdmin ?: return UserRole.STAFF
        return when {
            admin.username.equals("admin", ignoreCase = true) || admin.nipPetugas.startsWith("1985") -> UserRole.STAFF
            admin.username.equals("toolman", ignoreCase = true) || admin.nipPetugas.startsWith("1992") -> UserRole.STAFF
            admin.username.equals("guru", ignoreCase = true) || admin.nipPetugas.startsWith("1979") -> UserRole.GURU
            admin.username.equals("siswa", ignoreCase = true) || admin.nipPetugas == "22231045" -> UserRole.SISWA
            else -> UserRole.STAFF
        }
    }

    fun isStaff(): Boolean = getCurrentUserRole() == UserRole.STAFF

    fun isBorrower(): Boolean = !isStaff()

    fun logout() {
        currentAdmin = null
    }
}
