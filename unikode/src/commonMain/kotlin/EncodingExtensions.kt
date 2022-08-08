/*
 * Copyright 2022 William Swartzendruber
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.unikode

public fun CharSequence.toUtf8ByteArray(): ByteArray = this.asIterable().toUtf8ByteArray()

public fun CharSequence.toUtf16LeByteArray(): ByteArray = this.asIterable().toUtf16LeByteArray()

public fun CharSequence.toUtf16BeByteArray(): ByteArray = this.asIterable().toUtf16BeByteArray()

public fun CharSequence.toUtf32LeByteArray(): ByteArray = this.asIterable().toUtf32LeByteArray()

public fun CharSequence.toUtf32BeByteArray(): ByteArray = this.asIterable().toUtf32BeByteArray()

public fun CharArray.toUtf8ByteArray(): ByteArray = this.asIterable().toUtf8ByteArray()

public fun CharArray.toUtf16LeByteArray(): ByteArray = this.asIterable().toUtf16LeByteArray()

public fun CharArray.toUtf16BeByteArray(): ByteArray = this.asIterable().toUtf16BeByteArray()

public fun CharArray.toUtf32LeByteArray(): ByteArray = this.asIterable().toUtf32LeByteArray()

public fun CharArray.toUtf32BeByteArray(): ByteArray = this.asIterable().toUtf32BeByteArray()

public fun Iterable<Char>.toUtf8ByteArray(): ByteArray {

    val bytes = mutableListOf<Byte>()
    val encoder = Utf8Encoder({ byte -> bytes.add(byte) })

    for (char in this)
        encoder.input(char)

    return bytes.toByteArray()
}

public fun Iterable<Char>.toUtf16LeByteArray(): ByteArray {

    val bytes = mutableListOf<Byte>()
    val encoder = Utf16LeEncoder({ byte -> bytes.add(byte) })

    for (char in this)
        encoder.input(char)

    return bytes.toByteArray()
}

public fun Iterable<Char>.toUtf16BeByteArray(): ByteArray {

    val bytes = mutableListOf<Byte>()
    val encoder = Utf16BeEncoder({ byte -> bytes.add(byte) })

    for (char in this)
        encoder.input(char)

    return bytes.toByteArray()
}

public fun Iterable<Char>.toUtf32LeByteArray(): ByteArray {

    val bytes = mutableListOf<Byte>()
    val encoder = Utf32LeEncoder({ byte -> bytes.add(byte) })

    for (char in this)
        encoder.input(char)

    return bytes.toByteArray()
}

public fun Iterable<Char>.toUtf32BeByteArray(): ByteArray {

    val bytes = mutableListOf<Byte>()
    val encoder = Utf32BeEncoder({ byte -> bytes.add(byte) })

    for (char in this)
        encoder.input(char)

    return bytes.toByteArray()
}
