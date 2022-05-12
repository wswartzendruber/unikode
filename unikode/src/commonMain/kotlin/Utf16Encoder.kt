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

public abstract class Utf16Encoder : Encoder() {

    public override fun maxBytesNeeded(charCount: Int): Int = charCount * 2

    public override fun maxCharsPossible(byteCount: Int): Int = byteCount / 2

    protected fun Int.highSurrogate(): Int = ((this - 0x10000) ushr 10) + 0xD800

    protected fun Int.lowSurrogate(): Int = ((this - 0x10000) and 0x3FF) + 0xDC00
}
