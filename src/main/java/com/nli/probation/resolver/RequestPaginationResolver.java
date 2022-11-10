package com.nli.probation.resolver;

import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.resolver.annotation.RequestPagingParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class RequestPaginationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestPagingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String index = request.getParameter("index");
        String limit = request.getParameter("limit");
        String sortType = request.getParameter("sortType");
        String sortBy = request.getParameter("sortBy");
        int indexNum = 0;
        int limitNum = 10;
        if(index != null) {
            indexNum = Integer.parseInt(index);
        }
        if(limit != null) {
            limitNum = Integer.parseInt(limit);
        }

        return new RequestPaginationModel(indexNum, limitNum, sortBy, sortType);
    }
}
