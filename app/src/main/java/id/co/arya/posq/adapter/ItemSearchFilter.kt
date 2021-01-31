package id.co.arya.posq.adapter

import android.widget.Filter
import id.co.arya.posq.data.model.Cart

class ItemSearchFilter(
    private var adapter: ItemMenuAdapter,
    private var listItem: ArrayList<Cart>
): Filter() {

    override fun performFiltering(p0: CharSequence?): FilterResults {
        val results = FilterResults()
        if (p0 != null && p0.isNotEmpty()) {
            val query = p0.toString().toUpperCase()
            val filteredData: ArrayList<Cart> = ArrayList()
            for (i in 0 until listItem.size) {
                if (listItem[i].pr_nama.toUpperCase().contains(query)) {
                    filteredData.add(listItem[i])
                }
            }
            results.count = filteredData.size
            results.values = filteredData
        } else {
            results.count = listItem.size
            results.values = listItem
        }

        return results
    }

    override fun publishResults(p0: CharSequence, p1: FilterResults) {
        adapter.listProduct = p1.values as ArrayList<Cart>
        adapter.notifyDataSetChanged()
    }

}