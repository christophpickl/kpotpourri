package com.github.christophpickl.kpotpourri.http4k_fuel

import com.github.christophpickl.kpotpourri.http4k.internal.HttpClientType
import com.github.christophpickl.kpotpourri.http4k.non_test.AbstractHttpClientFactoryDetectorTest
import kotlin.reflect.KClass

class FuelHttpClientFactoryDetectorTest : AbstractHttpClientFactoryDetectorTest<FuelHttpClient>() {
    override val expectedType: KClass<FuelHttpClient> get() = FuelHttpClient::class
    override val httpClientEnum get() = HttpClientType.FuelClient
}

//class FuelHttpClientTest : WiremockTest() { }
