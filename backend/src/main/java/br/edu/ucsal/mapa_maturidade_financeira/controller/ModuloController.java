package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.ConcluirModuloRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ProgressoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.service.ModuloService;
import br.edu.ucsal.mapa_maturidade_financeira.service.ProgressoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService moduloService;
    private final ProgressoService progressoService;

    public ModuloController(ModuloService moduloService, ProgressoService progressoService) {
        this.moduloService = moduloService;
        this.progressoService = progressoService;
    }

    @GetMapping("/trilha")
    public ResponseEntity<List<Modulo>> listarTrilha(Authentication authentication) {
        return ResponseEntity.ok(moduloService.listarTrilhaDoUsuario(authentication.getName()));
    }

    @PostMapping("/concluir")
    public ResponseEntity<Void> concluirModulo(
            @Valid @RequestBody ConcluirModuloRequest dados,
            Authentication authentication
    ) {
        moduloService.marcarComoConcluido(authentication.getName(), dados.getModuloId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/concluidos")
    public ResponseEntity<List<Long>> listarConcluidos(Authentication authentication) {
        return ResponseEntity.ok(moduloService.listarIdsConcluidos(authentication.getName()));
    }

    @GetMapping("/progresso")
    public ResponseEntity<ProgressoResponse> obterProgresso(Authentication authentication) {
        return ResponseEntity.ok(progressoService.obterProgresso(authentication.getName()));
    }
}
