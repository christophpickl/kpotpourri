package com.github.christophpickl.kpotpourri.test4k


/**
 * e.message != null && e.message!!.contains("200") && e.message!!.contains("4xx")
 *    ==> BECOMES ==>
 * e.messageContains("200", "4xx")
 */
// TODO TEST this
fun Exception.messageContains(vararg substring: String): Boolean {
    val msg = message ?: return false

    return substring.all { sub -> msg.contains(sub) }
}
