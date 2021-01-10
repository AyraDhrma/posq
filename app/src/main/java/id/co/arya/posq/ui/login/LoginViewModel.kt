package id.co.arya.posq.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.arya.posq.api.MainRepository
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.data.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun login(key: String, data: RequestParams): MutableLiveData<LoginResponse> {
        return mainRepository.loginApi(key, data)
    }

}