package com.example.bengkelappclient.di

import android.content.Context
import androidx.room.Room
import com.example.bengkelappclient.data.local.AppDatabase
import com.example.bengkelappclient.data.local.dao.*
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
    fun provideVehicleDao(appDatabase: AppDatabase): VehicleDao {
        return appDatabase.vehicleDao()
    }

    @Provides
    fun provideServiceDao(appDatabase: AppDatabase): ServiceDao {
        return appDatabase.serviceDao()
    }

    @Provides
    fun provideBookingDao(appDatabase: AppDatabase): BookingDao {
        return appDatabase.bookingDao()
    }

    @Provides
    fun provideScheduleDao(appDatabase: AppDatabase): ScheduleDao {
        return appDatabase.scheduleDao()
    }
}
