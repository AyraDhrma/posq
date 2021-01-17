package id.co.arya.posq.ui.home

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.adapter.ItemMenuAdapter
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.RetrofitBuilder
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
        checkAlreadyLoginOrNot()

        // Search Input Setting Keyboard
        search.isFocusableInTouchMode = false
        search.isFocusable = false
        search.isFocusableInTouchMode = true
        search.isFocusable = true
        // ------------------------------------

        // Hardware EDC
        smartPosApi = ITYSmartPosApi.get(this)
        // ------------------------------------

        listener()
    }

    private fun checkAlreadyLoginOrNot() {
        try {
            if (sharedPreferences.loadUserData().us_id == "") {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else {
                mainFactory = MainFactory(ApiHelper(RetrofitBuilder.apiInterface))
                mainViewModel =
                    ViewModelProvider(this@MainActivity, mainFactory)[MainViewModel::class.java]
                getListProduct()

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

    private fun getListProduct() {
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
                if (dao.findByCode(listProductResponse[position].pr_kode).total == 0) {
                    dao.deleteItems(listProductResponse[position].pr_kode)
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
            val itemCount = dao.totalItem()
            val price = dao.totalPrice()
            if (response != null) {
                if (price.toInt() == 0) {
                    cart_div.visibility = View.GONE
                    rv_product.setPadding(0, 0, 0, 0)
                } else {
                    var padding = resources.getDimensionPixelOffset(R.dimen._60sdp)
                    rv_product.setPadding(0, 0, 0, padding)
                    cart_div.visibility = View.VISIBLE
                    tv_message.text = "$itemCount"
                    tv_action.text = "${numberFormat.format(price.toInt())}"
                    cart_div.setOnClickListener {
                        startActivity(Intent(this@MainActivity, CheckoutActivity::class.java))
                    }
                }
            }
        })
    }


}