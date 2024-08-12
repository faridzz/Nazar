package org.example.nazar.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.nazar.util.network.RequestInfoExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ReqAndResCounterInterceptor implements HandlerInterceptor {

    private final AtomicInteger succeedResCounter = new AtomicInteger(0);
    private final AtomicInteger reqCounter = new AtomicInteger(0);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        reqCounter.incrementAndGet();
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex == null) {
            succeedResCounter.incrementAndGet();
        } else {
            log.error("Exception : {} status : {}  in request : {} total req/res : {}",
                    ex.getMessage(),
                    response.getStatus(),
                    RequestInfoExtractor.extractRequestInfo(request),
                    (reqCounter + "/"+  succeedResCounter)
            );
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
