package com.xyz.ordersvc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class ApiLogger {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static void logRequest(ContentCachingRequestWrapper requestWrapper) {
        byte[] content = requestWrapper.getContentAsByteArray();
        String body = null;
        if (content.length > 0) {
            body = new String(content, StandardCharsets.UTF_8);
        }
        log.info("Incoming Request [Method: {}] [URI: {}] [Headers: {}] [Body: {}]",
                requestWrapper.getMethod(), requestWrapper.getRequestURI(), getHeaders(requestWrapper), body);
    }

    public static void logResponse(Object responseObject) {
        if (responseObject == null) {
            log.info("Outgoing Response: null");
            return;
        }

        try {
            String jsonBody = objectMapper.writeValueAsString(responseObject);
            log.info("Outgoing Response [Body: {}]", jsonBody);
        } catch (Exception e) {
            log.warn("Could not serialize response to JSON. Using toString() instead. Error: {}",
                    e.getMessage());
            log.info("Response object: {}", responseObject);
        }
    }

    private static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            var headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private static Map<String, String> getHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();

        for (String headerName: headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }

}
