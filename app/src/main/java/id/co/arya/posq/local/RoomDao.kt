package id.co.arya.posq.local

import androidx.room.*
import id.co.arya.posq.data.model.Cart
import java.math.BigInteger

@Dao
interface RoomDao {

    @Query("SELECT * FROM cart WHERE pr_kode = :pr_kode")
    fun findByCode(pr_kode: String): Cart

    @Insert
    fun insertCart(cart: Cart)

    @Query("UPDATE cart SET total = :total, pr_harga = :price WHERE pr_kode = :code")
    fun updateCart(total: Int, price: Int, code: String)

    @Query("SELECT * FROM cart")
    fun selectAllCart(): List<Cart>

    @Query("DELETE FROM cart")
    fun emptyCart()

    @Query("DELETE FROM cart WHERE pr_kode = :pr_kode")
    fun deleteItems(pr_kode: String)

//    @Delete
//    fun deleteItems(cart: Cart)

    @Query("SELECT SUM(pr_harga) FROM cart")
    fun totalPrice(): String

    @Query("SELECT COUNT(pr_harga) FROM cart")
    fun totalItem(): String
}