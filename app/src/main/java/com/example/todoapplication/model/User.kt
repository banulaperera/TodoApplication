package com.example.todoapplication.model

class User {
    var id: Int = 0
    var email: String = ""
    var password: String = ""

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    constructor() {

    }
}