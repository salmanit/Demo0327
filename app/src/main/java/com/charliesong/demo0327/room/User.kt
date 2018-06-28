package com.charliesong.demo0327.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Embedded



@Entity(tableName = "user")
class User (@PrimaryKey(autoGenerate = true) var uid:Int=0, @ColumnInfo(name = "firstName") var firstName: String = "",var lastName : String="", var age:Int=0){
//    @PrimaryKey
//    var uid: Int = 0
//
//    @ColumnInfo(name = "firstName")
//    var firstName: String = ""
//    @ColumnInfo(name = "lastName")
//    var lastName = ""
//
//    @Ignore
//    var age=0

    public var region:String=""
    @Embedded
    public var address: Address? = null
}