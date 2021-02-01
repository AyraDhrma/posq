package id.co.arya.posq.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentMethodResponse(
    @SerializedName("rc") @Expose val rc: String,
    @SerializedName("response") @Expose val response: String,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("data") @Expose val data: List<Data>
) {
    data class Data(
        @SerializedName("code") @Expose val code: String,
        @SerializedName("name") @Expose val name: String,
        @SerializedName("image") @Expose val image: String
    )

}
