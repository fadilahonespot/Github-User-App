package com.fadilahone.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fadilahone.githubuserapp.R
import com.fadilahone.githubuserapp.model.ListUser
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListUserAdapter() : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val mData = ArrayList<ListUser>()
    fun setData(items: ArrayList<ListUser>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
        // Onclick
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(mData[holder.adapterPosition]) }
    }

    inner class ListViewHolder(itemView :View) :RecyclerView.ViewHolder(itemView){
        fun bind(userItem :ListUser) {
            with(itemView) {
                tv_name.text = userItem.username
                tv_location.text = userItem.id.toString()
                tv_company.text = userItem.type
                Glide.with(itemView.context).load(userItem.avatar).apply(RequestOptions().placeholder(R.drawable.user8).override(72, 72)).into(img_user_photo)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListUser)
    }
}