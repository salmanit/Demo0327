package com.charliesong.demo0327.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.charliesong.demo0327.app.MyApplication


object UtilRoomDB{

   private var db:AppDatabase?=null
    fun getInstanceDB():AppDatabase{
        return  db?:Room.databaseBuilder(MyApplication.getInstance(),
                AppDatabase::class.java, "firstRoomDB")
                .addCallback(object :RoomDatabase.Callback() {
                    //第一次创建数据库时调用，但是在创建所有表之后调用的
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                    //当数据库被打开时调用
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                    }

                })
                .allowMainThreadQueries()//允许在主线程查询数据
                .addMigrations()//迁移数据库使用，下面会单独拿出来讲
                .fallbackToDestructiveMigration()//迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃

                .build()
    }
}