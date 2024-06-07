package com.tushant.passwordmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password")
data class Password(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val accountType: String,
    val email: String,
    val encryptedPassword: String,
)
