/*
 * Copyright 2024 The Android Open Source Project
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

@file:JvmName("SavedStateReaderKt")
@file:JvmMultifileClass
@file:Suppress("NOTHING_TO_INLINE")

package androidx.savedstate

import android.os.Parcelable
import androidx.core.os.BundleCompat.getParcelable
import androidx.core.os.BundleCompat.getParcelableArrayList

@JvmInline
actual value class SavedStateReader
@PublishedApi
internal actual constructor(
    @PublishedApi internal actual val source: SavedState,
) {

    actual inline fun getBoolean(key: String): Boolean {
        if (key !in this) keyNotFoundError(key)
        return source.getBoolean(key, DEFAULT_BOOLEAN)
    }

    actual inline fun getBooleanOrElse(key: String, defaultValue: () -> Boolean): Boolean {
        if (key !in this) defaultValue()
        return source.getBoolean(key, defaultValue())
    }

    actual inline fun getChar(key: String): Char {
        if (key !in this) keyNotFoundError(key)
        return source.getChar(key, DEFAULT_CHAR)
    }

    actual inline fun getCharOrElse(key: String, defaultValue: () -> Char): Char {
        if (key !in this) defaultValue()
        return source.getChar(key, defaultValue())
    }

    actual inline fun getDouble(key: String): Double {
        if (key !in this) keyNotFoundError(key)
        return source.getDouble(key, DEFAULT_DOUBLE)
    }

    actual inline fun getDoubleOrElse(key: String, defaultValue: () -> Double): Double {
        if (key !in this) defaultValue()
        return source.getDouble(key, defaultValue())
    }

    actual inline fun getFloat(key: String): Float {
        if (key !in this) keyNotFoundError(key)
        return source.getFloat(key, DEFAULT_FLOAT)
    }

    actual inline fun getFloatOrElse(key: String, defaultValue: () -> Float): Float {
        if (key !in this) defaultValue()
        return source.getFloat(key, defaultValue())
    }

    actual inline fun getInt(key: String): Int {
        if (key !in this) keyNotFoundError(key)
        return source.getInt(key, DEFAULT_INT)
    }

    actual inline fun getIntOrElse(key: String, defaultValue: () -> Int): Int {
        if (key !in this) defaultValue()
        return source.getInt(key, defaultValue())
    }

    actual inline fun getLong(key: String): Long {
        if (key !in this) keyNotFoundError(key)
        return source.getLong(key, DEFAULT_LONG)
    }

    actual inline fun getLongOrElse(key: String, defaultValue: () -> Long): Long {
        if (key !in this) defaultValue()
        return source.getLong(key, defaultValue())
    }

    /**
     * Retrieves a [Parcelable] object associated with the specified key. Throws an
     * [IllegalStateException] if the key doesn't exist.
     *
     * @param key The key to retrieve the value for.
     * @return The [Parcelable] object associated with the key.
     * @throws IllegalStateException If the key is not found.
     */
    inline fun <reified T : Parcelable> getParcelable(key: String): T {
        if (key !in this) keyNotFoundError(key)
        return getParcelable(source, key, T::class.java) ?: valueNotFoundError(key)
    }

    /**
     * Retrieves a [Parcelable] object associated with the specified key, or a default value if the
     * key doesn't exist.
     *
     * @param key The key to retrieve the value for.
     * @param defaultValue A function providing the default [Parcelable] if the key is not found.
     * @return The [Parcelable] object associated with the key, or the default value if the key is
     *   not found.
     */
    inline fun <reified T : Parcelable> getParcelableOrElse(key: String, defaultValue: () -> T): T {
        if (key !in this) defaultValue()
        return getParcelable(source, key, T::class.java) ?: defaultValue()
    }

    actual inline fun getString(key: String): String {
        if (key !in this) keyNotFoundError(key)
        return source.getString(key) ?: valueNotFoundError(key)
    }

    actual inline fun getStringOrElse(key: String, defaultValue: () -> String): String {
        if (key !in this) defaultValue()
        return source.getString(key, defaultValue())
    }

    actual inline fun getIntList(key: String): List<Int> {
        if (key !in this) keyNotFoundError(key)
        return source.getIntegerArrayList(key) ?: valueNotFoundError(key)
    }

    actual inline fun getIntListOrElse(key: String, defaultValue: () -> List<Int>): List<Int> {
        if (key !in this) defaultValue()
        return source.getIntegerArrayList(key) ?: defaultValue()
    }

    actual inline fun getStringList(key: String): List<String> {
        if (key !in this) keyNotFoundError(key)
        return source.getStringArrayList(key) ?: valueNotFoundError(key)
    }

    actual inline fun getStringListOrElse(
        key: String,
        defaultValue: () -> List<String>
    ): List<String> {
        if (key !in this) defaultValue()
        return source.getStringArrayList(key) ?: defaultValue()
    }

    /**
     * Retrieves a [List] of elements of [Parcelable] associated with the specified [key]. Throws an
     * [IllegalStateException] if the [key] doesn't exist.
     *
     * @param key The [key] to retrieve the value for.
     * @return The [List] of elements of [Parcelable] associated with the [key].
     * @throws IllegalStateException If the [key] is not found.
     */
    inline fun <reified T : Parcelable> getParcelableList(key: String): List<T> {
        if (key !in this) keyNotFoundError(key)
        return getParcelableArrayList(source, key, T::class.java) ?: valueNotFoundError(key)
    }

    /**
     * Retrieves a [List] of elements of [Parcelable] associated with the specified [key], or a
     * default value if the [key] doesn't exist.
     *
     * @param key The [key] to retrieve the value for.
     * @param defaultValue A function providing the default value if the [key] is not found or the
     *   retrieved value is not a list of [Parcelable].
     * @return The list of elements of [Parcelable] associated with the [key], or the default value
     *   if the [key] is not found.
     */
    inline fun <reified T : Parcelable> getParcelableListOrElse(
        key: String,
        defaultValue: () -> List<T>
    ): List<T> {
        if (key !in this) defaultValue()
        return getParcelableArrayList(source, key, T::class.java) ?: defaultValue()
    }

    actual inline fun getBooleanArray(key: String): BooleanArray {
        if (key !in this) keyNotFoundError(key)
        return source.getBooleanArray(key) ?: valueNotFoundError(key)
    }

    actual inline fun getBooleanArrayOrElse(
        key: String,
        defaultValue: () -> BooleanArray
    ): BooleanArray {
        if (key !in this) defaultValue()
        return source.getBooleanArray(key) ?: defaultValue()
    }

    actual inline fun getCharArray(key: String): CharArray {
        if (key !in this) keyNotFoundError(key)
        return source.getCharArray(key) ?: valueNotFoundError(key)
    }

    actual inline fun getCharArrayOrElse(key: String, defaultValue: () -> CharArray): CharArray {
        if (key !in this) defaultValue()
        return source.getCharArray(key) ?: defaultValue()
    }

    actual inline fun getDoubleArray(key: String): DoubleArray {
        if (key !in this) keyNotFoundError(key)
        return source.getDoubleArray(key) ?: valueNotFoundError(key)
    }

    actual inline fun getDoubleArrayOrElse(
        key: String,
        defaultValue: () -> DoubleArray
    ): DoubleArray {
        if (key !in this) defaultValue()
        return source.getDoubleArray(key) ?: defaultValue()
    }

    actual inline fun getFloatArray(key: String): FloatArray {
        if (key !in this) keyNotFoundError(key)
        return source.getFloatArray(key) ?: valueNotFoundError(key)
    }

    actual inline fun getFloatArrayOrElse(key: String, defaultValue: () -> FloatArray): FloatArray {
        if (key !in this) defaultValue()
        return source.getFloatArray(key) ?: defaultValue()
    }

    actual inline fun getIntArray(key: String): IntArray {
        if (key !in this) keyNotFoundError(key)
        return source.getIntArray(key) ?: valueNotFoundError(key)
    }

    actual inline fun getIntArrayOrElse(key: String, defaultValue: () -> IntArray): IntArray {
        if (key !in this) defaultValue()
        return source.getIntArray(key) ?: defaultValue()
    }

    actual inline fun getLongArray(key: String): LongArray {
        if (key !in this) keyNotFoundError(key)
        return source.getLongArray(key) ?: valueNotFoundError(key)
    }

    actual inline fun getLongArrayOrElse(key: String, defaultValue: () -> LongArray): LongArray {
        if (key !in this) defaultValue()
        return source.getLongArray(key) ?: defaultValue()
    }

    actual inline fun getStringArray(key: String): Array<String> {
        if (key !in this) keyNotFoundError(key)
        return source.getStringArray(key) ?: valueNotFoundError(key)
    }

    actual inline fun getStringArrayOrElse(
        key: String,
        defaultValue: () -> Array<String>
    ): Array<String> {
        if (key !in this) defaultValue()
        return source.getStringArray(key) ?: defaultValue()
    }

    actual inline fun getSavedState(key: String): SavedState {
        if (key !in this) keyNotFoundError(key)
        return source.getBundle(key) ?: valueNotFoundError(key)
    }

    actual inline fun getSavedStateOrElse(key: String, defaultValue: () -> SavedState): SavedState {
        if (key !in this) defaultValue()
        return source.getBundle(key) ?: defaultValue()
    }

    actual inline fun size(): Int = source.size()

    actual inline fun isEmpty(): Boolean = source.isEmpty

    actual inline fun isNull(key: String): Boolean {
        // Using `getString` to check for `null` is unreliable as it returns null for type
        // mismatches. To reliably determine if the value is actually `null`, we use the
        // deprecated `Bundle.get`.
        @Suppress("DEPRECATION") return contains(key) && source[key] == null
    }

    actual inline operator fun contains(key: String): Boolean = source.containsKey(key)

    actual fun contentDeepEquals(other: SavedState): Boolean = source.contentDeepEquals(other)

    actual fun contentDeepHashCode(): Int = source.contentDeepHashCode()

    actual fun toMap(): Map<String, Any?> {
        return buildMap(capacity = source.size()) {
            for (key in source.keySet()) {
                @Suppress("DEPRECATION") put(key, source[key])
            }
        }
    }
}

