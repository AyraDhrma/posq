package id.co.arya.posq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.co.arya.posq.R
import id.co.arya.posq.data.model.Cart
import id.co.arya.posq.local.SharedPreferenceManagerNoHilt
import kotlinx.android.synthetic.main.item_menu.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemMenuAdapter(var listProduct: ArrayList<Cart>) :
    RecyclerView.Adapter<ItemMenuAdapter.ViewHolder>(), Filterable {

    private var searchFilter: ItemSearchFilter? = null
    private var listProductFilter: ArrayList<Cart> = sharedPreferenceManagerNoHilt.getProductCart()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var onItemAdded: OnItemAdded
    private lateinit var sharedPreferenceManagerNoHilt: SharedPreferenceManagerNoHilt

    fun setOnItemSelected(onItemAdded: OnItemAdded) {
        this.onItemAdded = onItemAdded
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_menu,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return sharedPreferenceManagerNoHilt.getProductCart().size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            sharedPreferenceManagerNoHilt = SharedPreferenceManagerNoHilt(context)
            val localeID = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            remove.visibility = View.GONE
            count.visibility = View.GONE
            add.visibility = View.GONE
            Picasso.get().load(sharedPreferenceManagerNoHilt.getProductCart()[position].pr_image)
                .into(img_menu)
            title_menu.text = sharedPreferenceManagerNoHilt.getProductCart()[position].pr_nama
            price_menu.text =
                numberFormat.format(sharedPreferenceManagerNoHilt.getProductCart()[position].pr_harga.toInt())
                    .toString()
            count.text = sharedPreferenceManagerNoHilt.getProductCart()[position].total.toString()

            remove.visibility = View.VISIBLE
            count.visibility = View.VISIBLE
            add.visibility = View.VISIBLE

            add.setOnClickListener {
                var updateList = ArrayList<Cart>()
                var total: Int
                for ((index, a) in sharedPreferenceManagerNoHilt.getProductCart().indices.withIndex()) {
                    total = if (index == position
                        && sharedPreferenceManagerNoHilt.getProductCart()[index].pr_kode == sharedPreferenceManagerNoHilt.getProductCart()[position].pr_kode
                    ) {
                        sharedPreferenceManagerNoHilt.getProductCart()[position].total + 1
                    } else {
                        sharedPreferenceManagerNoHilt.getProductCart()[index].total
                    }
                    updateList.add(
                        Cart(
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_id,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_kode,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_nama,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_qty,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_tpel_kode,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_image,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_st_kode,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_us_id,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_harga,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_kategori,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_keterangan,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_created,
                            sharedPreferenceManagerNoHilt.getProductCart()[index].pr_modified,
                            total
                        )
                    )
                }
                val totalAdded = updateList[position].total
                onItemAdded.itemAdded(position, totalAdded, updateList)
                count.text = totalAdded.toString()

            }

            remove.setOnClickListener {
                if (sharedPreferenceManagerNoHilt.getProductCart()[position].total > 0) {
                    var updateList = ArrayList<Cart>()
                    updateList.clear()
                    var total = 0
                    for ((index, a) in sharedPreferenceManagerNoHilt.getProductCart().indices.withIndex()) {
                        total = if (index == position
                            && sharedPreferenceManagerNoHilt.getProductCart()[index].pr_kode == sharedPreferenceManagerNoHilt.getProductCart()[position].pr_kode
                        ) {
                            sharedPreferenceManagerNoHilt.getProductCart()[position].total - 1
                        } else {
                            sharedPreferenceManagerNoHilt.getProductCart()[index].total
                        }
                        updateList.add(
                            Cart(
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_id,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_kode,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_nama,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_qty,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_tpel_kode,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_image,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_st_kode,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_us_id,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_harga,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_kategori,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_keterangan,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_created,
                                sharedPreferenceManagerNoHilt.getProductCart()[index].pr_modified,
                                total
                            )
                        )
                    }
                    val totalAdded = updateList[position].total
                    onItemAdded.itemRemove(position, totalAdded, updateList)
                    count.text = totalAdded.toString()
                }
            }
        }
    }

    interface OnItemAdded {
        fun itemSelected(position: Int, listProductResponse: ArrayList<Cart>)
        fun itemAdded(position: Int, totalItems: Int, listProductResponse: ArrayList<Cart>)
        fun itemRemove(position: Int, totalItems: Int, listProductResponse: ArrayList<Cart>)
    }

    override fun getFilter(): Filter {
        if (searchFilter == null) {
            searchFilter = ItemSearchFilter(this, listProductFilter)
        }
        return searchFilter as ItemSearchFilter
    }

}

