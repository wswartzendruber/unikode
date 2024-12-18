/*
 * Copyright 2024 William Swartzendruber
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

package org.unikode.cf8

import org.unikode.Encoder

public fun CharSequence.toCf8ByteArray(): ByteArray = this.asIterable().toCf8ByteArray()

public fun CharArray.toCf8ByteArray(): ByteArray = this.asIterable().toCf8ByteArray()

public fun Iterable<Char>.toCf8ByteArray(): ByteArray {

    val bytes = mutableListOf<Byte>()
    val encoder = Cf8Encoder({ byte -> bytes.add(byte) })

    for (char in this)
        encoder.input(char)
    encoder.flush()

    return bytes.toByteArray()
}
