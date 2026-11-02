package com.nusa.hadithapp.di

import com.nusa.hadithapp.data.repository.BiographyRepositoryImpl
import com.nusa.hadithapp.data.repository.BookmarkRepositoryImpl
import com.nusa.hadithapp.data.repository.HadithRepositoryImpl
import com.nusa.hadithapp.data.repository.NoteRepositoryImpl
import com.nusa.hadithapp.data.repository.ReadingHistoryRepositoryImpl
import com.nusa.hadithapp.domain.repository.BiographyRepository
import com.nusa.hadithapp.domain.repository.BookmarkRepository
import com.nusa.hadithapp.domain.repository.HadithRepository
import com.nusa.hadithapp.domain.repository.NoteRepository
import com.nusa.hadithapp.domain.repository.ReadingHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindHadithRepository(
        impl: HadithRepositoryImpl
    ): HadithRepository

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        impl: BookmarkRepositoryImpl
    ): BookmarkRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        impl: NoteRepositoryImpl
    ): NoteRepository

    @Binds
    @Singleton
    abstract fun bindBiographyRepository(
        impl: BiographyRepositoryImpl
    ): BiographyRepository

    @Binds
    @Singleton
    abstract fun bindReadingHistoryRepository(
        impl: ReadingHistoryRepositoryImpl
    ): ReadingHistoryRepository
}
