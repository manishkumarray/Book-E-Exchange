package com.example.bookexchange

class acceptDetails {
    /// Model class
    var bookName : String? = null
    var sid : String? = null
    var rid : String? = null

    constructor(){
    }

    constructor(bookName: String?,sid: String?,rid : String?) {
        this.bookName = bookName
        this.sid = sid
        this.rid = rid
    }
}