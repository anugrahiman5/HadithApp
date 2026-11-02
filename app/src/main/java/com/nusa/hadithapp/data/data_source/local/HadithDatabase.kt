package com.nusa.hadithapp.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nusa.hadithapp.data.data_source.local.dao.BookmarkDao
import com.nusa.hadithapp.data.data_source.local.dao.CachedHadithDao
import com.nusa.hadithapp.data.data_source.local.dao.NoteDao
import com.nusa.hadithapp.data.data_source.local.dao.ReadingHistoryDao
import com.nusa.hadithapp.data.data_source.local.entity.BookmarkEntity
import com.nusa.hadithapp.data.data_source.local.entity.CachedHadithEntity
import com.nusa.hadithapp.data.data_source.local.entity.NoteEntity
import com.nusa.hadithapp.data.data_source.local.entity.ReadingHistoryEntity

@Database(
    entities = [
        BookmarkEntity::class,
        NoteEntity::class,
        CachedHadithEntity::class,
        ReadingHistoryEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class HadithDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao
    abstract val noteDao: NoteDao
    abstract val cachedHadithDao: CachedHadithDao
    abstract val readingHistoryDao: ReadingHistoryDao
}
