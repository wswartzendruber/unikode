/*
 * Copyright 2022 William Swartzendruber
 *
 * To the extent possible under law, the person who associated CC0 with this file has waived all
 * copyright and related or neighboring rights to this file.
 *
 * You should have received a copy of the CC0 legalcode along with this work. If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 *
 * SPDX-License-Identifier: CC0-1.0
 */

package org.unikode.test

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.isSurrogate
import org.unikode.isHighSurrogate
import org.unikode.isLowSurrogate
import org.unikode.highSurrogate
import org.unikode.lowSurrogate
import org.unikode.scalarValue

class SurrogateTests {

    @Test
    fun `all_supplementary_scalar_values_round_trip`() {
        for (i in 0x010000..0x10FFFF)
            assertEquals(i, scalarValue(i.highSurrogate(), i.lowSurrogate()))
    }

    @Test
    fun `all_bmp_surrogate_detection`() {
        for (i in 0x0000..0xD7FF) {
            assertFalse(i.isSurrogate())
            assertFalse(i.isHighSurrogate())
            assertFalse(i.isLowSurrogate())
        }
        for (i in 0xD800..0xDBFF) {
            assertTrue(i.isSurrogate())
            assertTrue(i.isHighSurrogate())
            assertFalse(i.isLowSurrogate())
        }
        for (i in 0xDC00..0xDFFF) {
            assertTrue(i.isSurrogate())
            assertFalse(i.isHighSurrogate())
            assertTrue(i.isLowSurrogate())
        }
        for (i in 0xE000..0xFFFF) {
            assertFalse(i.isSurrogate())
            assertFalse(i.isHighSurrogate())
            assertFalse(i.isLowSurrogate())
        }
    }
}
