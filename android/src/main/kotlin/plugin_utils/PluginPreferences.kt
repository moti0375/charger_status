package plugin_utils

import android.content.Context

private const val PREFS_NAME = "PLUGIN_PREFS"
const val HEADLEASS_DISPATCHER_HANDLE = "HEADLESS_DISPATCHER_HANDLE"
object PluginPreferences {
    fun saveLongValue(context: Context, key: String, value: Long){
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putLong(key, value).apply()
    }

    fun getLongValue(context: Context, key: String) : Long {
        return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong(key, -1L)
    }
}