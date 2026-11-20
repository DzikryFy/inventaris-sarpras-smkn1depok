package com.example.inventarissarprassmkn1depok.ui.screens.katalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventarissarprassmkn1depok.data.local.entity.KategoriEntity
import com.example.inventarissarprassmkn1depok.data.local.model.BarangWithKategori
import com.example.inventarissarprassmkn1depok.data.repository.SarprasRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class KatalogUiState(
    val searchQuery: String = "",
    val selectedKategoriId: Int? = null,
    val categories: List<KategoriEntity> = emptyList(),
    val barangList: List<BarangWithKategori> = emptyList(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class KatalogViewModel(
    private val sarprasRepository: SarprasRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedKategoriId = MutableStateFlow<Int?>(null)

    private val _categoriesFlow = sarprasRepository.getAllKategori()

    private val _barangFilteredFlow = combine(_searchQuery, _selectedKategoriId) { query, katId ->
        Pair(query, katId)
    }.flatMapLatest { (query, katId) ->
        sarprasRepository.searchBarang(query, katId)
    }

    val uiState: StateFlow<KatalogUiState> = combine(
        _searchQuery,
        _selectedKategoriId,
        _categoriesFlow,
        _barangFilteredFlow
    ) { query, katId, categories, barangList ->
        KatalogUiState(
            searchQuery = query,
            selectedKategoriId = katId,
            categories = categories,
            barangList = barangList,
            totalCount = barangList.size,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = KatalogUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectKategori(kategoriId: Int?) {
        _selectedKategoriId.value = kategoriId
    }

    class Factory(
        private val sarprasRepository: SarprasRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(KatalogViewModel::class.java)) {
                return KatalogViewModel(sarprasRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
