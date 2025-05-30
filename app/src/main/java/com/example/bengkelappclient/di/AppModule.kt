// di/AppModule.kt (atau buat DataStoreModule.kt)
package com.example.bengkelappclient.di


 import com.example.bengkelappclient.data.datastore.UserPreferenceManager
 import dagger.Module
 import dagger.hilt.InstallIn
 import dagger.hilt.android.qualifiers.ApplicationContext
 import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule { // Anda bisa gabungkan dengan module lain atau buat module terpisah

    // Tidak perlu provide UserPreferenceManager jika sudah @Inject constructor
    // @Provides
    // @Singleton
    // fun provideUserPreferenceManager(@ApplicationContext context: Context): UserPreferenceManager {
    //     return UserPreferenceManager(context)
    // }

    // Provider lain bisa ditambahkan di sini
}