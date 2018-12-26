package com.github.christophpickl.kpotpourri.bootweb

import com.github.christophpickl.kpotpourri.bootdatajpa.DEFAULT_PAGE_SIZE
import com.github.christophpickl.kpotpourri.bootdatajpa.PaginationRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class PaginationHandlerMethodArgumentResolver(
    private val paramNamePageNumber: String,
    private val paramNamePageSize: String
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.parameterType == PaginationRequest::class.java

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?) =
        PaginationRequest(
            pageNumber = (webRequest.parameterMap[paramNamePageNumber]?.first()?.toInt()?.let { it - 1 })
                ?: 0,
            pageSize = webRequest.parameterMap[paramNamePageSize]?.first()?.toInt()
                ?: DEFAULT_PAGE_SIZE
        )

}
