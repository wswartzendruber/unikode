package org.unikode.cli

import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.system.exitProcess

import org.unikode.Utf8Decoder
import org.unikode.Utf8Encoder
import org.unikode.bored.Stf7Decoder
import org.unikode.bored.Stf7Encoder

fun main(args: Array<String>) {

    if (args.size != 4) {
        throw IllegalArgumentException(
            "ARGUMENTS: [input-format] [input-file] [output-format] [output-file]"
        )
    }

    val inputStream = FileInputStream(args[1])
    val outputStream = FileOutputStream(args[3])
    val encoderCallback = { byte: Byte ->
        outputStream.write(byte.toInt())
    }
    val encoder =
        when (args[2]) {
            "utf8" -> Utf8Encoder(encoderCallback)
            "stf7" -> Stf7Encoder(encoderCallback)
            else -> throw IllegalArgumentException("Invalid output encoding specified.")
        }
    val decoderCallback = { char: Char ->
        encoder.input(char)
    }
    val decoder =
        when (args[0]) {
            "utf8" -> Utf8Decoder(decoderCallback)
            "stf7" -> Stf7Decoder(decoderCallback)
            else -> throw IllegalArgumentException("Invalid input encoding specififed.")
        }

    for (byte in inputStream.readBytes())
        decoder.input(byte)

    inputStream.close()
    outputStream.close()
}
