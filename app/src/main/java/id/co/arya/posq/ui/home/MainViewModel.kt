package id.co.arya.posq.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.co.arya.posq.api.MainRepository
import id.co.arya.posq.data.model.Cart
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.util.*

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    private var allCart = MutableLiveData<ArrayList<Cart>>()

    fun getListProduct(key: String, data: RequestParams) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getListProduct(key, data)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred"))
        }
    }

    fun getListPaymentMethod(key: String, data: RequestParams) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getListPaymentMethod(key, data)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred"))
        }
    }

    fun saveOrder(key: String, data: RequestParams) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.saveOrder(key, data)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred"))
        }
    }

    fun setAllCartData(allCart: ArrayList<Cart>) {
        this.allCart.postValue(allCart)
    }

    fun getAllCartData(): MutableLiveData<ArrayList<Cart>> {
        return this.allCart
    }

}