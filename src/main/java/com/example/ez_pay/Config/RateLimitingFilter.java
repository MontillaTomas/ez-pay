package com.example.ez_pay.Config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RateLimitingFilter implements Filter {

    private final Bucket bucket;

    @Autowired
    public RateLimitingFilter(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response); // Forward the request if rate limiting is not hit
        } else {
            //((HttpServletResponse) response).setStatus(429); // Return 429 if rate limit is exceeded
            httpResponse.setStatus(429);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("""
                {
                    "timestamp": "%s",
                    "error": "Rate limit exceeded",
                    "status": 429
                }
            """.formatted(java.time.LocalDateTime.now()));
        }
    }

}
