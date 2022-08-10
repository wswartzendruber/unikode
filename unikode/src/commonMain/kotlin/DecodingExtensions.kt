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

public fun ByteArray.toStringUtf8(): String = this.asIterable().toStringUtf8()

public fun ByteArray.toStringUtf16Le(): String = this.asIterable().toStringUtf16Le()

public fun ByteArray.toStringUtf16Be(): String = this.asIterable().toStringUtf16Be()

public fun ByteArray.toStringUtf32Le(): String = this.asIterable().toStringUtf32Le()

public fun ByteArray.toStringUtf32Be(): String = this.asIterable().toStringUtf32Be()

public fun Iterable<Byte>.toStringUtf8(): String {

    val builder = StringBuilder()
    val decoder = Utf8Decoder({ value: Char -> builder.append(value) })

    for (byte in this)
        decoder.input(byte)
    decoder.flush()

    return builder.toString()
}

public fun Iterable<Byte>.toStringUtf16Le(): String {

    val builder = StringBuilder()
    val decoder = Utf16LeDecoder({ value: Char -> builder.append(value) })

    for (byte in this)
        decoder.input(byte)
    decoder.flush()

    return builder.toString()
}

public fun Iterable<Byte>.toStringUtf16Be(): String {

    val builder = StringBuilder()
    val decoder = Utf16BeDecoder({ value: Char -> builder.append(value) })

    for (byte in this)
        decoder.input(byte)
    decoder.flush()

    return builder.toString()
}

public fun Iterable<Byte>.toStringUtf32Le(): String {

    val builder = StringBuilder()
    val decoder = Utf32LeDecoder({ value: Char -> builder.append(value) })

    for (byte in this)
        decoder.input(byte)
    decoder.flush()

    return builder.toString()
}

public fun Iterable<Byte>.toStringUtf32Be(): String {

    val builder = StringBuilder()
    val decoder = Utf32BeDecoder({ value: Char -> builder.append(value) })

    for (byte in this)
        decoder.input(byte)
    decoder.flush()

    return builder.toString()
}
