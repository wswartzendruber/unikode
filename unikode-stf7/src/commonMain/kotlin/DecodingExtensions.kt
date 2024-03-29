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

package org.unikode.stf7

public fun ByteArray.toStringStf7(): String = this.asIterable().toStringStf7()

public fun Iterable<Byte>.toStringStf7(): String {

    val builder = StringBuilder()
    val decoder = Stf7Decoder({ value: Char -> builder.append(value) })

    for (byte in this)
        decoder.input(byte)
    decoder.flush()

    return builder.toString()
}
