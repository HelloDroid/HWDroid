package com.hw.hwdroid.foundation.utils.storage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.content.SharedPreferencesCompat
import android.text.TextUtils
import com.hw.hwdroid.foundation.utils.StringUtils
import com.hw.hwdroid.foundation.utils.json.GsonUtils
import com.orhanobut.logger.Logger
import java.io.File

/**
 * 删除文件
 * @param context
 * @param fileName
 */
fun delFile(context: Context, fileName: String) {
    try {
        val file = File("/dataList/dataList/" + context.packageName + "/shared_prefs", fileName)
        if (file.exists()) {
            file.delete()
        }
    } catch (e: Exception) {
    }
}


/**
 * 清除所有数据
 * @param context
 * @param fileName
 */
@SuppressLint("ApplySharedPref")
fun clear(context: Context, fileName: String) {
    val sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    val editor = sp.edit()
    editor.clear().commit()
    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor)
}

/**
 * remove key
 * @param context
 * @param file
 * @param key
 */
fun remove(context: Context, file: String, key: String): Boolean {
    if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
        return false
    }

    val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
    return editor.remove(key).commit()
}

/**
 * save value
 * @param context
 * @param file
 * @param key
 * @param value
 */
fun setStorage(context: Context, file: String, key: String, value: Any?): Boolean {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return false
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()

        if (value == null) {
            return editor.remove(key).commit()
        } else {
            if (value is Int) {
                editor.putInt(key, value)
            } else if (value is Short) {
                editor.putInt(key, value.toInt())
            } else if (value is Long) {
                editor.putLong(key, value)
            } else if (value is Float) {
                editor.putFloat(key, value)
            } else if (value is Double) {
                editor.putFloat(key, value.toFloat())
            } else if (value is Boolean) {
                editor.putBoolean(key, value)
            } else if (value is String) {
                editor.putString(key, value)
            } else if (value is Any) {
                editor.putString(key, GsonUtils.toJson(value))
            }
        }
        return editor.commit()
    } catch (e: Exception) {
        Logger.e(e)
        return false
    }
}

/**
 * save values
 * @param context
 * @param file
 * @param values
 */
@SuppressLint("ApplySharedPref")
fun setStorage(context: Context, file: String, values: Map<String, Any?>) {
    try {
        if (TextUtils.isEmpty(file)) {
            return
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()

        for (key in values.keys) {
            val value = values[key]
            if (value == null) {
                editor.remove(key)
            } else {
                if (value is Int) {
                    editor.putInt(key, value)
                } else if (value is Short) {
                    editor.putInt(key, value.toInt())
                } else if (value is Long) {
                    editor.putLong(key, value)
                } else if (value is Float) {
                    editor.putFloat(key, value)
                } else if (value is Double) {
                    editor.putFloat(key, value.toFloat())
                } else if (value is Boolean) {
                    editor.putBoolean(key, value)
                } else if (value is String) {
                    editor.putString(key, value)
                } else if (value is Any) {
                    editor.putString(key, GsonUtils.toJson(value))
                }
            }
        }
        editor.commit()
    } catch (e: Exception) {
        Logger.e(e)
    }
}

/**
 * save values
 * @param context
 * @param file
 * @param values
 */
@SuppressLint("ApplySharedPref")
fun setStorage(context: Context, file: String, values: Bundle) {
    try {
        if (TextUtils.isEmpty(file)) {
            return
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()

        for (key in values.keySet()) {
            val value = values[key]
            if (value == null) {
                editor.remove(key)
            } else {
                if (value is Int) {
                    editor.putInt(key, value)
                } else if (value is Short) {
                    editor.putInt(key, value.toInt())
                } else if (value is Long) {
                    editor.putLong(key, value)
                } else if (value is Float) {
                    editor.putFloat(key, value)
                } else if (value is Double) {
                    editor.putFloat(key, value.toFloat())
                } else if (value is Boolean) {
                    editor.putBoolean(key, value)
                } else if (value is String) {
                    editor.putString(key, value)
                } else if (value is Any) {
                    editor.putString(key, GsonUtils.toJson(value))
                }
            }
        }
        editor.commit()
    } catch (e: Exception) {
        Logger.e(e)
    }
}

@Suppress("UNCHECKED_CAST", "JAVA_CLASS_ON_COMPANION")
fun <T> getData(context: Context, file: String, key: String, cls: Class<T>): T? {
    try {
        if (cls == String.javaClass) {
            return context.getSharedPreferences(file, Context.MODE_PRIVATE).getString(key, null) as? T
        }

        if (cls == Int.javaClass || cls == Short.javaClass) {
            return context.getSharedPreferences(file, Context.MODE_PRIVATE).getInt(key, 0) as? T
        }

        if (cls == Float.javaClass || cls == Double.javaClass) {
            return context.getSharedPreferences(file, Context.MODE_PRIVATE).getFloat(key, 0f) as? T
        }
        if (cls == Long.javaClass) {
            return context.getSharedPreferences(file, Context.MODE_PRIVATE).getLong(key, 0) as? T
        }

//        if (cls as Boolean) {
//            return context.getSharedPreferences(file, Context.MODE_PRIVATE).getBoolean(key, false) as? T
//        }

        val v = getString(context, file, key, "")
        if (StringUtils.isNullOrWhiteSpace(v)) {
            return null
        }

        return GsonUtils.fromJson<T>(v, cls)
    } catch (e: Exception) {
        Logger.e(e)
        return null
    }
}

/**
 * put String
 * @param context
 * @param file
 * @param key
 * @param value
 */
fun setString(context: Context, file: String, key: String, value: String?): Boolean {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return false
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
        if (TextUtils.isEmpty(value)) {
            return editor.remove(key).commit()
        }

        editor.putString(key, value)
        return editor.commit()
    } catch (e: Exception) {
        Logger.e(e)
        return false
    }
}

/**
 * get String
 * @param context
 * @param file
 * @param key
 * @param defValue
 * @return
 */
fun getString(context: Context, file: String, key: String, defValue: String? = String()): String? {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return defValue
        }

        val sp = context.getSharedPreferences(file, Context.MODE_PRIVATE)
        if (!sp.contains(key)) {
            return defValue
        }

        return sp.getString(key, defValue) ?: defValue
    } catch (e: Exception) {
        Logger.e(e)
    }

    return defValue
}

