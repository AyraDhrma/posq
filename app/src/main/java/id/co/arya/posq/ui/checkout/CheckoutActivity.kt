package id.co.arya.posq.ui.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.adapter.ItemCheckoutAdapter
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.RetrofitBuilder
import id.co.arya.posq.data.model.Cart
import id.co.arya.posq.local.AppDatabase
import id.co.arya.posq.local.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    @Inject
    lateinit var sharedPreferences: SharedPreferenceManager
    lateinit var checkoutViewModel: CheckoutViewModel
    lateinit var checkoutFactory: CheckoutFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        checkoutFactory = CheckoutFactory(ApiHelper(RetrofitBuilder.apiInterface))
        checkoutViewModel = ViewModelProvider(this, checkoutFactory)[CheckoutViewModel::class.java]
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "posq"
        ).allowMainThreadQueries()
            .build()

        val dao = db.dao()
        val price = dao.totalPrice()
        var allCart = dao.selectAllCart()
        checkoutViewModel.setAllCartData(allCart as java.util.ArrayList<Cart>)
        checkoutViewModel.getAllCartData().observe(this, androidx.lifecycle.Observer {
            response ->
            if (response != null) {
                val localeID = Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)
                rv_checkout.setHasFixedSize(true)
                rv_checkout.layoutManager = LinearLayoutManager(this)
                val adapter = ItemCheckoutAdapter(response)
                rv_checkout.adapter = adapter
                total_price.text = "Total ${numberFormat.format(price.toInt())}"
            }
        })
    }

}