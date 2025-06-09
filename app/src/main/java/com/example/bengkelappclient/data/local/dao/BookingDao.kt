package com.example.bengkelappclient.data.local.dao

import androidx.room.*
import com.example.bengkelappclient.data.local.entity.BookingEntity
import com.example.bengkelappclient.data.model.BookingWithVehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings WHERE user_id = :userId ORDER BY booking_date DESC")
    suspend fun getBookingsByUser(userId: Int): List<BookingEntity>

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)

    @Transaction
    @Query("SELECT * FROM bookings WHERE user_id = :userId ORDER BY booking_date DESC")
    fun getBookingsWithVehicleByUser(userId: Int): Flow<List<BookingWithVehicle>>

    @Transaction
    @Query("SELECT * FROM bookings WHERE id = :bookingId")
    fun getBookingWithVehicleById(bookingId: Int): Flow<BookingWithVehicle?>

    @Transaction
    @Query("SELECT * FROM bookings WHERE user_id = :userId AND status = :status ORDER BY booking_date DESC")
    fun getBookingsWithVehicleByUserAndStatus(userId: Int, status: String): Flow<List<BookingWithVehicle>>

}