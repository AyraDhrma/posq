package id.co.arya.posq.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.MainRepository
import id.co.arya.posq.api.RetrofitBuilder
import id.co.arya.posq.costumeview.snackbarcart.SnackbarCart
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.local.SharedPreferenceManager
import id.co.arya.posq.ui.login.LoginActivity
import id.co.arya.posq.ui.menunav.MenuNavFragment
import id.co.arya.posq.utils.Constant
import id.co.arya.posq.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferenceManager
    lateinit var smartPosApi: ITYSmartPosApi
    lateinit var mainViewModel: MainViewModel
    lateinit var mainFactory: MainFactory

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
        val clickListener: View.OnClickListener = View.OnClickListener {
        }
        SnackbarCart.make(
            search_div, "1000 Items", Snackbar.LENGTH_INDEFINITE,
            clickListener, R.drawable.ic_shopping_bag, "Rp 100.000.000",
            ContextCompat.getColor(this, R.color.purple_700)
        )?.show()
        // ----------------------------------------------------------------------------

        listener()
    }

    private fun checkAlreadyLogginOrNot() {
//        try {
            if (sharedPreferences.loadUserData().us_id == "") {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                mainFactory = MainFactory(ApiHelper(RetrofitBuilder.apiInterface))
                mainViewModel =
                    ViewModelProvider(this@MainActivity, mainFactory)[MainViewModel::class.java]
                getLitProduct()
            }
//        } catch (e: Exception) {
//
//        }
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
                                Toast.makeText(this, resource.data?.message, Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, resource.data?.message, Toast.LENGTH_LONG).show()
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
}