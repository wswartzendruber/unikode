package org.unikode

public abstract class Encoder {

    public abstract fun encode(
        destination: ByteArray,
        destinationOffset: Int = 0,
        source: CharSequence,
        sourceStartIndex: Int = 0,
        sourceEndIndex: Int = source.length,
    ): Int

    public abstract fun maxBytesNeeded(charCount: Int): Int

    public abstract fun reset(): Unit
}
