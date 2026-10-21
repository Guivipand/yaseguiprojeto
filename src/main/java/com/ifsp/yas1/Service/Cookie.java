package com.ifsp.yas1.Service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletResponse;

public class Cookie {

    public void setCookie(HttpServletResponse resp, String name, String value,
            long maxAgeSeconds, boolean secure, String sameSite, boolean httpOnly) {

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .build();

        resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}