package com.fadilahone.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fadilahone.githubuserapp.R
import com.fadilahone.githubuserapp.model.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListUserAdapter(private val listUser :ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ListViewHolder(view)
    }

    inner class ListViewHolder(itemView :View) :RecyclerView.ViewHolder(itemView){
        val name :TextView  = itemView.tv_name
        val location :TextView = itemView.tv_location
        val company :TextView = itemView.tv_company
        val userImage :CircleImageView  = itemView.img_user_photo
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(holder.itemView.context).load(user.image).apply(RequestOptions().override(72, 72)).into(holder.userImage)
        holder.name.text = user.name
        holder.location.text = user.location
        holder.company.text = user.company

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}