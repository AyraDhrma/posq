package id.co.arya.posq.api

import id.co.arya.posq.data.request.RequestParams

class ApiHelper(private val apiInterface: ApiInterface) {
    suspend fun getListProduct(key: String, data: RequestParams) = apiInterface.listProduct(key, data)
}