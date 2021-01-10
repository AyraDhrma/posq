package id.co.arya.posq.api

import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.data.response.ListProductResponse
import id.co.arya.posq.data.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

    @POST("Auth/login")
    fun loginApi(
            @Header("x-api-key") apiKey: String,
            @Body data: RequestParams
    ): Call<LoginResponse>

    @POST("Product/list_produk")
    suspend fun listProduct(
            @Header("x-api-key") apiKey: String,
            @Body data: RequestParams
    ): ListProductResponse
}