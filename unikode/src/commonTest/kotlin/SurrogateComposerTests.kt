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

import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.REPLACEMENT_CODE
import org.unikode.SurrogateComposer

class SurrogateComposerTests {

    @Test
    fun initial_ls() {
        assertTrue(
            intArrayOf(0x20, REPLACEMENT_CODE, 0x20) contentEquals
                charArrayOf(' ', '\uDE00', ' ').composeSurrogates()
        )
    }

    @Test
    fun unfollowed_hs_then_bmp() {
        assertTrue(
            intArrayOf(0x20, REPLACEMENT_CODE, 0x20) contentEquals
                charArrayOf(' ', '\uDA00', ' ').composeSurrogates()
        )
    }

    @Test
    fun unfollowed_hs_then_smp() {
        assertTrue(
            intArrayOf(0x20, REPLACEMENT_CODE, 0x1F600) contentEquals
                charArrayOf(' ', '\uDA00', '\uD83D', '\uDE00').composeSurrogates()
        )
    }

    companion object {

        fun CharArray.composeSurrogates(): IntArray {

            val returnValue = mutableListOf<Int>()
            val composer = SurrogateComposer({ scalarValue: Int ->
                returnValue.add(scalarValue) }
            )

            for (char in this)
                composer.input(char)
            composer.flush()

            return returnValue.toIntArray()
        }
    }
}
