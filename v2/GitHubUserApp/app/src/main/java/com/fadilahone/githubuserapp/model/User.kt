package com.fadilahone.githubuserapp.model


data class User(
    var username: String? = null,
    var name: String? = null,
    var location: String? = null,
    var company: String? = null,
    var avatar: String? = null,
    var repository :String? = null,
    var followers :String? = null,
    var following :String? = null
)