package id.co.arya.posq.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.MainRepository
import id.co.arya.posq.ui.home.MainViewModel

class LoginFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}