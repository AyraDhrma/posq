package id.co.arya.posq.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("rc") @Expose val rc: String,
    @SerializedName("response") @Expose val response: String,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("struk") @Expose val struk: String
)
