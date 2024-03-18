package com.casumo.races.filters;

import com.casumo.races.exception.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.casumo.races.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/auth/**");

    private static final String AUTH_TOKEN_HEADER_NAME = "access_token";

    private final AuthService authService;

    public TokenAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    private Authentication getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (token == null) {
            throw new BadCredentialsException("Invalid Token");
        }

        return this.authService.parseToken(token);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            Authentication authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception exp) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorResponse errorResponse = new ErrorResponse(exp.getMessage(), exp.getMessage());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String responseJson = ow.writeValueAsString(errorResponse);
            PrintWriter writer = response.getWriter();
            writer.print(responseJson);
            writer.flush();
            writer.close();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        log.error("should not filter method");
        return this.ignoredPaths.matches(request);
    }


}