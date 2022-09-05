package org.unikode.cli

import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.system.exitProcess

import org.unikode.BOM_CHAR
import org.unikode.Utf8Encoder
import org.unikode.Utf8Decoder
import org.unikode.Utf16LeEncoder
import org.unikode.Utf16LeDecoder
import org.unikode.Utf16BeEncoder
import org.unikode.Utf16BeDecoder
import org.unikode.Utf32LeEncoder
import org.unikode.Utf32LeDecoder
import org.unikode.Utf32BeEncoder
import org.unikode.Utf32BeDecoder
import org.unikode.bad.Cesu8Encoder
import org.unikode.bad.Cesu8Decoder
import org.unikode.stf7.Stf7Encoder
import org.unikode.stf7.Stf7Decoder

fun main(args: Array<String>) {

    if (args.size != 4) {
        throw IllegalArgumentException(
            "ARGUMENTS: [input-format] [input-file] [output-format] [output-file]"
        )
    }

    var passedBom = false
    val inputStream = FileInputStream(args[1])
    val outputStream = FileOutputStream(args[3])
    val encoderCallback = { byte: Byte ->
        outputStream.write(byte.toInt())
    }
    val (encoder, useBom) =
        when (args[2]) {
            "utf8" -> Pair(Utf8Encoder(encoderCallback), false)
            "utf8-bom" -> Pair(Utf8Encoder(encoderCallback), true)
            "utf16le-bom" -> Pair(Utf16LeEncoder(encoderCallback), true)
            "utf16be-bom" -> Pair(Utf16BeEncoder(encoderCallback), true)
            "utf32le-bom" -> Pair(Utf32LeEncoder(encoderCallback), true)
            "utf32be-bom" -> Pair(Utf32BeEncoder(encoderCallback), true)
            "cesu8" -> Pair(Cesu8Encoder(encoderCallback), false)
            "stf7" -> Pair(Stf7Encoder(encoderCallback), false)
            else -> throw IllegalArgumentException("Invalid output encoding specified.")
        }
    val decoderCallback = { char: Char ->
        if (passedBom) {
            encoder.input(char)
        } else {
            if (char != BOM_CHAR)
                encoder.input(char)
            passedBom = true
        }
    }
    val decoder =
        when (args[0]) {
            "utf8" -> Utf8Decoder(decoderCallback)
            "utf16le" -> Utf16LeDecoder(decoderCallback)
            "utf16be" -> Utf16BeDecoder(decoderCallback)
            "utf32le" -> Utf32LeDecoder(decoderCallback)
            "utf32be" -> Utf32BeDecoder(decoderCallback)
            "cesu8" -> Cesu8Decoder(decoderCallback)
            "stf7" -> Stf7Decoder(decoderCallback)
            else -> throw IllegalArgumentException("Invalid input encoding specififed.")
        }

    if (useBom)
        encoder.input(BOM_CHAR)
    for (byte in inputStream.readBytes())
        decoder.input(byte)

    inputStream.close()
    outputStream.close()
}
