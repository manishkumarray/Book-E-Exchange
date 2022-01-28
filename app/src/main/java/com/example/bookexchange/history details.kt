package com.example.bookexchange

class historydetails {
    /// Model class
    var bookName: String? = null
    var authorName: String? = null
    var language: String? = null
    var bookImage: String? = null
    var date: String? = null

    constructor() {

    }

    constructor(bookName: String?, authorName: String?, language: String?, bookImage: String?, date:String?) {
        this.bookName = bookName
        this.authorName = authorName
        this.language = language
        this.bookImage = bookImage
        this.date = date
    }
}