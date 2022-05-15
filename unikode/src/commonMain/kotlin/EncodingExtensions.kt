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

public fun CharSequence.toUtf8ByteArray(): ByteArray = this.toByteArray(Utf8Encoder())

public fun CharArray.toUtf8ByteArray(): ByteArray = this.toByteArray(Utf8Encoder())

public fun Iterable<Char>.toUtf8ByteArray(): ByteArray = this.toByteArray(Utf8Encoder())

private fun CharSequence.toByteArray(encoder: Encoder): ByteArray {

    val bytes = mutableListOf<Byte>()
    val writeNextByte: (Byte) -> Unit = { value: Byte ->
        bytes.add(value)
    }

    for (char in this)
        encoder.inputChar(char, writeNextByte)

    return bytes.toByteArray()
}

private fun CharArray.toByteArray(encoder: Encoder): ByteArray {

    val bytes = mutableListOf<Byte>()
    val writeNextByte: (Byte) -> Unit = { value: Byte ->
        bytes.add(value)
    }

    for (char in this)
        encoder.inputChar(char, writeNextByte)

    return bytes.toByteArray()
}

private fun Iterable<Char>.toByteArray(encoder: Encoder): ByteArray {

    val bytes = mutableListOf<Byte>()
    val writeNextByte: (Byte) -> Unit = { value: Byte ->
        bytes.add(value)
    }

    for (char in this)
        encoder.inputChar(char, writeNextByte)

    return bytes.toByteArray()
}
