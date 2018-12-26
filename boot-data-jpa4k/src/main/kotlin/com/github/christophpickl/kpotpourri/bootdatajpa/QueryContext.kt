package com.github.christophpickl.kpotpourri.bootdatajpa

import org.springframework.data.domain.Page
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import javax.persistence.metamodel.SingularAttribute

inline fun <reified T> EntityManager.buildQueryContext(): QueryContext<T> {
    val builder = criteriaBuilder
    val query = builder.createQuery(T::class.java)
    val root = query.from(T::class.java)
    return QueryContext(builder, query, root, this)
}

data class QueryContext<T>(
    val builder: CriteriaBuilder,
    val query: CriteriaQuery<T>,
    val root: Root<T>,
    val em: EntityManager
) :
    CriteriaBuilder by builder,
    CriteriaQuery<T> by query {

    fun <TO> get(attribute: SingularAttribute<T, TO>): Path<TO> = root.get(attribute)

    fun and(restrictions: List<Predicate>) = builder.and(restrictions)

    fun resultList(): List<T> = em.createQuery(query).resultList
    fun resultPage(pagination: PaginationRequest): Page<T> = em.createQuery(query).resultPage(pagination)
    fun resultFirstOrNull(): T? = em.createQuery(query).apply {
        maxResults = 1
    }.resultList.firstOrNull()

}

inline fun <reified JPA, R> EntityManager.withQueryContext(wither: QueryContext<JPA>.() -> R): R =
    with(buildQueryContext(), wither)
