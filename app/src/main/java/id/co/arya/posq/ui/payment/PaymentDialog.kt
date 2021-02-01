package id.co.arya.posq.ui.payment

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.co.arya.posq.R
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.RetrofitBuilder
import id.co.arya.posq.data.model.CheckoutItems
import id.co.arya.posq.data.response.PaymentMethodResponse
import id.co.arya.posq.local.SharedPreferenceManager
import id.co.arya.posq.ui.home.MainFactory
import id.co.arya.posq.ui.home.MainViewModel
import kotlinx.android.synthetic.main.fragment_payment_dialog.*
import javax.inject.Inject

@AndroidEntryPoint
class PaymentDialog(
    private val paymentMethodCode: String, private val paymentMethodResponse: PaymentMethodResponse,
    private val position: Int
) : DialogFragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var mainFactory: MainFactory
    @Inject lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainFactory = MainFactory(ApiHelper(RetrofitBuilder.apiInterface))
        mainViewModel =
            activity?.let { ViewModelProvider(it, mainFactory) }!![MainViewModel::class.java]

        Picasso.get().load(paymentMethodResponse.data[position].image).into(img_payment)

        if (paymentMethodCode == "4") {
            label_payment_reference.text = "Cash"
            payment_reference_input.inputType = InputType.TYPE_CLASS_NUMBER
        } else {
            label_payment_reference.text = "Payment Reference"
        }

        close.setOnClickListener {
            dismiss()
        }
        order.setOnClickListener {
            if (payment_reference_input.text.toString() == "") {
                payment_reference_input.error = resources.getString(R.string.cannot_null)
            } else {
                val checkoutData = CheckoutItems(
                    sharedPreferenceManager.getJsonRequestCheckout().total,
                    sharedPreferenceManager.getJsonRequestCheckout().userid,
                    sharedPreferenceManager.getJsonRequestCheckout().costumername,
                    paymentMethodCode,
                    payment_reference_input.text.toString(),
                    sharedPreferenceManager.getJsonRequestCheckout().items
                )

                sharedPreferenceManager.saveJsonRequestCheckout(checkoutData)

                val gson = Gson()
                val listCheckoutData: String = gson.toJson(sharedPreferenceManager.getJsonRequestCheckout())

                Toast.makeText(activity, listCheckoutData, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

}