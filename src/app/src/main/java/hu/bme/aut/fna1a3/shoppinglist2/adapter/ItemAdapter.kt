package hu.bme.aut.fna1a3.shoppinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.fna1a3.shoppinglist2.R
import hu.bme.aut.fna1a3.shoppinglist2.model.Item

class ItemAdapter(private val items: List<Item>,
                  private val onItemCheckboxClicked: (position: Int, status: Boolean) -> Unit
) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // onItemClick property
    var onItemClick: ((position: Int) -> Unit)? = null

    //helps use less referencing to components
    class ItemViewHolder(itemView: View, private val items: List<Item>) : RecyclerView.ViewHolder(itemView) {
        val ivCategory: ImageView = itemView.findViewById(R.id.ivCategory)
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvItemPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)




    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        val viewHolder = ItemViewHolder(view, items)

        view.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick?.invoke(position)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = items[position]

        //binding the data to the views
        holder.ivCategory.setImageResource(getIconResource(current.category))
        holder.tvItemName.text = current.itemName
        holder.tvItemPrice.text = "${current.estimatedPrice} HUF"
        holder.checkBox.isChecked = current.status

        holder.checkBox.setOnClickListener {
            val checked = holder.checkBox.isChecked
            onItemCheckboxClicked.invoke(position, checked)
        }
    }
    private fun getIconResource(category: String): Int {
        return when(category){
           "Education"-> R.drawable.ic_education
            "Food" -> R.drawable.ic_food
            "Toiletries" -> R.drawable.ic_toiletries
            else -> {R.drawable.ic_unknown}
        }
    }


}
