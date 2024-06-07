package com.tushant.passwordmanager.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object EncryptionUtils {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "PasswordManagerKey"
    private const val KEY_SIZE = 256
    private const val ALGORITHM = "AES/CBC/PKCS7Padding"
    private const val IV_SIZE = 16

    fun encrypt(data: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val cipherText = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        val combined = ByteArray(iv.size + cipherText.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(cipherText, 0, combined, iv.size, cipherText.size)

        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String, key: SecretKey): String {
        val decoded = Base64.decode(encryptedData, Base64.DEFAULT)
        val cipher = Cipher.getInstance(ALGORITHM)
        val iv = IvParameterSpec(decoded, 0, IV_SIZE)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)

        val decryptedText = cipher.doFinal(decoded, IV_SIZE, decoded.size- IV_SIZE)
        return String(decryptedText, StandardCharsets.UTF_8)
    }

    fun generateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        // Check if key already exists
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                setKeySize(KEY_SIZE)
                build()
            }
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }

        return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }


    fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }
}
