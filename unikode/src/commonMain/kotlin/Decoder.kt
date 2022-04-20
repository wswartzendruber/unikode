package org.unikode

public abstract class Decoder {

    public abstract fun decode(
        destination: CharArray,
        destinationOffset: Int = 0,
        source: ByteArray,
        sourceStartIndex: Int = 0,
        sourceEndIndex: Int = source.size,
    ): Int

    public abstract fun maxCharsNeeded(byteCount: Int): Int

    public abstract fun reset(): Unit
}
