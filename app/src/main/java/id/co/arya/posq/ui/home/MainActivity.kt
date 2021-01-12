package id.co.arya.posq.ui.home

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.adapter.ItemMenuAdapter
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.RetrofitBuilder
import id.co.arya.posq.costumeview.snackbarcart.SnackbarCart
import id.co.arya.posq.data.model.Cart
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.local.AppDatabase
import id.co.arya.posq.local.SharedPreferenceManager
import id.co.arya.posq.ui.checkout.CheckoutActivity
import id.co.arya.posq.ui.login.LoginActivity
import id.co.arya.posq.ui.menunav.MenuNavFragment
import id.co.arya.posq.utils.Constant
import id.co.arya.posq.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    @Inject
    lateinit var sharedPreferences: SharedPreferenceManager
    lateinit var smartPosApi: ITYSmartPosApi
    lateinit var mainViewModel: MainViewModel
    lateinit var mainFactory: MainFactory
    private lateinit var listProduct: ArrayList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressbar.visibility = View.GONE
        checkAlreadyLogginOrNot()

        // Search Input Setting Keyboard
        search.isFocusableInTouchMode = false
        search.isFocusable = false
        search.isFocusableInTouchMode = true
        search.isFocusable = true
        // ------------------------------------

        // Hardware EDC
        smartPosApi = ITYSmartPosApi.get(this)
        // ------------------------------------

        // Cart Show
//        val clickListener: View.OnClickListener = View.OnClickListener {
//        }
//        SnackbarCart.make(
//            search_div, "1000 Items", Snackbar.LENGTH_INDEFINITE,
//            clickListener, R.drawable.ic_shopping_bag, "Rp 100.000.000",
//            ContextCompat.getColor(this@MainActivity, R.color.purple_700)
//        )?.show()
        // ----------------------------------------------------------------------------

        listener()
    }

    private fun checkAlreadyLogginOrNot() {
        try {
            if (sharedPreferences.loadUserData().us_id == "") {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                mainFactory = MainFactory(ApiHelper(RetrofitBuilder.apiInterface))
                mainViewModel =
                    ViewModelProvider(this@MainActivity, mainFactory)[MainViewModel::class.java]
                getLitProduct()

                db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "posq"
                ).allowMainThreadQueries()
                    .build()

                val dao = db.dao()
                dao.emptyCart()
            }
        } catch (e: Exception) {

        }
    }

    private fun listener() {
        // Menu
        menu_navigation.setOnClickListener {
            val menunavfragment = MenuNavFragment()
            menunavfragment.show(supportFragmentManager, Constant.menuNavFragmentTag)
        }
        // ---------------------------------------------------------------------------
    }

    private fun getLitProduct() {
        mainViewModel.getListProduct(
            Constant.KEY,
            RequestParams(sharedPreferences.loadUserData().us_id)
        )
            .observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressbar.visibility = View.GONE
                            if (resource.data?.rc == "0000") {

                                listProduct = ArrayList()
                                for ((index, a) in resource.data.data.indices.withIndex()) {
                                    listProduct.add(
                                        Cart(
                                            resource.data.data[index].pr_id,
                                            resource.data.data[index].pr_kode,
                                            resource.data.data[index].pr_nama,
                                            resource.data.data[index].pr_qty,
                                            resource.data.data[index].pr_tpel_kode,
                                            resource.data.data[index].pr_image,
                                            resource.data.data[index].pr_st_kode,
                                            resource.data.data[index].pr_us_id,
                                            resource.data.data[index].pr_harga,
                                            resource.data.data[index].pr_keterangan,
                                            resource.data.data[index].pr_created,
                                            resource.data.data[index].pr_modified,
                                            0
                                        )
                                    )
                                }

                                rv_product.setHasFixedSize(true)
                                rv_product.layoutManager = LinearLayoutManager(this)
                                sharedPreferences.saveProductCart(listProduct)
                                val adapter = ItemMenuAdapter(sharedPreferences.getProductCart())
                                rv_product.adapter = adapter
                                adapter.setOnItemSelected(object : ItemMenuAdapter.OnItemAdded {
                                    override fun itemSelected(
                                        position: Int,
                                        listProductResponse: ArrayList<Cart>
                                    ) {

                                    }

                                    override fun itemAdded(
                                        position: Int,
                                        totalItems: Int,
                                        listProductResponse: ArrayList<Cart>
                                    ) {
                                        sharedPreferences.saveProductCart(listProductResponse)
                                        insertToDatabase(position, totalItems, listProductResponse)
                                        showCart()
                                    }

                                    override fun itemRemove(
                                        position: Int,
                                        totalItems: Int,
                                        listProductResponse: ArrayList<Cart>
                                    ) {
                                        sharedPreferences.saveProductCart(listProductResponse)
                                        removeFromDatabase(
                                            position,
                                            totalItems,
                                            listProductResponse
                                        )
                                        showCart()
                                    }

                                })
                            } else {
                                Toast.makeText(this, resource.data?.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        Status.ERROR -> {
                            progressbar.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            progressbar.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }

    private fun removeFromDatabase(
        position: Int,
        totalItems: Int,
        listProductResponse: ArrayList<Cart>
    ) {
        try {
            val dao = db.dao()
            if (dao.findByCode(listProductResponse[position].pr_kode) != null) {
                if (dao.findByCode(listProductResponse[position].pr_kode).pr_harga.toInt() == 0) {
                     dao.deleteItems(listProductResponse[position].pr_id)
                } else {
                    val totalPrice = totalItems * listProduct[position].pr_harga.toInt()
                    dao.updateCart(totalItems, totalPrice, listProductResponse[position].pr_kode)
                }
            }
        } catch (e: SQLiteException) {
            Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertToDatabase(
        position: Int,
        totalItems: Int,
        listProductResponse: ArrayList<Cart>
    ) {
        try {
            val dao = db.dao()
            if (dao.findByCode(listProductResponse[position].pr_kode) != null) {
                // dao.updateCart(listProductResponse[position])
                val totalPrice = totalItems * listProduct[position].pr_harga.toInt()
                dao.updateCart(totalItems, totalPrice, listProductResponse[position].pr_kode)
            } else {
                dao.insertCart(listProductResponse[position])
            }
        } catch (e: SQLiteException) {
            Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCart() {
        val dao = db.dao()
        var allCart = dao.selectAllCart()
        mainViewModel.setAllCartData(allCart as java.util.ArrayList<Cart>)
        mainViewModel.getAllCartData().observe(this, Observer { response ->
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            val price = dao.totalPrice()
            val itemCount = dao.totalItem()
            if (response != null) {
                if (itemCount.toInt() != 0) {
                    // Cart Show
                    val clickListener: View.OnClickListener = View.OnClickListener {
                        startActivity(Intent(this@MainActivity, CheckoutActivity::class.java))
                    }
                    SnackbarCart.make(
                        search_div, itemCount, Snackbar.LENGTH_INDEFINITE,
                        clickListener, R.drawable.ic_shopping_bag, numberFormat.format(price.toInt()),
                        ContextCompat.getColor(this@MainActivity, R.color.purple_700)
                    )?.show()
                    // ----------------------------------------------------------------------------
                }
            }
        })
    }


}