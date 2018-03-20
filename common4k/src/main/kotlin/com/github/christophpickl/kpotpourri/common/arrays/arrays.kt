package com.github.christophpickl.kpotpourri.common.arrays

/**
 * Especially needed in data classes with properties of type [ByteArray].
 */
fun ByteArray?.byteArrayEquals(that: ByteArray?): Boolean =
    if (this != null && that != null) this.contentEquals(that)
    else this == null && that == null
