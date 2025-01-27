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

@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package androidx.compose.runtime.snapshots

expect class SnapshotId

expect val SnapshotIdZero: SnapshotId
expect val SnapshotIdMax: SnapshotId
expect val SnapshotIdInvalidValue: SnapshotId

expect val SnapshotIdSize: Int

expect operator fun SnapshotId.compareTo(other: SnapshotId): Int

expect operator fun SnapshotId.compareTo(other: Int): Int

expect operator fun SnapshotId.plus(other: Int): SnapshotId

expect operator fun SnapshotId.minus(other: SnapshotId): SnapshotId

expect operator fun SnapshotId.minus(other: Int): SnapshotId

expect operator fun SnapshotId.div(other: Int): SnapshotId

expect operator fun SnapshotId.times(other: Int): SnapshotId

expect fun SnapshotId.toInt(): Int

expect class SnapshotIdArray

internal expect fun snapshotIdArrayWithCapacity(capacity: Int): SnapshotIdArray

internal expect fun snapshotIdArrayOf(id: SnapshotId): SnapshotIdArray

internal expect operator fun SnapshotIdArray.get(index: Int): SnapshotId

internal expect operator fun SnapshotIdArray.set(index: Int, value: SnapshotId)

internal expect val SnapshotIdArray.size: Int

internal expect fun SnapshotIdArray.copyInto(other: SnapshotIdArray)

internal expect fun SnapshotIdArray.first(): SnapshotId

internal expect inline fun SnapshotIdArray.forEach(block: (SnapshotId) -> Unit)

internal expect fun SnapshotIdArray.binarySearch(id: SnapshotId): Int

internal expect fun SnapshotIdArray.withIdInsertedAt(index: Int, id: SnapshotId): SnapshotIdArray

internal expect fun SnapshotIdArray.withIdRemovedAt(index: Int): SnapshotIdArray?

internal expect class SnapshotIdArrayBuilder(array: SnapshotIdArray?) {
    fun add(id: SnapshotId)

    fun toArray(): SnapshotIdArray?
}

internal expect fun Int.toSnapshotId(): SnapshotId
