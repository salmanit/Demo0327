/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.charliesong.demo0327.navigation

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.*
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder
import com.charliesong.demo0327.R
import java.text.SimpleDateFormat
import java.util.*

class DeepLinkAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.deep_link_appwidget
        )
        println("appWidgetIds==============${Arrays.toString(appWidgetIds)}")

        val args = Bundle()
        args.putString("myarg", "From Widget")
        // TODO Step 11 - construct and set a PendingIntent using DeepLinkBuilder
        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.fragmentStepOne)
            .setArguments(args)
                .setComponentName(ActivityNavigationTest::class.java)
            .createPendingIntent()

        remoteViews.setOnClickPendingIntent(R.id.deep_link, pendingIntent)
        // TODO ENDSTEP 11
        remoteViews.setTextViewText(R.id.tv_current,format.format(Date(System.currentTimeMillis())))
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
    }
    val format=SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA)

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        println("onAppWidgetOptionsChanged=================$newOptions===${newOptions.getInt(OPTION_APPWIDGET_MIN_WIDTH )}===${newOptions?.getInt(OPTION_APPWIDGET_MAX_WIDTH)}==" +
                "${newOptions.getInt(OPTION_APPWIDGET_MIN_HEIGHT)}===========${newOptions.getInt(OPTION_APPWIDGET_MAX_HEIGHT)}")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        println("onDeleted===============")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        println("onDisabled================")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        println("onEnabled=================")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        println("onRestored=======================")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        println("onReceive======================${intent?.action}")
        super.onReceive(context, intent)
    }
}
