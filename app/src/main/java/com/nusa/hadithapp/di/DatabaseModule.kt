package com.nusa.hadithapp.di

import android.content.Context
import androidx.room.Room
import com.nusa.hadithapp.data.data_source.local.HadithDatabase
import com.nusa.hadithapp.data.data_source.local.dao.BookmarkDao
import com.nusa.hadithapp.data.data_source.local.dao.CachedHadithDao
import com.nusa.hadithapp.data.data_source.local.dao.NoteDao
import com.nusa.hadithapp.data.data_source.local.dao.ReadingHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHadithDatabase(
        @ApplicationContext context: Context
    ): HadithDatabase {
        return Room.databaseBuilder(
            context,
            HadithDatabase::class.java,
            "hadith_app.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: HadithDatabase): BookmarkDao {
        return database.bookmarkDao
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: HadithDatabase): NoteDao {
        return database.noteDao
    }

    @Provides
    @Singleton
    fun provideCachedHadithDao(database: HadithDatabase): CachedHadithDao {
        return database.cachedHadithDao
    }

    @Provides
    @Singleton
    fun provideReadingHistoryDao(database: HadithDatabase): ReadingHistoryDao {
        return database.readingHistoryDao
    }
}
