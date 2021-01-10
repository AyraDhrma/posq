package id.co.arya.posq.ui.home

import androidx.lifecycle.MutableLiveData
import id.co.arya.posq.api.MainRepository
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.data.response.ListProductResponse
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.co.arya.posq.utils.Resource

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

//    fun listAllProduct(key: String, data: RequestParams): MutableLiveData<ListProductResponse> {
//        return mainRepository.listProduct(key, data)
//    }

    fun getListProduct(key: String, data: RequestParams) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getListProduct(key, data)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred"))
        }
    }

}