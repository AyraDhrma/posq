package id.co.arya.posq.api

import id.co.arya.posq.data.request.RequestParams

class ApiHelper(private val apiInterface: ApiInterface) {
    suspend fun getListProduct(key: String, data: RequestParams) = apiInterface.listProduct(key, data)

    suspend fun getListPaymentMethod(key: String, data: RequestParams) = apiInterface.listPaymentMethod(key, data)

    suspend fun saveOrder(key: String, data: RequestParams) = apiInterface.saveOrder(key, data)
}