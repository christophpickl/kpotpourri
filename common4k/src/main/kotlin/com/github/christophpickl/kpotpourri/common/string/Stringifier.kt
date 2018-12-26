package com.github.christophpickl.kpotpourri.common.string

import kotlin.reflect.KVisibility
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


@Target(AnnotationTarget.PROPERTY)
annotation class IgnoreStringified

object Stringifier {

    inline fun <reified T : Any> stringify(any: T) =
        "${any::class.simpleName}{${
        T::class.memberProperties
//            .map { println(it); it }
            .filter { prop ->
                prop.findAnnotation<IgnoreStringified>() == null &&
                    any::class.allSuperclasses.all { superClass ->
                        superClass.members.filter { superProp -> superProp.name == prop.name }.all { superProp ->
                            superProp.findAnnotation<IgnoreStringified>() == null
                        }
                    } &&
                    !prop.isAbstract &&
                    prop.visibility == KVisibility.PUBLIC
            }
            .map {
                it.isAccessible = true
                it
            }
            .sortedBy { it.name }
            .joinToString() {
                "${it.name}=${it.get(any)}"
            }}}"

}
