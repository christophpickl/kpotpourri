package com.github.christophpickl.kpotpourri.jackson4k.non_test

import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper

fun main(args: Array<String>) {

    buildJackson4kMapper {
        failOnUnknownProperties = true

        indentOutput = true
        orderMapEntries = true
        renderNulls = true
    }

}
