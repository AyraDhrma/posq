package id.co.arya.posq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.co.arya.posq.R
import id.co.arya.posq.data.response.PaymentMethodResponse
import kotlinx.android.synthetic.main.item_payment_method.view.*

class RecyclerPaymentMethodAdapter(var paymentMethodResponse: PaymentMethodResponse) :
    RecyclerView.Adapter<RecyclerPaymentMethodAdapter.ViewHolder>() {

    lateinit var selectedPayment: SelectedPayment

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun onSelectedPayment(selectedPayment: SelectedPayment) {
        this.selectedPayment = selectedPayment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_payment_method,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return paymentMethodResponse.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            Picasso.get().load(paymentMethodResponse.data[position].image)
                .into(img_payment)
            payment_method.setOnClickListener {
                selectedPayment.onSelectedPayment(paymentMethodResponse, position)
            }
            title_payment.text = "${paymentMethodResponse.data[position].name}"
        }
    }

    interface SelectedPayment {
        fun onSelectedPayment(paymentMethodResponse: PaymentMethodResponse, position: Int)
    }

}

