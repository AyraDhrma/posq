package id.co.arya.posq.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse (
        @SerializedName("rc") @Expose val rc: String,
        @SerializedName("response") @Expose val response: String,
        @SerializedName("message") @Expose val message: String,
        @SerializedName("data") @Expose val data: Data
) {
    data class Data (
            @SerializedName("us_id") @Expose val us_id: String,
            @SerializedName("us_username") @Expose val us_username: String,
            @SerializedName("us_password") @Expose val us_password: String,
            @SerializedName("us_nama") @Expose val us_nama: String,
            @SerializedName("us_alamat") @Expose val us_alamat: String,
            @SerializedName("us_kota") @Expose val us_kota: String,
            @SerializedName("us_photo") @Expose val us_photo: String,
            @SerializedName("us_hp") @Expose val us_hp: String,
            @SerializedName("us_keterangan") @Expose val us_keterangan: String,
            @SerializedName("us_email") @Expose val us_email: String,
            @SerializedName("us_aktif") @Expose val us_aktif: String,
            @SerializedName("us_create_by") @Expose val us_create_by: String,
            @SerializedName("us_created") @Expose val us_created: String,
            @SerializedName("us_modified") @Expose val us_modified: String
    )
}
