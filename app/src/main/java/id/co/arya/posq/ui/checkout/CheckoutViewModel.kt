package id.co.arya.posq.ui.checkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.co.arya.posq.api.MainRepository
import id.co.arya.posq.data.model.Cart
import java.util.ArrayList

class CheckoutViewModel(private val mainRepository: MainRepository): ViewModel() {

    private var allCart = MutableLiveData<ArrayList<Cart>>()

    fun setAllCartData(allCart: ArrayList<Cart>) {
        this.allCart.postValue(allCart)
    }

    fun getAllCartData(): MutableLiveData<ArrayList<Cart>> {
        return this.allCart
    }

}