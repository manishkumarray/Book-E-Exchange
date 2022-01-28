package com.example.bookexchange

class Notificationdetails
{
    /// Model class
    var bookName : String? = null
    var sid : String? = null
    var rid : String? = null
    var bookid : String? = null

    constructor(){

    }

    constructor(bookName: String?,sid: String?,rid : String?,bookid : String?) {
        this.bookName = bookName
        this.sid = sid
        this.rid = rid
        this.bookid = bookid
    }
}