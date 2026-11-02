package com.nusa.hadithapp.domain.use_case

import com.nusa.hadithapp.domain.model.ScholarComment
import com.nusa.hadithapp.domain.repository.HadithRepository
import javax.inject.Inject

class GetScholarCommentUseCase @Inject constructor(
    private val repository: HadithRepository
) {
    suspend operator fun invoke(kodeRawi:Int): List<ScholarComment>{
        return repository.getScholarComments(kodeRawi)
    }
}