package com.kamuran.earthquake.servis

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kamuran.earthquake.model.Besin

@Dao
interface BesinDao {

    @Insert
    suspend fun insertAll(vararg besin: Besin):List<Long>


    @Query("SELECT * FROM besin")
    suspend fun getAllBesin():List<Besin>

    @Query("SELECT * FROM besin WHERE uuid = :besinId")
    suspend fun getBesin(besinId : Int):Besin


    @Query("DELETE FROM besin")
    suspend fun deleteAllBesin()
}