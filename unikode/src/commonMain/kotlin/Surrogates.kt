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

public fun codePoint(highSurrogate: Char, lowSurrogate: Char): Int =
    codePoint(highSurrogate.code, lowSurrogate.code)

public fun codePoint(highSurrogate: Int, lowSurrogate: Int): Int =
    (((highSurrogate - 0xD800) shl 10) or (lowSurrogate - 0xDC00)) + 0x10000

public fun Int.highSurrogate(): Char = (((this - 0x10000) ushr 10) + 0xD800).toChar()

public fun Int.lowSurrogate(): Char = (((this - 0x10000) and 0x3FF) + 0xDC00).toChar()
