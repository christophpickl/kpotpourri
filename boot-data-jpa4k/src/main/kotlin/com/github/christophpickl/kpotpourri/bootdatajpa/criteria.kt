package com.github.christophpickl.kpotpourri.bootdatajpa

import com.github.christophpickl.kpotpourri.common.exception.NotFoundException
import org.springframework.data.repository.CrudRepository
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate

fun CriteriaBuilder.or(restrictions: List<Predicate>): Predicate =
    or(*restrictions.toTypedArray())

fun CriteriaBuilder.and(restrictions: List<Predicate>): Predicate =
    and(*restrictions.toTypedArray())

