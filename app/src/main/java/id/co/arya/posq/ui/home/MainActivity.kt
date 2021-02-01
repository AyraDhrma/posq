package id.co.arya.posq.ui.home

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.adapter.ItemMenuAdapter
import id.co.arya.posq.adapter.RecyclerMenuAdapter
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
import kotlinx.android.synthetic.main.item_menu.view.*
import kotlinx.android.synthetic.main.item_menu_all.view.*
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ItemMenuAdapter
    private lateinit var db: AppDatabase

    @Inject
    lateinit var sharedPreferences: SharedPreferenceManager
    lateinit var smartPosApi: ITYSmartPosApi
    lateinit var mainViewModel: MainViewModel
    lateinit var mainFactory: MainFactory
    private lateinit var listProduct: ArrayList<Cart>
    private lateinit var listKategori: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressbar.visibility = View.GONE
        checkAlreadyLoginOrNot()

        // Hardware EDC
        smartPosApi = ITYSmartPosApi.get(this)
        // ------------------------------------

        listener()
    }

    private fun showNoData() {
        img_no_data.visibility = View.VISIBLE
        refresh.visibility = View.VISIBLE
    }

    private fun hideNoData() {
        img_no_data.visibility = View.GONE
        refresh.visibility = View.GONE
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

        refresh.setOnClickListener {
            getListProduct()
        }
        img_no_data.setOnClickListener {
            getListProduct()
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @SuppressLint("SetTextI18n")
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
                                            resource.data.data[index].pr_kategori,
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

                                rv_product.adapter = RecyclerMenuAdapter(
                                    itemCount = resource.data.kategori.size,
                                    createHolder = { parent, _ ->
                                        val view = LayoutInflater.from(parent.context).inflate(
                                            R.layout.item_menu_all, parent, false
                                        )
                                        ItemHolder(view)
                                    },
                                    bindHolder = { holder, position ->
                                        val listSubMenu = holder.itemView.list_sub_menu
                                        var codeCategory = resource.data.kategori[position].pkr_kode

                                        var countKategori = 0
                                        for ((index, a) in resource.data.data.indices.withIndex()) {
                                            if (resource.data.data[index].pr_kategori == resource.data.kategori[position].pkr_kode) {
                                                countKategori++
                                            }
                                        }

                                        var indexContainsArray = ArrayList<Int>()
                                        indexContainsArray.clear()
                                        for ((index, a) in sharedPreferences.getProductCart().indices.withIndex()) {
                                            if (sharedPreferences.getProductCart()[index].pr_kategori == codeCategory) {
                                                indexContainsArray.add(index)
                                            }
                                        }

                                        holder.itemView.apply {
                                            title.text = resource.data.kategori[position].pkr_nama
                                            listSubMenu.hasFixedSize()
                                            listSubMenu.layoutManager = LinearLayoutManager(context)
                                            listSubMenu.adapter = RecyclerMenuAdapter(
                                                itemCount = countKategori,
                                                createHolder = { parent, _ ->
                                                    val view =
                                                        LayoutInflater.from(parent.context).inflate(
                                                            R.layout.item_menu, parent, false
                                                        )
                                                    ItemHolder(view)
                                                },
                                                bindHolder = { subholder, subposition ->
                                                    var indexItemsContains =
                                                        indexContainsArray[subposition]
                                                    subholder.itemView.apply {
                                                        val localeID = Locale("in", "ID")
                                                        val numberFormat =
                                                            NumberFormat.getCurrencyInstance(
                                                                localeID
                                                            )
                                                        Picasso.get()
                                                            .load(sharedPreferences.getProductCart()[indexItemsContains].pr_image)
                                                            .into(img_menu)
                                                        title_menu.text =
                                                            sharedPreferences.getProductCart()[indexItemsContains].pr_nama
                                                        price_menu.text =
                                                            numberFormat.format(
                                                                sharedPreferences.getProductCart()[indexItemsContains].pr_harga.toInt()
                                                            )
                                                                .toString()
                                                        count.text =
                                                            sharedPreferences.getProductCart()[indexItemsContains].total.toString()

                                                        remove.visibility = View.VISIBLE
                                                        count.visibility = View.VISIBLE
                                                        add.visibility = View.VISIBLE

                                                        add.setOnClickListener {
                                                            var updateList = ArrayList<Cart>()
                                                            var total: Int
                                                            for ((index, a) in sharedPreferences.getProductCart().indices.withIndex()) {
                                                                total =
                                                                    if (index == indexItemsContains
                                                                        && sharedPreferences.getProductCart()[index].pr_kode == sharedPreferences.getProductCart()[indexItemsContains].pr_kode
                                                                    ) {
                                                                        sharedPreferences.getProductCart()[indexItemsContains].total + 1
                                                                    } else {
                                                                        sharedPreferences.getProductCart()[index].total
                                                                    }
                                                                updateList.add(
                                                                    Cart(
                                                                        sharedPreferences.getProductCart()[index].pr_id,
                                                                        sharedPreferences.getProductCart()[index].pr_kode,
                                                                        sharedPreferences.getProductCart()[index].pr_nama,
                                                                        sharedPreferences.getProductCart()[index].pr_qty,
                                                                        sharedPreferences.getProductCart()[index].pr_tpel_kode,
                                                                        sharedPreferences.getProductCart()[index].pr_image,
                                                                        sharedPreferences.getProductCart()[index].pr_st_kode,
                                                                        sharedPreferences.getProductCart()[index].pr_us_id,
                                                                        sharedPreferences.getProductCart()[index].pr_harga,
                                                                        sharedPreferences.getProductCart()[index].pr_kategori,
                                                                        sharedPreferences.getProductCart()[index].pr_keterangan,
                                                                        sharedPreferences.getProductCart()[index].pr_created,
                                                                        sharedPreferences.getProductCart()[index].pr_modified,
                                                                        total
                                                                    )
                                                                )
                                                            }
                                                            val totalAdded =
                                                                updateList[indexItemsContains].total
                                                            count.text = totalAdded.toString()
                                                            sharedPreferences.saveProductCart(
                                                                updateList
                                                            )
                                                            insertToDatabase(
                                                                indexItemsContains,
                                                                totalAdded,
                                                                updateList
                                                            )
                                                            showCart()
                                                        }

                                                        remove.setOnClickListener {
                                                            if (sharedPreferences.getProductCart()[indexItemsContains].total > 0) {
                                                                var updateList =
                                                                    ArrayList<Cart>()
                                                                updateList.clear()
                                                                var total = 0
                                                                for ((index, a) in sharedPreferences.getProductCart().indices.withIndex()) {
                                                                    total =
                                                                        if (index == indexItemsContains
                                                                            && sharedPreferences.getProductCart()[index].pr_kode == sharedPreferences.getProductCart()[indexItemsContains].pr_kode
                                                                        ) {
                                                                            sharedPreferences.getProductCart()[indexItemsContains].total - 1
                                                                        } else {
                                                                            sharedPreferences.getProductCart()[index].total
                                                                        }
                                                                    updateList.add(
                                                                        Cart(
                                                                            sharedPreferences.getProductCart()[index].pr_id,
                                                                            sharedPreferences.getProductCart()[index].pr_kode,
                                                                            sharedPreferences.getProductCart()[index].pr_nama,
                                                                            sharedPreferences.getProductCart()[index].pr_qty,
                                                                            sharedPreferences.getProductCart()[index].pr_tpel_kode,
                                                                            sharedPreferences.getProductCart()[index].pr_image,
                                                                            sharedPreferences.getProductCart()[index].pr_st_kode,
                                                                            sharedPreferences.getProductCart()[index].pr_us_id,
                                                                            sharedPreferences.getProductCart()[index].pr_harga,
                                                                            sharedPreferences.getProductCart()[index].pr_kategori,
                                                                            sharedPreferences.getProductCart()[index].pr_keterangan,
                                                                            sharedPreferences.getProductCart()[index].pr_created,
                                                                            sharedPreferences.getProductCart()[index].pr_modified,
                                                                            total
                                                                        )
                                                                    )
                                                                }
                                                                val totalAdded =
                                                                    updateList[indexItemsContains].total
                                                                count.text =
                                                                    totalAdded.toString()
                                                                sharedPreferences.saveProductCart(
                                                                    updateList
                                                                )
                                                                removeFromDatabase(
                                                                    indexItemsContains,
                                                                    totalAdded,
                                                                    updateList
                                                                )
                                                                showCart()
                                                            }
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                        PagerSnapHelper().attachToRecyclerView(listSubMenu)
                                    }
                                )

                                listKategori = ArrayList()
                                for ((index, a) in resource.data.kategori.indices.withIndex()) {
                                    listKategori.add(resource.data.kategori[index].pkr_nama)
                                }

                                var spinnerDialog = SpinnerDialog(
                                    this@MainActivity,
                                    listKategori,
                                    "Select Category",
                                    R.style.DialogAnimations_SmileWindow,
                                    "Close"
                                )

                                spinnerDialog.setCancellable(true)
                                spinnerDialog.setShowKeyboard(false)
                                title_kategori.text = "Filter : ${listKategori[0]}"
                                spinnerDialog.bindOnSpinerListener { item, position ->
                                    rv_product.smoothScrollToPosition(position)
                                    search.setText("$item")
                                }
                                findViewById<View>(R.id.search).setOnClickListener { spinnerDialog.showSpinerDialog() }
                            } else {
                                Toast.makeText(this, resource.data?.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        Status.ERROR -> {
                            progressbar.visibility = View.GONE
                            showNoData()
                        }
                        Status.LOADING -> {
                            hideNoData()
                            progressbar.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }

    fun removeFromDatabase(
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

    fun insertToDatabase(
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

    fun showCart() {
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