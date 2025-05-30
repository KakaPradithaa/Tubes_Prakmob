package com.example.bengkelappclient.data.local.dao

import androidx.room.*
import com.example.bengkelappclient.data.local.entity.BookingEntity

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings WHERE user_id = :userId ORDER BY booking_date DESC")
    suspend fun getBookingsByUser(userId: Int): List<BookingEntity>

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)
}
