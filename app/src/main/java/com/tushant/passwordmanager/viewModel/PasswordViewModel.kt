package com.tushant.passwordmanager.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tushant.passwordmanager.model.Password
import com.tushant.passwordmanager.repository.PasswordRepository
import kotlinx.coroutines.launch

class PasswordViewModel(private val repository: PasswordRepository): ViewModel() {
    val passwords: LiveData<List<Password>> = repository.getAll()

    fun insertPassword(password: Password) {
        viewModelScope.launch {
            repository.insertPassword(password)
        }
    }

    fun deletePassword(password: Password) {
        viewModelScope.launch {
            repository.deletePassword(password)
        }
    }

    fun updatePassword(password: Password) {
        viewModelScope.launch {
            repository.updatePassword(password)
        }
    }
}
