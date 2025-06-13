package com.e_boutique.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class XssProtectionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req) {
            @Override
            public String getParameter(String name) {
                String value = super.getParameter(name);
                return value != null ? HtmlUtils.htmlEscape(value) : null;
            }

            @Override
            public String[] getParameterValues(String name) {
                String[] values = super.getParameterValues(name);
                if (values == null) return null;
                for (int i = 0; i < values.length; i++) {
                    values[i] = HtmlUtils.htmlEscape(values[i]);
                }
                return values;
            }
        };

        chain.doFilter(wrappedRequest, response);
    }
}
