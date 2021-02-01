package id.co.arya.posq.ui.paymentmethod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.arya.posq.R
import id.co.arya.posq.adapter.RecyclerPaymentMethodAdapter
import id.co.arya.posq.api.ApiHelper
import id.co.arya.posq.api.RetrofitBuilder
import id.co.arya.posq.data.request.RequestParams
import id.co.arya.posq.data.response.PaymentMethodResponse
import id.co.arya.posq.ui.home.MainFactory
import id.co.arya.posq.ui.home.MainViewModel
import id.co.arya.posq.ui.payment.PaymentDialog
import id.co.arya.posq.utils.Constant
import id.co.arya.posq.utils.Status
import kotlinx.android.synthetic.main.fragment_payment_method_dialog.*

class PaymentMethodDialog : BottomSheetDialogFragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var mainFactory: MainFactory
    lateinit var adapter: RecyclerPaymentMethodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_method_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainFactory = MainFactory(ApiHelper(RetrofitBuilder.apiInterface))
        mainViewModel =
            activity?.let { ViewModelProvider(it, mainFactory) }!![MainViewModel::class.java]

        activity?.let {
            mainViewModel.getListPaymentMethod(
                Constant.KEY,
                RequestParams("")
            ).observe(it, Observer { resources ->
                when (resources.status) {
                    Status.LOADING -> {
                        progressbar.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        progressbar.visibility = View.GONE
                        if (resources.data!!.rc == "0000") {
                            rv_payment_method.setHasFixedSize(true)
                            rv_payment_method.layoutManager = LinearLayoutManager(activity)
                            adapter = RecyclerPaymentMethodAdapter(resources.data)
                            adapter.notifyDataSetChanged()
                            rv_payment_method.adapter = adapter
                            adapter.onSelectedPayment(object :
                                RecyclerPaymentMethodAdapter.SelectedPayment {
                                override fun onSelectedPayment(
                                    paymentMethodResponse: PaymentMethodResponse,
                                    position: Int
                                ) {
                                    val payment = PaymentDialog(
                                        paymentMethodResponse.data[position].code,
                                        paymentMethodResponse,
                                        position
                                    )
                                    activity?.supportFragmentManager?.let { it1 -> payment.show(it1, "PAYMENT") }
                                    dismiss()
                                }
                            })
                        } else {
                            Toast.makeText(activity, resources.data?.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    Status.ERROR -> {
                        progressbar.visibility = View.GONE
                        Toast.makeText(activity, resources.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
        }

        close.setOnClickListener {
            dismiss()
        }
    }
}