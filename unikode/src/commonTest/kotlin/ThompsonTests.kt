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
import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.REPLACEMENT_CODE
import org.unikode.ThompsonEncoder
import org.unikode.ThompsonDecoder

class ThompsonTests {

    @Test
    fun round_trip_tests() {

        var index = 0
        val decoder = ThompsonDecoder({ value: Int ->
            assertEquals(scalarValues[index++], value)
        })
        val encoder = ThompsonEncoder({ byte: Byte ->
            decoder.input(byte)
        })

        for (value in scalarValues)
            encoder.input(value)
    }

    @Test
    fun reject_overlongs_two_bytes() {

        val range = 0x0..0x7F
        val buffer = ByteArray(2)
        var count = 0
        val decoder = ThompsonDecoder({ value: Int ->
            assertEquals(REPLACEMENT_CODE, value)
        })

        for (value in range) {
            twoByteValue(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
            count++
        }

        assertEquals(range.count(), count)
    }

    @Test
    fun reject_overlongs_three_bytes() {

        val range = 0x0..0x7FF step 10
        val buffer = ByteArray(3)
        var count = 0
        val decoder = ThompsonDecoder({ value: Int ->
            assertEquals(REPLACEMENT_CODE, value)
        })

        for (value in range) {
            threeByteValue(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
            decoder.input(buffer[2])
            count++
        }

        assertEquals(range.count(), count)
    }

    @Test
    fun reject_overlongs_four_bytes() {

        val range = 0x0..0xFFFF step 100
        val buffer = ByteArray(4)
        var count = 0
        val decoder = ThompsonDecoder({ value: Int ->
            assertEquals(REPLACEMENT_CODE, value)
        })

        for (value in range) {
            fourByteValue(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
            decoder.input(buffer[2])
            decoder.input(buffer[3])
            count++
        }

        assertEquals(range.count(), count)
    }

    @Test
    fun reject_overlongs_five_bytes() {

        val range = 0x0..0x1FFFFF step 1_000
        val buffer = ByteArray(5)
        var count = 0
        val decoder = ThompsonDecoder({ value: Int ->
            assertEquals(REPLACEMENT_CODE, value)
        })

        for (value in range) {
            fiveByteValue(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
            decoder.input(buffer[2])
            decoder.input(buffer[3])
            decoder.input(buffer[4])
            count++
        }

        assertEquals(range.count(), count)
    }

    @Test
    fun reject_overlongs_six_bytes() {

        val range = 0x0..0x3FFFFFF step 10_000
        val buffer = ByteArray(6)
        var count = 0
        val decoder = ThompsonDecoder({ value: Int ->
            assertEquals(REPLACEMENT_CODE, value)
        })

        for (value in range) {
            sixByteValue(buffer, value)
            decoder.input(buffer[0])
            decoder.input(buffer[1])
            decoder.input(buffer[2])
            decoder.input(buffer[3])
            decoder.input(buffer[4])
            decoder.input(buffer[5])
            count++
        }

        assertEquals(range.count(), count)
    }

    @Test
    fun reject_initial_continuation_byte() {

        val input = byteArrayOf(-0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_2() {

        val input = byteArrayOf(-0x40, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_3() {

        val input = byteArrayOf(-0x20, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_2_of_3() {

        val input = byteArrayOf(-0x20, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_4() {

        val input = byteArrayOf(-0x10, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_2_of_4() {

        val input = byteArrayOf(-0x10, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_3_of_4() {

        val input = byteArrayOf(-0x10, -0x80, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_5() {

        val input = byteArrayOf(-0x08, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_2_of_5() {

        val input = byteArrayOf(-0x08, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_3_of_5() {

        val input = byteArrayOf(-0x08, -0x80, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_4_of_5() {

        val input = byteArrayOf(-0x08, -0x80, -0x80, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_6() {

        val input = byteArrayOf(-0x04, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_2_of_6() {

        val input = byteArrayOf(-0x04, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_3_of_6() {

        val input = byteArrayOf(-0x04, -0x80, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_4_of_6() {

        val input = byteArrayOf(-0x04, -0x80, -0x80, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_5_of_6() {

        val input = byteArrayOf(-0x04, -0x80, -0x80, -0x80, -0x80, 0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE, 0x20) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_2() {

        val input = byteArrayOf(-0x40)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_3() {

        val input = byteArrayOf(-0x20)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_2_of_3() {

        val input = byteArrayOf(-0x20, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_4() {

        val input = byteArrayOf(-0x10)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_2_of_4() {

        val input = byteArrayOf(-0x10, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_3_of_4() {

        val input = byteArrayOf(-0x10, -0x80, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_5() {

        val input = byteArrayOf(-0x08)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_2_of_5() {

        val input = byteArrayOf(-0x08, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_3_of_5() {

        val input = byteArrayOf(-0x08, -0x80, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_4_of_5() {

        val input = byteArrayOf(-0x08, -0x80, -0x80, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_6() {

        val input = byteArrayOf(-0x04)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_2_of_6() {

        val input = byteArrayOf(-0x04, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_3_of_6() {

        val input = byteArrayOf(-0x04, -0x80, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_4_of_6() {

        val input = byteArrayOf(-0x04, -0x80, -0x80, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_5_of_6() {

        val input = byteArrayOf(-0x04, -0x80, -0x80, -0x80, -0x80)
        val output = input.toIntArrayThompson()

        assertTrue(intArrayOf(REPLACEMENT_CODE) contentEquals output)
    }

    companion object {

        val scalarValues = buildList {
            for (value in 0x0..0x7F)
                add(value)
            add(0x7F)
            for (value in 0x80..0x7FF step 10)
                add(value)
            add(0x7FF)
            for (value in 0x800..0xFFFF step 100)
                add(value)
            add(0xFFFF)
            for (value in 0x10000..0x1FFFFF step 1_000)
                add(value)
            add(0x1FFFFF)
            for (value in 0x200000..0x3FFFFFF step 10_000)
                add(value)
            add(0x3FFFFFF)
            for (value in 0x4000000..0x7FFFFFFF step 100_000)
                add(value)
            add(0x7FFFFFFF)
        }

        fun twoByteValue(destination: ByteArray, value: Int) {
            destination[0] = (0xC0 or (value ushr 6)).toByte()
            destination[1] = (0x80 or (value and 0x3F)).toByte()
        }

        fun threeByteValue(destination: ByteArray, value: Int) {
            destination[0] = (0xE0 or (value ushr 12)).toByte()
            destination[1] = (0x80 or (value ushr 6 and 0x3F)).toByte()
            destination[2] = (0x80 or (value and 0x3F)).toByte()
        }

        fun fourByteValue(destination: ByteArray, value: Int) {
            destination[0] = (0xF0 or (value ushr 18)).toByte()
            destination[1] = (0x80 or (value ushr 12 and 0x3F)).toByte()
            destination[2] = (0x80 or (value ushr 6 and 0x3F)).toByte()
            destination[3] = (0x80 or (value and 0x3F)).toByte()
        }

        fun fiveByteValue(destination: ByteArray, value: Int) {
            destination[0] = (0xF8 or (value ushr 24)).toByte()
            destination[1] = (0x80 or (value ushr 18 and 0x3F)).toByte()
            destination[2] = (0x80 or (value ushr 12 and 0x3F)).toByte()
            destination[3] = (0x80 or (value ushr 6 and 0x3F)).toByte()
            destination[4] = (0x80 or (value and 0x3F)).toByte()
        }

        fun sixByteValue(destination: ByteArray, value: Int) {
            destination[0] = (0xFC or (value ushr 30)).toByte()
            destination[1] = (0x80 or (value ushr 24 and 0x3F)).toByte()
            destination[2] = (0x80 or (value ushr 18 and 0x3F)).toByte()
            destination[3] = (0x80 or (value ushr 12 and 0x3F)).toByte()
            destination[4] = (0x80 or (value ushr 6 and 0x3F)).toByte()
            destination[5] = (0x80 or (value and 0x3F)).toByte()
        }

        fun ByteArray.toIntArrayThompson(): IntArray {

            val returnValue = mutableListOf<Int>()
            val decoder = ThompsonDecoder({ value: Int -> returnValue.add(value) })

            for (byte in this)
                decoder.input(byte)
            decoder.flush()

            return returnValue.toIntArray()
        }
    }
}
