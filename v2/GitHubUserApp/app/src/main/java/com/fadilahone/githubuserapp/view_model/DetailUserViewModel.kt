package com.fadilahone.githubuserapp.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadilahone.githubuserapp.MainActivity
import com.fadilahone.githubuserapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailUserViewModel : ViewModel() {

    private val TAG = DetailUserViewModel::class.java.simpleName
    private val detailUser = MutableLiveData<User>()
    private var mStatusCode =MutableLiveData<String>()

    fun setDetailUser(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}"
        client.addHeader(MainActivity.KEY_ACCESS, MainActivity.TOKEN_ACCESS)
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
                    val jsonObject = JSONObject(result)

                    val user = User()
                    user.avatar = jsonObject.getString("avatar_url")
                    user.name = jsonObject.getString("name")
                    user.location = jsonObject.getString("location")
                    user.company = jsonObject.getString("company")
                    user.repository = jsonObject.getString("public_repos")
                    user.following = jsonObject.getString("following")
                    user.followers = jsonObject.getString("followers")
                    user.username = username

                    detailUser.postValue(user)
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

    fun getDetailUser(): LiveData<User> {
        return detailUser
    }
}