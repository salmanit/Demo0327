package com.charliesong.demo0327.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(version = 1,entities = arrayOf(User::class,Address::class,NameTuple::class))
open abstract class AppDatabase:RoomDatabase(){

    open abstract fun userDao(): UserDao
}