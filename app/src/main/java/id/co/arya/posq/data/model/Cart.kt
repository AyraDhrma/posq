package id.co.arya.posq.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey val pr_id: String,
    @ColumnInfo(name = "pr_kode") val pr_kode: String,
    @ColumnInfo(name = "pr_nama") val pr_nama: String,
    @ColumnInfo(name = "pr_qty") val pr_qty: String,
    @ColumnInfo(name = "pr_tpel_kode") val pr_tpel_kode: String,
    @ColumnInfo(name = "pr_image") val pr_image: String,
    @ColumnInfo(name = "pr_st_kode") val pr_st_kode: String,
    @ColumnInfo(name = "pr_us_id") val pr_us_id: String,
    @ColumnInfo(name = "pr_harga") val pr_harga: String,
    @ColumnInfo(name = "pr_keterangan") val pr_keterangan: String,
    @ColumnInfo(name = "pr_created") val pr_created: String,
    @ColumnInfo(name = "pr_modified") val pr_modified: String,
    @ColumnInfo(name = "total") var total: Int
)