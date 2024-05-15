package com.example.todoapplication.model

data class Todo (
    var title: String,
    var description: String,
    var date: String,
    var time: String,
    var isChecked: Boolean = false
)