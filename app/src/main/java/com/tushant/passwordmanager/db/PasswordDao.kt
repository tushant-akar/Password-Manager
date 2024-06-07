package com.tushant.passwordmanager.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tushant.passwordmanager.model.Password

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password")
    fun getAll(): LiveData<List<Password>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: Password)

    @Delete
    suspend fun deletePassword(password: Password)

    @Update
    suspend fun updatePassword(password: Password)

}