package com.charliesong.demo0327.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update
import android.arch.lifecycle.LiveData

@Dao
open interface UserDao{

    @Query("select * from user")
    fun getUsersFromSync():LiveData<List<User>>


    @Query("select * from user")
    fun getAllUser():List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Update
    fun updateUsers(vararg users: User)

    @Query("SELECT * FROM user WHERE age > :minAge")
    fun loadAllUsersOlderThan(minAge: Int): Array<User>

    //可以查询几列，返回的数据用一个新的对象来接收，只要字段名字和查询的一样即可
    @Query("SELECT firstName, lastName FROM user")
    fun loadFullName(): List<NameTuple>


    @Query("SELECT firstName, lastName FROM user WHERE region IN (:regions)")
    fun loadUsersFromRegionsSync(regions: List<String>): LiveData<List<User>>
}