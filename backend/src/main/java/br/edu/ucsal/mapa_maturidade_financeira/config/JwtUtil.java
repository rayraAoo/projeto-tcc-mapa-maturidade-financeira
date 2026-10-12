package br.edu.ucsal.mapa_maturidade_financeira.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final SecretKey chaveSecreta;
    private final long tempoExpiracaoMs = 1000L * 60 * 60 * 10; // 10 horas

    /**
     * A chave vem da propriedade jwt.secret (variável de ambiente JWT_SECRET).
     * Nunca fica escrita no código-fonte. Se não for informada, gera-se uma chave aleatória
     * a cada inicialização: serve para desenvolvimento, mas invalida os tokens ao reiniciar.
     */
    public JwtUtil(@Value("${jwt.secret:}") String segredo) {
        if (segredo == null || segredo.isBlank()) {
            log.warn("jwt.secret não configurado: usando chave aleatória (tokens perdem a validade ao reiniciar). "
                    + "Defina a variável de ambiente JWT_SECRET.");
            this.chaveSecreta = Jwts.SIG.HS256.key().build();
        } else {
            byte[] bytes = segredo.getBytes(StandardCharsets.UTF_8);
            if (bytes.length < 32) {
                throw new IllegalStateException("jwt.secret deve ter pelo menos 32 caracteres.");
            }
            this.chaveSecreta = Keys.hmacShaKeyFor(bytes);
        }
    }

    public String gerarToken(String email) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + tempoExpiracaoMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(chaveSecreta)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairTodasClaims(token).getSubject();
    }

    public boolean tokenValido(String token, String email) {
        String emailDoToken = extrairEmail(token);
        return emailDoToken.equals(email) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extrairTodasClaims(token).getExpiration().before(new Date());
    }

    private Claims extrairTodasClaims(String token) {
        return Jwts.parser()
                .verifyWith(chaveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
