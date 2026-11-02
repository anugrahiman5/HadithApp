package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.SearchResult
import com.nusa.hadithapp.domain.repository.HadithRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHadithUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    operator fun invoke(keyword: String, books: List<String>): Flow<SearchResult> {
        return repository.searchHadits(keyword, books)
    }
}