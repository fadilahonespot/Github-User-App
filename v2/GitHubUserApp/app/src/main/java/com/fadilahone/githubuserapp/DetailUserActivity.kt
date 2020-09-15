package com.fadilahone.githubuserapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fadilahone.githubuserapp.adapter.SectionPageAdapter
import com.fadilahone.githubuserapp.model.User
import com.fadilahone.githubuserapp.view_model.DetailUserViewModel
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var detailViewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val user = intent.getStringExtra(EXTRA_USER)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)
        detailViewModel.setDetailUser(user)

        detailViewModel.getDetailUser().observe(this, Observer { userItem ->
            if (userItem != null) {
                setData(this, userItem)
            }
        })

        detailViewModel.getErrorMessage().observe(this, Observer { conResult ->
            if (conResult != null) {
                card_connection.visibility = View.VISIBLE
                Toast.makeText(this, conResult, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setData(context :Context, item : User) {
        txt_name.text = item.name
        txt_username.text = item.username
        txt_repository.text = resources.getString(R.string.repo, item.repository)
        txt_location_detail.text = item.location
        txt_company_detail.text = item.company
        Glide.with(context).load(item.avatar).apply(RequestOptions().override(140, 140)).into(image_user)

        title = item.username
        progressBar.visibility = View.GONE
        // Tab Layout Adapter
        val sectionsPagerAdapter = SectionPageAdapter(context, supportFragmentManager, item.followers, item.following, item.username)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
}