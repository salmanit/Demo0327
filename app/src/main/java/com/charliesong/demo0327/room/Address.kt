package com.charliesong.demo0327.room

import android.arch.persistence.room.ColumnInfo

open class Address {
    var street: String? = null
    var state: String? = null
    var city: String? = null

    @ColumnInfo(name = "post_code")
    var postCode: Int = 0
}
