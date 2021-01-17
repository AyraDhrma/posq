package id.co.arya.posq.ui.checkout

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.adapter.ItemCheckoutAdapter
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.RetrofitBuilder
import id.co.arya.posq.data.model.Cart
import id.co.arya.posq.data.model.CheckoutItems
import id.co.arya.posq.local.AppDatabase
import id.co.arya.posq.local.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private var price = ""
    private lateinit var db: AppDatabase

    @Inject
    lateinit var sharedPreferences: SharedPreferenceManager
    lateinit var checkoutViewModel: CheckoutViewModel
    lateinit var checkoutFactory: CheckoutFactory
    private lateinit var listCheckout: ArrayList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Input Setting Keyboard
        costumer_name_input.isFocusableInTouchMode = false
        costumer_name_input.isFocusable = false
        costumer_name_input.isFocusableInTouchMode = true
        costumer_name_input.isFocusable = true
        // ------------------------------------

        checkoutFactory = CheckoutFactory(ApiHelper(RetrofitBuilder.apiInterface))
        checkoutViewModel = ViewModelProvider(this, checkoutFactory)[CheckoutViewModel::class.java]
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "posq"
        ).allowMainThreadQueries()
            .build()

        val dao = db.dao()
        price = dao.totalPrice()
        var allCart = dao.selectAllSelectedCart()
        checkoutViewModel.setAllCartData(allCart as java.util.ArrayList<Cart>)
        checkoutViewModel.getAllCartData().observe(this, androidx.lifecycle.Observer { response ->
            if (response != null) {
                listCheckout = response
                val localeID = Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                rv_checkout.setHasFixedSize(true)
                rv_checkout.layoutManager = LinearLayoutManager(this)
                val adapter = ItemCheckoutAdapter(response)
                rv_checkout.adapter = adapter
                total_price.text = "Total ${numberFormat.format(price?.toInt())}"
            }
        })

        order.setOnClickListener {
            if (costumer_name_input.text.toString() == "") {
                costumer_name_input.error = resources.getString(R.string.nama_costumer_harus_diisi)
            } else {
                val gson = Gson()
                val checkoutData = CheckoutItems(
                    price,
                    costumer_name_input.text.toString(),
                    listCheckout
                )
                val listCheckoutData: String = gson.toJson(checkoutData)
                Log.d("POSQ", listCheckoutData)
                Toast.makeText(this@CheckoutActivity, listCheckoutData, Toast.LENGTH_SHORT).show()
            }
        }
    }

}