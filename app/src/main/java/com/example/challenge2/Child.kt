package com.example.challenge2

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import java.util.*

@Entity
class Child {
    @Id
    var id: Long = 0
    var name: String? = null
    var gender: String? = null
    var dob: Date? = null
    var timeStamp: Date? = null

    constructor(id: Long, name: String?, gender: String?, dob: Date?,timeStamp: Date?) {
        this.id = id
        this.name = name
        this.gender = gender
        this.dob = dob
        this.timeStamp = timeStamp
    }

    constructor() {}
}