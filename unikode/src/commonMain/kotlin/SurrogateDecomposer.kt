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

public class SurrogateDecomposer(private val callback: (Char) -> Unit) {

    public fun input(value: Int): Unit =
        when (value) {
            in bmpRange1, in bmpRange2 -> {
                callback(value.toChar())
            }
            in extRange -> {
                callback(value.highSurrogate())
                callback(value.lowSurrogate())
            }
            else -> {
                callback(REPLACEMENT_CHAR)
            }
        }

    private companion object {

        private val bmpRange1 = 0x0000..0xD7FF
        private val bmpRange2 = 0xE000..0xFFFF
        private val extRange = 0x010000..0x10FFFF
    }
}
