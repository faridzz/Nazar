package org.example.nazar.util.network;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.stream.Collectors;


public class RequestInfoExtractor {

    public static String extractRequestInfo(HttpServletRequest request) throws UnsupportedEncodingException {
        // Wrap the request to enable reading the body
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        // Extract request information
        String body = getRequestBody(requestWrapper);
        String path = getPath(requestWrapper);
        String requestParams = getRequestParameters(requestWrapper);
        String requestHeaders = getRequestHeaders(requestWrapper);

        // Combine non-empty values
        StringBuilder result = new StringBuilder();

        if (!path.isEmpty()) {
            result.append("Path : ").append(path).append("\n");
        }

        if (!body.isEmpty()) {
            result.append("Request Body: ").append(body).append("\n");
        }

        if (!requestParams.isEmpty()) {
            result.append("Request Parameters: ").append(requestParams).append("\n");
        }

        if (!requestHeaders.isEmpty()) {
            result.append("Request Headers: ").append(requestHeaders).append("\n");
        }

        return result.toString();
    }

    private static String getRequestBody(ContentCachingRequestWrapper requestWrapper) throws UnsupportedEncodingException {
        byte[] body = requestWrapper.getContentAsByteArray();
        return body.length > 0 ? new String(body, requestWrapper.getCharacterEncoding()) : "";
    }

    private static String getPath(HttpServletRequest request) {
        return request.getRequestURI();

    }

    private static String getRequestParameters(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .filter(entry -> entry.getValue().length > 0)
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    private static String getRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (headerValue != null && !headerValue.isEmpty()) {
                headers.append(headerName).append("=").append(headerValue).append(", ");
            }
        }

        return headers.toString().isEmpty() ? "" : headers.substring(0, headers.length() - 2);
    }
}
