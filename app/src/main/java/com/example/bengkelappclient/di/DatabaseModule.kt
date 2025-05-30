// di/DatabaseModule.kt
package com.example.bengkelappclient.di

import android.content.Context
import androidx.room.Room
import com.example.bengkelappclient.data.local.AppDatabase
import com.example.bengkelappclient.data.local.dao.CustomerDao
import com.example.bengkelappclient.data.local.dao.UserDao
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
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideCustomerDao(appDatabase: AppDatabase): CustomerDao {
        return appDatabase.customerDao()
    }

    // TODO: Provide DAO untuk Vehicle dan ServiceOrder jika sudah dibuat
}