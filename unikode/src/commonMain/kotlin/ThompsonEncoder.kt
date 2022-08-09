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

public class ThompsonEncoder(private val callback: (Byte) -> Unit) {

    public fun input(value: Int): Unit {
        when {
            value < 0x80 -> {
                callback(value.toByte())
            }
            value < 0x800 -> {
                callback((0xC0 or (value ushr 6)).toByte())
                callback((0x80 or (value and 0x3F)).toByte())
            }
            value < 0x10000 -> {
                callback((0xE0 or (value ushr 12)).toByte())
                callback((0x80 or (value ushr 6 and 0x3F)).toByte())
                callback((0x80 or (value and 0x3F)).toByte())
            }
            value < 0x200000 -> {
                callback((0xF0 or (value ushr 18)).toByte())
                callback((0x80 or (value ushr 12 and 0x3F)).toByte())
                callback((0x80 or (value ushr 6 and 0x3F)).toByte())
                callback((0x80 or (value and 0x3F)).toByte())
            }
            value < 0x4000000 -> {
                callback((0xF0 or (value ushr 24)).toByte())
                callback((0x80 or (value ushr 18 and 0x3F)).toByte())
                callback((0x80 or (value ushr 12 and 0x3F)).toByte())
                callback((0x80 or (value ushr 6 and 0x3F)).toByte())
                callback((0x80 or (value and 0x3F)).toByte())
            }
            value <= 0x7FFFFFFF -> {
                callback((0xF0 or (value ushr 30)).toByte())
                callback((0x80 or (value ushr 24 and 0x3F)).toByte())
                callback((0x80 or (value ushr 18 and 0x3F)).toByte())
                callback((0x80 or (value ushr 12 and 0x3F)).toByte())
                callback((0x80 or (value ushr 6 and 0x3F)).toByte())
                callback((0x80 or (value and 0x3F)).toByte())
            }
            else -> {
                throw IllegalArgumentException("Value too large for Thompson encoding.")
            }
        }
    }
}
