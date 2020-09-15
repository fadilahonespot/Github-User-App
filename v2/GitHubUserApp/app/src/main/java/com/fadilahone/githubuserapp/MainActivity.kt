package com.fadilahone.githubuserapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadilahone.githubuserapp.adapter.ListUserAdapter
import com.fadilahone.githubuserapp.model.ListUser
import com.fadilahone.githubuserapp.view_model.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_ACCESS = BuildConfig.API_KEY_ACCESS
        const val TOKEN_ACCESS = BuildConfig.API_TOKEN_ACCESS
        const val SEARCH_KEY = "search_key"
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListUserAdapter

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.putExtra(SEARCH_KEY, query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_setting) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = intent.getStringExtra(SEARCH_KEY)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        if (username != null) {
            showRecycleList(username)
            mainViewModel.getSearchCount().observe(this, Observer { conResult ->
                if (conResult != null) {
                    title = resources.getString(R.string.search)
                    card_search.visibility = View.VISIBLE
                    txt_search_key.setTypeface(null, Typeface.ITALIC)
                    txt_search_key.text = HtmlCompat.fromHtml(
                        resources.getString(R.string.users_result, conResult, username),
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
                }
            })
        } else {
            showRecycleList("")
        }

        mainViewModel.getErrorMessage().observe(this, Observer { conResult ->
            if (conResult != null) {
                card_connection.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, conResult, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecycleList(username: String) {
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        rv_list_user.layoutManager = LinearLayoutManager(this)
        rv_list_user.adapter = adapter

        // View Model Live Data
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        mainViewModel.setUsers(username, "")
        mainViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                progressBar.visibility = View.GONE
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
        val moveObject = Intent(this@MainActivity, DetailUserActivity::class.java)
        moveObject.putExtra(DetailUserActivity.EXTRA_USER, username)
        startActivity(moveObject)
    }
}