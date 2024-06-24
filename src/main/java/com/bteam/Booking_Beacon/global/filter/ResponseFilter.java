package com.bteam.Booking_Beacon.global.filter;


import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class ResponseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("ResponseFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 처리 전
        Date startDate = new Date();
        filterChain.doFilter(servletRequest, servletResponse);
        Date endDate = new Date();
        // 처리 후
        log.info("[Time] request ~ response : {}ms", endDate.getTime()-startDate.getTime());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