/**
 * put int
 * @param context
 * @param file
 * @param key
 * @param value
 */
fun setInt(context: Context, file: String, key: String, value: Int): Boolean {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return false
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)
        return editor.commit()
    } catch (e: Exception) {
        Logger.e(e)
        return false
    }
}

/**
 * get int value
 * @param context
 * @param file
 * @param key
 * @param defValue
 * @return
 */
fun getInt(context: Context, file: String, key: String, defValue: Int = 0): Int {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return defValue
        }

        val sp = context.getSharedPreferences(file, Context.MODE_PRIVATE)
        if (!sp.contains(key)) {
            return defValue
        }

        return sp.getInt(key, defValue)
    } catch (e: Exception) {
        Logger.e(e)
    }

    return defValue
}

/**
 * put short
 * @param context
 * @param file
 * @param key
 * @param value
 */
fun setShort(context: Context, file: String, key: String, value: Short): Boolean {
    return setInt(context, file, key, value.toInt())
}

/**
 * get int value
 * @param context
 * @param file
 * @param key
 * @param defValue
 * @return
 */
fun getShort(context: Context, file: String, key: String, defValue: Short = 0): Short {
    return getInt(context, file, key, defValue.toInt()).toShort()
}

/**
 * put long
 * @param context
 * @param file
 * @param key
 * @param value
 */
fun setLong(context: Context, file: String, key: String, value: Long): Boolean {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return false
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
        return editor.putLong(key, value).commit()
    } catch (e: Exception) {
        Logger.e(e)
        return false
    }
}

/**
 * get long value
 * @param context
 * @param file
 * @param key
 * @param defValue
 * @return
 */
fun getLong(context: Context, file: String, key: String, defValue: Long = 0L): Long {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return defValue
        }

        val sp = context.getSharedPreferences(file, Context.MODE_PRIVATE)
        if (!sp.contains(key)) {
            return defValue
        }

        return sp.getLong(key, defValue)
    } catch (e: Exception) {
        Logger.e(e)
    }

    return defValue
}

/**
 * put float
 * @param context
 * @param file
 * @param key
 * @param value
 */
fun setFloat(context: Context, file: String, key: String, value: Float): Boolean {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return false
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
        return editor.putFloat(key, value).commit()
    } catch (e: Exception) {
        Logger.e(e)
        return false
    }
}

/**
 * get float value
 * @param context
 * @param file
 * @param key
 * @param defValue
 * @return
 */
fun getFloat(context: Context, file: String, key: String, defValue: Float = 0F): Float {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return defValue
        }

        val sp = context.getSharedPreferences(file, Context.MODE_PRIVATE)
        if (!sp.contains(key)) {
            return defValue
        }

        return sp.getFloat(key, defValue)
    } catch (e: Exception) {
        Logger.e(e)
    }

    return defValue
}

fun setDouble(context: Context, file: String, key: String, value: Double): Boolean {
    return setFloat(context, file, key, value.toFloat())
}

fun getDouble(context: Context, file: String, key: String, defValue: Double = 0.0): Double {
    return getFloat(context, file, key, defValue.toFloat()).toDouble()
}

/**
 * put boolean
 * @param context
 * @param file
 * @param key
 * @param value
 */
fun setBoolean(context: Context, file: String, key: String, value: Boolean): Boolean {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return false
        }

        val editor = context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
        return editor.putBoolean(key, value).commit()
    } catch (e: Exception) {
        Logger.e(e)
        return false
    }
}

/**
 * return boolean
 * @param context
 * @param file
 * @param key
 * @param defValue
 * @return
 */
fun getBoolean(context: Context, file: String, key: String, defValue: Boolean): Boolean {
    try {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(key)) {
            return defValue
        }

        val sp = context.getSharedPreferences(file, Context.MODE_PRIVATE)

        if (!sp.contains(key))
            return defValue

        return sp.getBoolean(key, defValue)
    } catch (e: Exception) {
        Logger.e(e)
    }

    return defValue
}

/**
 * 返回所有的键值对
 * @param context
 * @param fileName
 * @return
 */
fun getAll(context: Context, fileName: String): Map<String, *>? {
    val sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    return sp.all
}