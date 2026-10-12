package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.PerfilResponse;
import br.edu.ucsal.mapa_maturidade_financeira.dto.QuestionarioRequest;
import br.edu.ucsal.mapa_maturidade_financeira.service.PerfilOnboardingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilOnboardingController {

    private final PerfilOnboardingService perfilService;

    public PerfilOnboardingController(PerfilOnboardingService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/atual")
    public ResponseEntity<PerfilResponse> obterPerfilAtual(Authentication authentication) {
        return perfilService.obterPerfil(authentication.getName())
                .map(PerfilResponse::de)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/questionario")
    public ResponseEntity<PerfilResponse> responderQuestionario(
            @Valid @RequestBody QuestionarioRequest respostas,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                PerfilResponse.de(perfilService.calcularEsalvar(authentication.getName(), respostas)));
    }
}
