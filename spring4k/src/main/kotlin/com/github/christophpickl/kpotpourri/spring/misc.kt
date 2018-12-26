package com.github.christophpickl.kpotpourri.spring

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.type.filter.AnnotationTypeFilter

fun ByteArray.toInputStreamSource() = ByteArrayResource(this)

object ClassPathScanner {

    inline fun <reified T> scan(annotation: Class<out Annotation>): List<T> {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AnnotationTypeFilter(annotation))
        return scanner.findCandidateComponents("at.namaste").map {
            Class.forName(it.beanClassName).newInstance() as? T
                ?: throw RuntimeException("Expected bean of class [${it.beanClassName}] to be of type ${T::class.java.name}")
        }
    }

}
