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

package org.unikode.bad

import org.unikode.REPLACEMENT_CHAR
import org.unikode.Decoder
import org.unikode.SurrogateValidator
import org.unikode.ThompsonDecoder

public class Cesu8Decoder(callback: (Char) -> Unit) : Decoder(callback) {

    private val surrogateValidator = SurrogateValidator(callback)
    private val thompsonDecoder = ThompsonDecoder({ codeUnit: Int ->
        surrogateValidator.input(
            if (codeUnit < 0x10000)
                codeUnit.toChar()
            else
                REPLACEMENT_CHAR
        )
    })

    public override fun maxCharsNeeded(byteCount: Int): Int = byteCount

    public override fun input(value: Byte): Unit {
        thompsonDecoder.input(value)
    }

    public override fun flush(): Unit {
        thompsonDecoder.flush()
        surrogateValidator.flush()
    }

    public override fun reset(): Unit {
        thompsonDecoder.reset()
        surrogateValidator.reset()
    }
}
