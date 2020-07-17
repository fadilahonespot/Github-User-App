package com.fadilahone.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fadilahone.githubuserapp.model.User
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val user = intent.getParcelableExtra(EXTRA_USER) as User
        txt_name.text = user.name
        txt_username.text = user.username
        txt_follower.text = user.followers
        txt_following.text = user.following
        txt_repository.text = user.repository
        text_name_detail.text = user.name
        text_username_detail.text = user.username
        text_location_detail.text = user.location
        text_company_detail.text = user.company
        Glide.with(this).load(user.image).apply(RequestOptions().override(140, 140)).into(image_user)
    }
}