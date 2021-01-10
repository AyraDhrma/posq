package id.co.arya.posq.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.MainRepository

class MainFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}