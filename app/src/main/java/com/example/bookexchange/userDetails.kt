package com.example.bookexchange

class userDetails {
    var userName : String? = null
    var userId : String? = null
    var senderId : String? = null
    var profileImage : String? = null

    constructor(){
    }

    constructor(userName : String?,profileImage : String?, userId: String?,senderId : String?) {
        this.userName = userName
        this.userId = userId
        this.senderId =senderId
        this.profileImage = profileImage
    }
}