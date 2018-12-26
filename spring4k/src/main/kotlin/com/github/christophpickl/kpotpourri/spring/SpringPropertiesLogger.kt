package com.github.christophpickl.kpotpourri.spring

import mu.KotlinLogging
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Environment
import org.springframework.core.env.MapPropertySource

class SpringPropertiesLogger(
    private val springEnvironment: Environment
) {

    companion object {
        // sample to express "not contains term" in regexp: "^((?!term).)*\$"
        val defaultExclusions = listOf(Regex(".*([Pp]assword).*"), Regex(".*([Ss]ecret).*"), Regex(".*(OAuth).*"))
    }

    private val log = KotlinLogging.logger {}
    private val springPropertySourcePrefixForYamlFiles = "applicationConfig:"

    fun logProperties(
        inclusions: List<Regex> = emptyList(),
        exclusions: List<Regex> = defaultExclusions
    ) {
        val properties = fetchProperties(inclusions, exclusions)

        log.info { "Spring Properties: (${inclusions.size} inclusions, ${exclusions.size} exclusions)" }
        properties.forEach { (key, value) -> log.info { "  $key => $value" } }
    }

    private fun fetchProperties(inclusions: List<Regex>, exclusions: List<Regex>): Map<String, Any> =
        springEnvironment.listAllMapPropertySources()
            .filter { it.name.startsWith(springPropertySourcePrefixForYamlFiles) }
            .flatMap { it.propertyNames.toSet() }
            .toSet()
            .filter { propertyName -> exclusions.all { !it.matches(propertyName) } }
            .filter { propertyName -> if (inclusions.isEmpty()) true else inclusions.any { regex -> propertyName.matches(regex) } }
            .sorted()
            .map { propertyName -> propertyName to (springEnvironment.getProperty(propertyName) ?: "null") }
            .toList()
            .toMap()

    /**
     * Unfortunately spring does not support accessing all keys/properties.
     *
     * https://stackoverflow.com/questions/23506471/spring-access-all-environment-properties-as-a-map-or-properties-object
     */
    private fun Environment.listAllMapPropertySources(): List<MapPropertySource> {
        val abstractEnvironment = this as? ConfigurableEnvironment ?: return emptyList()
        return abstractEnvironment.propertySources.filterIsInstance<MapPropertySource>()
    }

}
