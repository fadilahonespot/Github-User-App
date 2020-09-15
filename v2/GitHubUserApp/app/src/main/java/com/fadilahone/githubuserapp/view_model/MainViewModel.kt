package com.fadilahone.githubuserapp.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadilahone.githubuserapp.MainActivity
import com.fadilahone.githubuserapp.model.ListUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val listUser = MutableLiveData<ArrayList<ListUser>>()
    private val searchCount = MutableLiveData<String>()
    private var mStatusCode =MutableLiveData<String>()
    private val TAG = MainViewModel::class.java.simpleName

    fun setUsers(username: String, method: String) {

        val listItems = ArrayList<ListUser>()
        val client = AsyncHttpClient()
        var url = "https://api.github.com/users"

        if (username != "" && method == "") {
            url = "https://api.github.com/search/users?q=$username"
        }

        if (username == "" && method != "") {
            url = "https://api.github.com/users/$method"
        }

        client.addHeader(
            MainActivity.KEY_ACCESS,
            MainActivity.TOKEN_ACCESS
        )
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    var jsonArray: JSONArray? = null
                    if (username != "" && method == "") {
                        val jsonObjectResult = JSONObject(result)
                        jsonArray = jsonObjectResult.getJSONArray("items")

                        val mConResult = jsonObjectResult.getInt("total_count")
                        searchCount.postValue(mConResult.toString())
                    } else {
                        jsonArray = JSONArray(result)
                    }
                    if (jsonArray != null) {
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val userItem = ListUser()
                            userItem.username= jsonObject.getString("login")
                            userItem.avatar = jsonObject.getString("avatar_url")
                            userItem.type = jsonObject.getString("type")
                            userItem.id = jsonObject.getInt("id")

                            listItems.add(userItem)
                        }
                        listUser.postValue(listItems)
                    }

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure: ", error?.message.toString())
                mStatusCode.postValue(error?.message)
            }
        })
    }

    fun getErrorMessage() :LiveData<String> {
        return mStatusCode
    }

    fun getSearchCount(): LiveData<String> {
        return searchCount
    }

    fun getUsers(): LiveData<ArrayList<ListUser>> {
        return listUser
    }
}