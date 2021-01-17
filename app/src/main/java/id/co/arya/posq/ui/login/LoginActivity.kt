package id.co.arya.posq.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.RetrofitBuilder
import id.co.arya.posq.costumeview.snackbarcart.SnackbarCart
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.local.SharedPreferenceManager
import id.co.arya.posq.ui.home.MainActivity
import id.co.arya.posq.utils.Constant
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginFactory: LoginFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViewModel()

        listener()
    }

    private fun initViewModel() {
        loginFactory = LoginFactory(ApiHelper(RetrofitBuilder.apiInterface))
        loginViewModel =
            ViewModelProvider(this, loginFactory)[LoginViewModel::class.java]
    }

    private fun listener() {
        login_button.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val username = username_input.text.toString().trim()
                val password = md5(password_input.text.toString().trim())
                loginViewModel.login(Constant.KEY, RequestParams("${username}|${password}"))
                        .observe(this@LoginActivity, Observer { response ->
                            when (response.rc) {
                                "0000" -> {
                                    sharedPreferenceManager.saveUserData(response)
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    finish()
                                }
                                "ERR" -> {
                                    // Cart Show
                                    val clickListener: View.OnClickListener = View.OnClickListener {
                                    }
                                    SnackbarCart.make(
                                            login_button, "", Snackbar.LENGTH_LONG,
                                            clickListener, null, response.message,
                                            ContextCompat.getColor(this@LoginActivity, R.color.purple_700)
                                    )?.show()
                                    // ----------------------------------------------------------------------------
                                }
                                else -> {
                                    // Cart Show
                                    val clickListener: View.OnClickListener = View.OnClickListener {
                                    }
                                    SnackbarCart.make(
                                            login_button, "", Snackbar.LENGTH_LONG,
                                            clickListener, null, response.message,
                                            ContextCompat.getColor(this@LoginActivity, R.color.purple_700)
                                    )?.show()
                                    // ----------------------------------------------------------------------------
                                }
                            }
                        })
            }
        }
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}