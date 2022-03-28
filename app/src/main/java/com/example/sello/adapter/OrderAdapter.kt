package com.example.sello.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sello.R
import com.example.sello.dao.AppDao
import com.example.sello.database.AppDatabase
import com.example.sello.entity.Cart

class OrderAdapter(application: Application) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var carts = listOf<Cart>()

    private val appDao: AppDao

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(application)
        appDao = appDatabase.dao
    }

    inner class OrderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun onBind(cart: Cart){
            val tvName = itemView.findViewById<TextView>(R.id.productName)
            val imgProduct = itemView.findViewById<ImageView>(R.id.productImg)
            val tvPrice = itemView.findViewById<TextView>(R.id.price)
            val tvQuantity = itemView.findViewById<TextView>(R.id.quantity)
            val product = appDao.findProductByID(cart.productID)
            tvName.text = product.name
            Glide.with(itemView).load(product.image).into(imgProduct)
            tvPrice.text = (product.price*cart.quantity).toString()
            tvQuantity.text = cart.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.onBind(carts[position])
    }

    override fun getItemCount(): Int = carts.size

    fun setOrders(carts: List<Cart>) {
        this.carts = carts
        notifyDataSetChanged()
    }

}