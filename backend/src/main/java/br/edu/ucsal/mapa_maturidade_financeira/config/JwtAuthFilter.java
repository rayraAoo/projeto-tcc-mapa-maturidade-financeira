package br.edu.ucsal.mapa_maturidade_financeira.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String cabecalhoAuth = request.getHeader("Authorization");

        if (cabecalhoAuth != null && cabecalhoAuth.startsWith("Bearer ")) {
            String token = cabecalhoAuth.substring(7);

            try {
                String email = jwtUtil.extrairEmail(token);

                if (jwtUtil.tokenValido(token, email)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token inválido ou expirado: a requisição segue sem
                // autenticação e será bloqueada adiante, se o endpoint exigir login.
            }
        }

        filterChain.doFilter(request, response);
    }
}
