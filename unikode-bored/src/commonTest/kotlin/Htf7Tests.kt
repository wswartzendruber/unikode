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

package org.unikode.bored.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.Test

import org.unikode.REPLACEMENT_CHAR
import org.unikode.REPLACEMENT_CHAR
import org.unikode.bored.toHtf7ByteArray
import org.unikode.bored.toStringHtf7
import org.unikode.bored.Htf7Encoder
import org.unikode.bored.Htf7Decoder

class Htf7Tests {

    @Test
    fun round_trip() =
        assertEquals(completeString, completeString.toHtf7ByteArray().toStringHtf7())

    @Test
    fun reject_overlongs_two_bytes() {

        val range = 0x0..0x39
        val buffer = ByteArray(2)
        var count = 0
        val decoder = Htf7Decoder({ value: Char ->
            assertEquals(REPLACEMENT_CHAR, value)
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
    fun reject_overlongs_four_bytes() {

        val range = 0x0..0xFF step 10
        val buffer = ByteArray(4)
        var count = 0
        val decoder = Htf7Decoder({ value: Char ->
            assertEquals(REPLACEMENT_CHAR, value)
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
    fun reject_overlongs_six_bytes() {

        val range = 0x0..0xFFFF step 100
        val buffer = ByteArray(6)
        var count = 0
        val decoder = Htf7Decoder({ value: Char ->
            assertEquals(REPLACEMENT_CHAR, value)
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

        val input = byteArrayOf(0x70)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_2() {

        val input = byteArrayOf(0x40, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_4() {

        val input = byteArrayOf(0x50, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_2_of_4() {

        val input = byteArrayOf(0x50, 0x70, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_3_of_4() {

        val input = byteArrayOf(0x50, 0x70, 0x70, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_1_of_6() {

        val input = byteArrayOf(0x60, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_2_of_6() {

        val input = byteArrayOf(0x60, 0x70, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_3_of_6() {

        val input = byteArrayOf(0x60, 0x70, 0x70, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_4_of_6() {

        val input = byteArrayOf(0x60, 0x70, 0x70, 0x70, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun reject_incomplete_sequence_5_of_6() {

        val input = byteArrayOf(0x60, 0x70, 0x70, 0x70, 0x70, 0x20)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR, ' ') contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_2() {

        val input = byteArrayOf(0x40)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_4() {

        val input = byteArrayOf(0x50)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_2_of_4() {

        val input = byteArrayOf(0x50, 0x70)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_3_of_4() {

        val input = byteArrayOf(0x50, 0x70, 0x70)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_1_of_6() {

        val input = byteArrayOf(0x60)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_2_of_6() {

        val input = byteArrayOf(0x60, 0x70)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_3_of_6() {

        val input = byteArrayOf(0x60, 0x70, 0x70)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_4_of_6() {

        val input = byteArrayOf(0x60, 0x70, 0x70, 0x70)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
    }

    @Test
    fun flush_incomplete_sequence_5_of_6() {

        val input = byteArrayOf(0x60, 0x70, 0x70, 0x70, 0x70)
        val output = input.toCharArrayHtf7()

        assertTrue(charArrayOf(REPLACEMENT_CHAR) contentEquals output)
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
            destination[0] = (0x40 or (value ushr 4)).toByte()
            destination[1] = (0x70 or (value and 0xF)).toByte()
        }

        fun fourByteValue(destination: ByteArray, value: Int) {
            destination[0] = (0x50 or (value ushr 12)).toByte()
            destination[1] = (0x70 or (value ushr 8 and 0xF)).toByte()
            destination[2] = (0x70 or (value ushr 4 and 0xF)).toByte()
            destination[3] = (0x70 or (value and 0xF)).toByte()
        }

        fun sixByteValue(destination: ByteArray, value: Int) {
            destination[0] = (0x60 or (value ushr 20)).toByte()
            destination[1] = (0x70 or (value ushr 16 and 0xF)).toByte()
            destination[2] = (0x70 or (value ushr 12 and 0xF)).toByte()
            destination[3] = (0x70 or (value ushr 8 and 0xF)).toByte()
            destination[4] = (0x70 or (value ushr 4 and 0xF)).toByte()
            destination[5] = (0x70 or (value and 0xF)).toByte()
        }

        fun ByteArray.toCharArrayHtf7(): CharArray {

            val returnValue = mutableListOf<Char>()
            val decoder = Htf7Decoder({ value: Char -> returnValue.add(value) })

            for (byte in this)
                decoder.input(byte)
            decoder.flush()

            return returnValue.toCharArray()
        }
    }
}
