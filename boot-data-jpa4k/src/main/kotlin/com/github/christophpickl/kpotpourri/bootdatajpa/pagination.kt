package com.github.christophpickl.kpotpourri.bootdatajpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import javax.persistence.TypedQuery

const val DEFAULT_PAGE_SIZE = 5

val defaultPageable = pageOf(0, DEFAULT_PAGE_SIZE)

fun pageOf(pageNumber: Int?, pageSize: Int?): PageRequest = PageRequest.of(
    pageNumber ?: 0, pageSize ?: DEFAULT_PAGE_SIZE)


fun <T> TypedQuery<T>.resultPage(pagination: PaginationRequest): Page<T> {
    val totalRows = resultList.size

    firstResult = pagination.pageNumber * pagination.pageSize
    maxResults = pagination.pageSize

    return PageImpl(resultList, pagination.toPageable(), totalRows.toLong())
}


data class PaginationRequest(
    val pageNumber: Int,
    val pageSize: Int
) {
    companion object {
        val all get() = PaginationRequest(0, Int.MAX_VALUE)
    }
    fun toPageable() = pageOf(pageNumber, pageSize)
}
