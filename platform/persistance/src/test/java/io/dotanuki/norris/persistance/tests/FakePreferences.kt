package io.dotanuki.norris.persistance.tests

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener

internal object FakePreferences : SharedPreferences {

    val UNSUPPORTED_OPERATION = IllegalAccessError("You are not supposed to call this method")
    var brokenMode = false

    var storage = ""

    override fun edit() = FakeEditor

    override fun getStringSet(key: String?, defValues: MutableSet<String>?) =
        throw UNSUPPORTED_OPERATION

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener?
    ) = throw UNSUPPORTED_OPERATION

    override fun registerOnSharedPreferenceChangeListener(
        listener: OnSharedPreferenceChangeListener?
    ) = throw UNSUPPORTED_OPERATION

    override fun contains(key: String?) = throw UNSUPPORTED_OPERATION
    override fun getBoolean(key: String?, defValue: Boolean) = throw UNSUPPORTED_OPERATION
    override fun getInt(key: String?, defValue: Int) = throw UNSUPPORTED_OPERATION
    override fun getAll(): MutableMap<String, *> = throw UNSUPPORTED_OPERATION
    override fun getLong(key: String?, defValue: Long) = throw UNSUPPORTED_OPERATION
    override fun getFloat(key: String?, defValue: Float) = throw UNSUPPORTED_OPERATION
    override fun getString(key: String?, defValue: String?) =
        if (!brokenMode) storage else null

    object FakeEditor : Editor {

        private var tempValues: String = ""

        override fun putString(key: String?, value: String?): Editor {
            tempValues = requireNotNull(value)
            return this
        }

        override fun commit() =
            tempValues
                .let {
                    storage = tempValues
                    true
                }

        override fun clear() = throw UNSUPPORTED_OPERATION
        override fun putLong(key: String?, value: Long) = throw UNSUPPORTED_OPERATION
        override fun putInt(key: String?, value: Int) = throw UNSUPPORTED_OPERATION
        override fun remove(key: String?): Editor = throw UNSUPPORTED_OPERATION
        override fun putBoolean(key: String?, value: Boolean) = throw UNSUPPORTED_OPERATION
        override fun putFloat(key: String?, value: Float) = throw UNSUPPORTED_OPERATION
        override fun apply() = throw UNSUPPORTED_OPERATION
        override fun putStringSet(key: String?, value: MutableSet<String>?) =
            throw UNSUPPORTED_OPERATION
    }
}
