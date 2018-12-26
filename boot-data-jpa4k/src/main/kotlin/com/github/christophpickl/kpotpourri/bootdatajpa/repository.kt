package com.github.christophpickl.kpotpourri.bootdatajpa

import com.github.christophpickl.kpotpourri.common.exception.NotFoundException
import org.springframework.data.repository.CrudRepository

fun <T, ID> CrudRepository<T, ID>.findByIdOrNull(id: ID): T? = findById(id).orElse(null)
fun <T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T = findByIdOrNull(id)
    ?: throw NotFoundException("Not found by ID: $id")
