package com.charliesong.demo0327.assitservice

import android.content.ContentValues.TAG
import android.content.Context
import android.provider.Settings
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
import android.text.TextUtils
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent


/**
 * Created by charlie.song on 2018/3/30.
 */
 object  AssistUtil{
    /**
     * 检测辅助功能是否开启
     */
      fun isAccessibilitySettingsOn(mContext: Context,mClas :Class<*>): Boolean {
        var accessibilityEnabled = 0
        val service = mContext.getPackageName() + "/" + mClas.getCanonicalName()
        // com.z.buildingaccessibilityservices/android.accessibilityservice.AccessibilityService
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED)
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled)
        } catch (e: Settings.SettingNotFoundException) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.message)
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------")
            val settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            // com.z.buildingaccessibilityservices/com.z.buildingaccessibilityservices.TestService
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()

                    Log.v(TAG, "-------------- > accessibilityService :: $accessibilityService $service")
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!")
                        return true
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***")
        }
        return false
    }

     fun goSetService(mContext: Context){
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        mContext.startActivity(intent)
    }
}