private fun SavedState.contentDeepEquals(other: SavedState): Boolean {
    if (this === other) return true
    if (this.size() != other.size()) return false

    for (k in this.keySet()) {
        @Suppress("DEPRECATION") val v1 = this[k]
        @Suppress("DEPRECATION") val v2 = other[k]

        when {
            v1 === v2 -> continue
            v1 == v2 -> continue
            v1 == null || v2 == null -> return false

            // container types
            v1 is SavedState && v2 is SavedState -> if (!v1.contentDeepEquals(v2)) return false
            v1 is Array<*> && v2 is Array<*> -> if (!v1.contentDeepEquals(v2)) return false

            // primitive arrays
            v1 is ByteArray && v2 is ByteArray -> if (!v1.contentEquals(v2)) return false
            v1 is ShortArray && v2 is ShortArray -> if (!v1.contentEquals(v2)) return false
            v1 is IntArray && v2 is IntArray -> if (!v1.contentEquals(v2)) return false
            v1 is LongArray && v2 is LongArray -> if (!v1.contentEquals(v2)) return false
            v1 is FloatArray && v2 is FloatArray -> if (!v1.contentEquals(v2)) return false
            v1 is DoubleArray && v2 is DoubleArray -> if (!v1.contentEquals(v2)) return false
            v1 is CharArray && v2 is CharArray -> if (!v1.contentEquals(v2)) return false
            v1 is BooleanArray && v2 is BooleanArray -> if (!v1.contentEquals(v2)) return false

            // if nothing else works
            else -> if (v1 != v2) return false
        }
    }
    return true
}

private fun SavedState.contentDeepHashCode(): Int {
    var result = 1

    for (k in this.keySet()) {
        val elementHash =
            when (@Suppress("DEPRECATION") val element = this[k]) {
                // container types
                is SavedState -> element.contentDeepHashCode()
                is Array<*> -> element.contentDeepHashCode()

                // primitive arrays
                is ByteArray -> element.contentHashCode()
                is ShortArray -> element.contentHashCode()
                is IntArray -> element.contentHashCode()
                is LongArray -> element.contentHashCode()
                is FloatArray -> element.contentHashCode()
                is DoubleArray -> element.contentHashCode()
                is CharArray -> element.contentHashCode()
                is BooleanArray -> element.contentHashCode()

                // if nothing else works
                else -> element.hashCode()
            }
        result = 31 * result + elementHash
    }

    return result
}
