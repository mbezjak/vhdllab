package hr.fer.zemris.vhdllab.web;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class LogRequestHeadersFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        Enumeration<?> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder(2000);
        sb.append("Request headers for uri: ").append(request.getRequestURI());
        sb.append("\n");
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            String value = request.getHeader(name);
            sb.append(name).append(":").append(value).append("\n");
        }
        logger.debug(sb.toString());
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
    }

}
