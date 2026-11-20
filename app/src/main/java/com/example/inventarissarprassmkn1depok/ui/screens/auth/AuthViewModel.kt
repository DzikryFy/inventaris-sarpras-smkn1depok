package com.example.inventarissarprassmkn1depok.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventarissarprassmkn1depok.data.local.entity.AdminEntity
import com.example.inventarissarprassmkn1depok.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class RoleType {
    STAFF,
    BORROWER
}

data class AuthUiState(
    val selectedRole: RoleType = RoleType.STAFF,
    val identifier: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val rememberSession: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val loggedInAdmin: AdminEntity? = null,
    val isScanSimulationActive: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun selectRole(role: RoleType) {
        _uiState.update {
            it.copy(
                selectedRole = role,
                errorMessage = null
            )
        }
    }

    fun onIdentifierChange(value: String) {
        _uiState.update { it.copy(identifier = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleRememberSession() {
        _uiState.update { it.copy(rememberSession = !it.rememberSession) }
    }

    fun fillDemoCredentials() {
        if (_uiState.value.selectedRole == RoleType.STAFF) {
            val isCurrentAdmin = _uiState.value.identifier == "admin"
            _uiState.update {
                it.copy(
                    identifier = if (isCurrentAdmin) "toolman" else "admin",
                    password = if (isCurrentAdmin) "toolman123" else "admin123",
                    errorMessage = null
                )
            }
        } else {
            val isCurrentSiswa = _uiState.value.identifier == "22231045"
            _uiState.update {
                it.copy(
                    identifier = if (isCurrentSiswa) "197903122005011003" else "22231045",
                    password = if (isCurrentSiswa) "guru123" else "siswa-pplg-smkn1",
                    errorMessage = null
                )
            }
        }
    }

    fun triggerScanSimulation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScanSimulationActive = true) }
            delay(4000)
            _uiState.update { it.copy(isScanSimulationActive = false) }
        }
    }

    fun login() {
        val currentState = _uiState.value
        val identifier = currentState.identifier.trim()
        val password = currentState.password.trim()

        if (identifier.isBlank() || password.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "NIP / Username dan Kata Sandi wajib diisi")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Small delay for smooth verification feedback
            delay(600)

            val result = authRepository.login(identifier, password)
            result.onSuccess { admin ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true,
                        loggedInAdmin = admin,
                        errorMessage = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = false,
                        errorMessage = exception.message ?: "Autentikasi gagal. Silakan periksa kredensial Anda."
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetLoginSuccess() {
        _uiState.update { it.copy(loginSuccess = false) }
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
