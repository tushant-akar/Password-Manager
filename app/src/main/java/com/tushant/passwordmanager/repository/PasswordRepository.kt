package com.tushant.passwordmanager.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.map
import com.tushant.passwordmanager.db.PasswordDao
import com.tushant.passwordmanager.model.Password
import com.tushant.passwordmanager.utils.EncryptionUtils
import javax.crypto.SecretKey

class PasswordRepository(private val passwordDao: PasswordDao, private val context: Context) {

    private val secretKey: SecretKey = EncryptionUtils.getKey()

    fun getAll() = passwordDao.getAll().map { passwords ->
        passwords.map { password ->
            try {
                password.copy(
                    encryptedPassword = EncryptionUtils.decrypt(
                        encryptedData = password.encryptedPassword,
                        key = secretKey
                    )
                )
            } catch (e: Exception) {
                logError("Failed to decrypt password", e)
                password
            }
        }
    }

    suspend fun insertPassword(password: Password) {
        try {
            val encryptedPassword = password.copy(
                encryptedPassword = EncryptionUtils.encrypt(
                    data = password.encryptedPassword,
                    key = secretKey
                )
            )
            passwordDao.insertPassword(encryptedPassword)
        } catch (e: Exception) {
            logError("Failed to insert password", e)
            showToast("Failed to insert password")
        }
    }

    suspend fun deletePassword(password: Password) {
        try {
            passwordDao.deletePassword(password)
        } catch (e: Exception) {
            logError("Failed to delete password", e)
            showToast("Failed to delete password")
        }
    }

    suspend fun updatePassword(password: Password) {
        try {
            val encryptedPassword = password.copy(
                encryptedPassword = EncryptionUtils.encrypt(
                    data = password.encryptedPassword,
                    key = secretKey
                )
            )
            passwordDao.updatePassword(encryptedPassword)
        } catch (e: Exception) {
            logError("Failed to update password", e)
            showToast("Failed to update password")
        }
    }

    private fun logError(message: String, throwable: Throwable) {
        Log.e("PasswordRepository", message, throwable)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
