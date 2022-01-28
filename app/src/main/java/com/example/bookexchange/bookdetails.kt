package com.example.bookexchange

class bookdetails
{
    /// Model class
    var bookName : String? = null
    var authorName : String? = null
    var language : String? = null
    var bookImage : String? = null
    var sid : String? = null
    var rid : String? = null
    var bookid: String? = null
    var dis: String? = null

    constructor(){

    }

    constructor(bookName: String?, authorName: String?,language:String?, bookImage: String?,sid: String?,rid: String?,bookid:String?,dis: String?) {
        this.bookName = bookName
        this.authorName = authorName
        this.language = language
        this.bookImage = bookImage
        this.sid = sid
        this.rid = rid
        this.bookid = bookid
        this.dis = dis
    }
}