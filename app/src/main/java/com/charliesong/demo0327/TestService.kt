package com.charliesong.demo0327

import android.app.IntentService
import android.content.Intent

class TestService:IntentService{
    constructor() : super("default")
    constructor(name: String?) : super(name)

    override fun onHandleIntent(intent: Intent?) {
        println("=========handleIntent==${Thread.currentThread().name}")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        println("============onStart")
    }
}