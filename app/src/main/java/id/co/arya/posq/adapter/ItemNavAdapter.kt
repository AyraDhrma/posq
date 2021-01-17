package id.co.arya.posq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.arya.posq.R
import kotlinx.android.synthetic.main.item_navigation.view.*

class ItemNavAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<ItemNavAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var onNavSelected: onNavSelected

    fun onSelectedNavigation(onNavSelected: onNavSelected) {
        this.onNavSelected = onNavSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_navigation, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            title_nav.text = list[position]
            setOnClickListener {
                onNavSelected.onNavSelected(position, list)
            }
        }
    }
}

interface onNavSelected {
    fun onNavSelected(position: Int, list: ArrayList<String>)
}