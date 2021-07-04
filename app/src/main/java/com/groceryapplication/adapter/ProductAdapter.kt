package com.groceryapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.groceryapplication.R
import com.groceryapplication.model.ProductDetail

class ProductAdapter(val context:Context, val list:ArrayList<ProductDetail>)
    :RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private  var onClick:ProductAdapter.onItemClickListener?=null

    fun setOnClickListener(onItemClick: onItemClickListener)
    {
        this.onClick=onItemClick
    }



    inner  class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivfav=itemView.findViewById<ImageView>(R.id.ivfav)
        var ivProduct=itemView.findViewById<ImageView>(R.id.ivProduct)
        var ivAdd=itemView.findViewById<ImageView>(R.id.ivAdd)
        var ivMinus=itemView.findViewById<ImageView>(R.id.ivMinus)
        var tvProductName=itemView.findViewById<TextView>(R.id.tvProductName)
        var tvProductPrice=itemView.findViewById<TextView>(R.id.tvProductPrice)
        var tvQunatity=itemView.findViewById<TextView>(R.id.tvQunatity)
        var tvAdd=itemView.findViewById<TextView>(R.id.tvAdd)
        var clQuantity=itemView.findViewById<ConstraintLayout>(R.id.clQuantity)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_products,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvProductName.text=list[position].name
        holder.tvProductPrice.text=list[position].price
        Glide.with(context).load(list[position].image).into(holder.ivProduct)

        if(list[position].quantity!!>1)
        {
            holder.clQuantity.visibility=View.VISIBLE
            holder.tvAdd.visibility=View.GONE
            holder.tvQunatity.text=list[position].quantity.toString()
        }
        else
        {
            holder.clQuantity.visibility=View.GONE
            holder.tvAdd.visibility=View.VISIBLE
        }

        holder.tvAdd.setOnClickListener(View.OnClickListener {
            holder.tvAdd.visibility=View.GONE
            holder.clQuantity.visibility=View.VISIBLE
            onClick!!.onItemClickAdd(list[position], 1)

        })

        holder.ivAdd.setOnClickListener(View.OnClickListener {
            val quant=holder.tvQunatity.text.toString().toInt()
            val newquant=quant.plus(1)
            holder.tvQunatity.text=newquant.toString()
            onClick!!.onItemClickUpdate(list[position], newquant)

        })
        holder.ivMinus.setOnClickListener(View.OnClickListener {
            val quant=holder.tvQunatity.text.toString().toInt()
            if(quant==1)
            {
                holder.tvAdd.visibility=View.VISIBLE
                holder.clQuantity.visibility=View.GONE
                onClick!!.onItemClickDelete(list[position])
            }
            else{
                val newquant=quant.minus(1)
                holder.tvQunatity.text=newquant.toString()
                onClick!!.onItemClickUpdate(list[position], newquant)
            }



        })

    }

    override fun getItemCount(): Int {
        return  list.size
    }

    interface  onItemClickListener{
        fun onItemClickAdd(body:ProductDetail, quantity:Int)
        fun onItemClickDelete(body:ProductDetail)
        fun onItemClickUpdate(body:ProductDetail, quantity:Int)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}