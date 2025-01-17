package com.alura.restapiforohubchallenge.security;

import java.io.IOException;

import com.alura.restapiforohubchallenge.domain.login.jsonwebtoken.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import com.alura.restapiforohubchallenge.domain.login.user.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String tokenReceivedHeader = request.getHeader("Authorization");

        if (tokenReceivedHeader != null && tokenReceivedHeader.startsWith("Bearer ")) {
            String token = tokenReceivedHeader.replace("Bearer ", "").trim();

            // Valida el token
            if (tokenService.validateToken(token)) {
                String username = tokenService.getSubjectToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userRepository.findByUserName(username);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized: Invalid token.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
