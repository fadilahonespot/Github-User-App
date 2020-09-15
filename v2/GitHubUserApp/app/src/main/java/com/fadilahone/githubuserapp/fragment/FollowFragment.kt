package com.fadilahone.githubuserapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadilahone.githubuserapp.DetailUserActivity
import com.fadilahone.githubuserapp.view_model.MainViewModel
import com.fadilahone.githubuserapp.R
import com.fadilahone.githubuserapp.adapter.ListUserAdapter
import com.fadilahone.githubuserapp.model.ListUser
import kotlinx.android.synthetic.main.fragment_follow.*

class FollowFragment : Fragment() {
    companion object {
        const val EXTRA_METHOD = "extra_username"
        fun newInstance(method: String): FollowFragment {
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_METHOD, method)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var method = ""
        if (arguments != null) {
            method = arguments?.getString(EXTRA_METHOD) as String
        }

        showRecycleList( method)
    }

    private fun showRecycleList(method: String) {
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        rv_list_user.layoutManager = LinearLayoutManager(activity)
        rv_list_user.adapter = adapter

        // View Model Live Data
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        mainViewModel.setUsers("", method)
        mainViewModel.getUsers().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                progressBar.visibility = View.GONE

                if (userItems.size == 0) {
                    layout_empty_list.visibility = View.VISIBLE
                    rv_list_user.visibility = View.GONE
                    val splitMethod = method.split("/")
                    if (splitMethod[1] == "following") {
                        txt_not_follow.text = resources.getString(R.string.not_following)
                    } else {
                        txt_not_follow.text = resources.getString(R.string.not_followers)
                    }
                }
            }
        })

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListUser) {
                data.username?.let { moveSelectedUser(it) }
            }
        })
        rv_list_user.setHasFixedSize(true)
    }

    private fun moveSelectedUser(username: String) {
        val moveObject = Intent(activity, DetailUserActivity::class.java)
        moveObject.putExtra(DetailUserActivity.EXTRA_USER, username)
        startActivity(moveObject)
    }
}