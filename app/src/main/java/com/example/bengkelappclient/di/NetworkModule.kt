// di/NetworkModule.kt
package com.example.bengkelappclient.di

import android.content.Context
import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // --- GANTI BASE URL INI SESUAI DENGAN ALAMAT IP DAN PORT SERVER LARAVEL ANDA ---
    // Emulator Android Studio standar: "http://10.0.2.2:8000/api/"
    // Genymotion: "http://10.0.3.2:8000/api/"
    // Perangkat fisik (terhubung ke WiFi yang sama dengan PC/Laptop Anda):
    // Ganti 10.0.2.2 dengan IP address lokal PC/Laptop Anda (misal, "http://192.168.1.10:8000/api/")
    // Pastikan firewall tidak memblokir koneksi.
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY) // Log semua body request/response
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferenceManager: UserPreferenceManager): Interceptor {
        return Interceptor { chain ->
            // runBlocking digunakan di sini karena interceptor bukan suspend function,
            // dan kita perlu token secara sinkron sebelum request dikirim.
            // Ini umumnya aman karena token biasanya sudah ada atau cepat diambil dari DataStore.
            val token = runBlocking { userPreferenceManager.getToken().first() }
            val requestBuilder = chain.request().newBuilder()
            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            requestBuilder.addHeader("Accept", "application/json")
            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor // Tambahkan authInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Untuk logging
            .addInterceptor(authInterceptor)    // Untuk menambahkan token Bearer
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Gson untuk konversi JSON
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}