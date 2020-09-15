package com.fadilahone.githubuserapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadilahone.githubuserapp.adapter.ListUserAdapter
import com.fadilahone.githubuserapp.model.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addData()
        showRecycleList()
    }

    @SuppressLint("Recycle")
    private fun addData() {
        val dataUsername :Array<String> = resources.getStringArray(R.array.username)
        val dataName :Array<String> = resources.getStringArray(R.array.name)
        val dataImage :TypedArray = resources.obtainTypedArray(R.array.avatar)
        val dataLocation :Array<String> = resources.getStringArray(R.array.location)
        val dataRepository :Array<String> = resources.getStringArray(R.array.repository)
        val dataCompany :Array<String> = resources.getStringArray(R.array.company)
        val dataFollower :Array<String> = resources.getStringArray(R.array.followers)
        val dataFollowing :Array<String> = resources.getStringArray(R.array.following)

        for (position in dataName.indices) {
            val user = User(
                dataUsername[position],
                dataName[position],
                dataImage.getResourceId(position, -1),
                dataLocation[position],
                dataRepository[position],
                dataCompany[position],
                dataFollower[position],
                dataFollowing[position]
            )
            users.add(user)
        }
    }

    private fun showRecycleList() {
        rv_list_user.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(users)
        rv_list_user.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                moveSelectedUser(data)
            }
        })
        rv_list_user.setHasFixedSize(true)
    }

    private fun moveSelectedUser(user :User) {
        val moveObject = Intent(this@MainActivity, DetailUserActivity::class.java)
        moveObject.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(moveObject)
    }
}