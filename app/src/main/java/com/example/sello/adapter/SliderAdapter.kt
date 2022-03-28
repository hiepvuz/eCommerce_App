package com.example.sello.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sello.R
import com.example.sello.entity.Product


class SliderAdapter(private var listener: ProductAdapter.OnClickImg) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private var itemSlider = listOf<Product>()

    inner class SliderViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(product: Product){
            val img: ImageView = view.findViewById(R.id.imv_item_viewpager)
            Glide.with(view).load(product.image).into(img)
            img.setOnClickListener {
                listener.onClickImg(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_viewpager, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.onBind(itemSlider[position])
    }

    override fun getItemCount(): Int = itemSlider.size

    fun setItemSlider(itemSlider: List<Product>){
        this.itemSlider = itemSlider
        notifyDataSetChanged()
    }
}