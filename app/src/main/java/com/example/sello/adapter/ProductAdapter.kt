package com.example.sello.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sello.R
import com.example.sello.entity.Product

class ProductAdapter(var listener: OnClickImg) :
    RecyclerView.Adapter<ProductAdapter.PersonViewHolder>() {
    private var products = listOf<Product>()

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(product: Product) {
            val txtTitle = itemView.findViewById<TextView>(R.id.tv_product)
            val imgProduct = itemView.findViewById<ImageView>(R.id.imv_product)
            txtTitle.text = product.name

            Glide.with(itemView).load(product.image).into(imgProduct)

            imgProduct?.setOnClickListener {
                listener.onClickImg(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.onBind(products[position])
    }

    interface OnClickImg {
        fun onClickImg(product: Product)
    }

    override fun getItemCount() = products.size

    fun setProducts(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }
}