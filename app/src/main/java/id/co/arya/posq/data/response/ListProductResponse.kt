package id.co.arya.posq.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListProductResponse(
    @SerializedName("rc") @Expose val rc: String,
    @SerializedName("response") @Expose val response: String,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("data") @Expose val data: List<Data>
) {
    data class Data(
        @SerializedName("pr_id") @Expose val pr_id: String,
        @SerializedName("pr_kode") @Expose val pr_kode: String,
        @SerializedName("pr_nama") @Expose val pr_nama: String,
        @SerializedName("pr_qty") @Expose val pr_qty: String,
        @SerializedName("pr_tpel_kode") @Expose val pr_tpel_kode: String,
        @SerializedName("pr_image") @Expose val pr_image: String,
        @SerializedName("pr_st_kode") @Expose val pr_st_kode: String,
        @SerializedName("pr_us_id") @Expose val pr_us_id: String,
        @SerializedName("pr_harga") @Expose val pr_harga: String,
        @SerializedName("pr_keterangan") @Expose val pr_keterangan: String,
        @SerializedName("pr_created") @Expose val pr_created: String,
        @SerializedName("pr_modified") @Expose val pr_modified: String
    )
}
