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

import org.unikode.REPLACEMENT_CHAR
import org.unikode.Decoder
import org.unikode.SurrogateValidator

public class Cf8Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateValidator = SurrogateValidator(callback)
    private var continuing = false
    private var currentScalarValue = 0
    private var bytesExpected = 0
    private var byteIndex = 0
    private var minimumScalarValue = 0

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun input(value: Byte): Unit {

        val valueInt = value.toInt() and 0xFF

        if (!continuing) {
            when {
                valueInt < 0xA0 -> {
                    callback(valueInt.toChar())
                }
                valueInt < 0xE0 -> {
                    callback(REPLACEMENT_CHAR)
                }
                valueInt < 0xF0 -> {
                    continuing = true
                    currentScalarValue = valueInt and 0xF
                    bytesExpected = 2
                    byteIndex = 1
                    minimumScalarValue = 0x00A0
                }
                else -> {
                    continuing = true
                    currentScalarValue = valueInt and 0xF
                    bytesExpected = 3
                    byteIndex = 1
                    minimumScalarValue = 0x0400
                }
            }
        } else {
            when {
                valueInt < 0xA0 -> {
                    callback(REPLACEMENT_CHAR)
                    reset()
                }
                valueInt < 0xE0 -> {
                    currentScalarValue = currentScalarValue shl 6
                    when (bytesExpected) {
                        2 -> {
                            when (++byteIndex)
                            {
                                2 -> {
                                    currentScalarValue = currentScalarValue or valueInt - 0xA0
                                    surrogateValidator.input(currentScalarValue)
                                    end()
                                }
                                else -> {
                                    throw IllegalStateException()
                                }
                            }
                        }
                        3 -> {
                            when (++byteIndex)
                            {
                                2 -> {
                                    currentScalarValue = currentScalarValue or valueInt - 0xA0
                                }
                                3 -> {
                                    currentScalarValue = currentScalarValue or valueInt - 0xA0
                                    surrogateValidator.input(currentScalarValue)
                                    end()
                                }
                                else -> {
                                    throw IllegalStateException()
                                }
                            }
                        }
                        else -> {
                            throw IllegalStateException()
                        }
                    }
                }
                else -> {
                    callback(REPLACEMENT_CHAR)
                    reset()
                }
            }
        }
    }

    public override fun flush(): Unit {
        if (continuing == true)
            callback(REPLACEMENT_CHAR)
        reset()
    }

    public override fun reset(): Unit {
        surrogateValidator.reset()
        end()
    }

    private fun end() {
        continuing = false
        currentScalarValue = 0
        bytesExpected = 0
        byteIndex = 0
        minimumScalarValue = 0
    }
}
