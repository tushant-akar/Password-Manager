package com.tushant.passwordmanager

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.tushant.passwordmanager.db.PasswordDatabase
import com.tushant.passwordmanager.repository.PasswordRepository
import com.tushant.passwordmanager.ui.theme.PasswordManagerTheme
import com.tushant.passwordmanager.view.PasswordListScreen
import com.tushant.passwordmanager.viewModel.PasswordViewModel
import com.tushant.passwordmanager.viewModel.PasswordViewModelFactory

class MainActivity : FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var keyguardManager: KeyguardManager
    private val database by lazy { PasswordDatabase.getDatabase(applicationContext) }
    private val repository by lazy { PasswordRepository(database.passwordDao()) }
    private val viewModelFactory by lazy { PasswordViewModelFactory(repository) }
    private val viewModel by lazy { ViewModelProvider(this, viewModelFactory)[PasswordViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                PasswordListScreen(viewModel = viewModel)
            }
        }

        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        checkBiometricSupport()
    }

    private fun checkBiometricSupport() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                setupBiometricPrompt()
                showAuthenticationPrompt()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                showErrorMessage("Biometric authentication is not supported")
                showManualPinAuthentication()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                showErrorMessage("Biometric hardware is unavailable")
                showManualPinAuthentication()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showEnrollBiometricPrompt()
            }
            else -> {
                showErrorMessage("Biometric authentication is not supported")
                showManualPinAuthentication()
            }
        }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                when (errorCode) {
                    BiometricPrompt.ERROR_USER_CANCELED -> finish()
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON -> showManualPinAuthentication()
                    else -> {
                        showErrorMessage("Biometric authentication error: $errString")
                        showManualPinAuthentication()
                    }
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                setContent {
                    PasswordListScreen(viewModel = viewModel)
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showErrorMessage("Biometric authentication failed.")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Use fingerprint or face ID to unlock")
            .setNegativeButtonText("Use PIN")
            .build()
    }

    private fun showAuthenticationPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun showManualPinAuthentication() {
        val intent = keyguardManager.createConfirmDeviceCredentialIntent(
            "Unlock using your PIN",
            "Confirm your PIN to unlock the app"
        )
        if (intent != null) {
            startActivityForResult(intent, REQUEST_PIN_CODE)
        } else {
            showErrorMessage("Error: Unable to show PIN authentication")
        }
    }

    private fun showEnrollBiometricPrompt() {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG)
        }
        startActivityForResult(enrollIntent, REQUEST_ENROLL_BIOMETRIC)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PIN_CODE) {
            if (resultCode == RESULT_OK) {
                setContent {
                    PasswordListScreen(viewModel = viewModel)
                }
            } else {
                showErrorMessage("PIN authentication failed.")
                finish()
            }
        } else if (requestCode == REQUEST_ENROLL_BIOMETRIC) {
            if (resultCode == RESULT_OK) {
                setupBiometricPrompt()
                showAuthenticationPrompt()
            } else {
                showErrorMessage("Biometric enrollment failed")
                showManualPinAuthentication()
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_PIN_CODE = 1234
        private const val REQUEST_ENROLL_BIOMETRIC = 5678
    }
}
