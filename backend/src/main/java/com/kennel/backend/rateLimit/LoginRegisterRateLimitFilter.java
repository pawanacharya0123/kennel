package com.kennel.backend.rateLimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRegisterRateLimitFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    private Bucket newBucket(){
        return Bucket.builder()
                .addLimit(Bandwidth.simple(10, Duration.ofMinutes(3)))
                .build();
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals("/login") || path.equals("/register")) {
            String ip = request.getRemoteAddr();
            Bucket bucket = ipBuckets.computeIfAbsent(ip, k -> newBucket());

            if (!bucket.tryConsume(1)) {
                response.setStatus(429);
                response.getWriter().write("Too many requests – try again after 3 minutes.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
