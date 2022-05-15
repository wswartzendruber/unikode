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

public fun ByteArray.toStringUtf8(): String = this.toString(Utf8Decoder())

public fun Iterable<Byte>.toStringUtf8(): String = this.toString(Utf8Decoder())

private fun ByteArray.toString(decoder: Decoder): String {

    val builder = StringBuilder()
    val writeNextChar: (Char) -> Unit = { value: Char ->
        builder.append(value)
    }

    for (byte in this)
        decoder.inputByte(byte, writeNextChar)

    return builder.toString()
}

private fun Iterable<Byte>.toString(decoder: Decoder): String {

    val builder = StringBuilder()
    val writeNextChar: (Char) -> Unit = { value: Char ->
        builder.append(value)
    }

    for (byte in this)
        decoder.inputByte(byte, writeNextChar)

    return builder.toString()
}
