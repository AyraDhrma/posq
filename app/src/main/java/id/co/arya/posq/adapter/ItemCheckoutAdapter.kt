package id.co.arya.posq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.arya.posq.R
import id.co.arya.posq.data.model.Cart
import kotlinx.android.synthetic.main.item_checkout.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemCheckoutAdapter(private val listProduct: ArrayList<Cart>) :
    RecyclerView.Adapter<ItemCheckoutAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_checkout,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            title_menu.text = listProduct[position].pr_nama
            price_menu.text = numberFormat.format(listProduct[position].pr_harga.toInt())
            count.text = "items: x${listProduct[position].total}"
        }
    }

}

