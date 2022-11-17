package com.ra.order.security.filters;

import com.ra.order.dto.response.ValidateTokenResponse;
import com.ra.order.util.Constants;
import com.ra.order.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationJwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${service.client.authService.url}")
    private String authClient;

    private final WebClient webClient;

    public AuthenticationJwtFilter(JwtUtil jwtUtil, WebClient webClient) {
        this.jwtUtil = jwtUtil;
        this.webClient = webClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!hasToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        if (!jwtUtil.validateJwtToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(token);

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {

        ValidateTokenResponse response = webClient.get().uri(authClient + "/api/auth/validateToken")
                .header(Constants.HEADER, Constants.TOKEN_PREFIX + token)
                .retrieve()
                .bodyToMono(ValidateTokenResponse.class).block();

        if(response == null) return;

        String email = jwtUtil.getUserNameFromJwtToken(response.getToken());

        String[] authorities = { response.getAuthority() };

        List<SimpleGrantedAuthority> simpleAuthorities = new ArrayList<>();
        simpleAuthorities.add(new SimpleGrantedAuthority(authorities[0]));

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                email,
                null,
                simpleAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(Constants.HEADER);
        return header.split("\\s")[1].trim();
    }

    private boolean hasToken(HttpServletRequest request) {
        String header = request.getHeader(Constants.HEADER);
        return !ObjectUtils.isEmpty(header) && header.startsWith(Constants.TOKEN_PREFIX);
    }
}

