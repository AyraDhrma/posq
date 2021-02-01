package id.co.arya.posq.api

import androidx.lifecycle.MutableLiveData
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.data.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class MainRepository
@Inject constructor(private val apiHelper: ApiHelper) {

    private val apiService = ApiService("").api()
    val TAG = MainRepository::class.java.simpleName

    fun loginApi(key: String, data: RequestParams): MutableLiveData<LoginResponse> {

        var resultApi: MutableLiveData<LoginResponse> = MutableLiveData()

        apiService.loginApi(key, data).enqueue(object : Callback<LoginResponse> {

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val resultData = LoginResponse.Data("", "", "",
                        "", "", "", "", "", "",
                        "", "", "", "", "")
                val result = LoginResponse("ERR", "", t.localizedMessage, resultData)
                resultApi.postValue(result)
                Timber.d(TAG, t.localizedMessage)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    resultApi.postValue(response.body())
                    Timber.d(TAG, response.body().toString())
                } else {
                    val resultData = LoginResponse.Data("", "", "",
                            "", "", "", "", "", "",
                            "", "", "", "", "")
                    val result = LoginResponse("ERR", "", response.errorBody().toString(), resultData)
                    resultApi.postValue(result)
                    Timber.d(TAG, response.errorBody().toString())
                }
            }

        })

        return resultApi

    }

    suspend fun getListProduct(key: String, data: RequestParams) = apiHelper.getListProduct(key, data)

    suspend fun getListPaymentMethod(key: String, data: RequestParams) = apiHelper.getListPaymentMethod(key, data)

    suspend fun saveOrder(key: String, data: RequestParams) = apiHelper.saveOrder(key, data)

}
