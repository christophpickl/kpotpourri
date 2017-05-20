package com.github.christophpickl.kpotpourri.jackson4k.non_test

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kMapper

fun main(args: Array<String>) {

    buildJackson4kMapper {
        failOnUnknownProperties = true

        indentOutput = true
        orderMapEntries = true
        renderNulls = true
        visibilities += PropertyAccessor.ALL to JsonAutoDetect.Visibility.ANY
    }

}
