package com.example.todoapplication.model

class Todo {
    var id: Int = 0
    var userId: Int = 0
    var title: String = ""
    var description: String = ""
    var date: String = ""
    var time: String = ""
    var isChecked: Boolean = false

    constructor(
        title: String,
        description: String,
        date: String,
        time: String,
        isChecked: Boolean,
        userId: Int
    ) {
        this.title = title
        this.description = description
        this.date = date
        this.time = time
        this.isChecked = isChecked
        this.userId = userId
    }

    constructor() {

    }
}