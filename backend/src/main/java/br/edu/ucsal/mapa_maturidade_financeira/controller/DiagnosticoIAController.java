package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.DiagnosticoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.dto.GerarDiagnosticoRequest;
import br.edu.ucsal.mapa_maturidade_financeira.service.DiagnosticoIAService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ia/diagnostico")
public class DiagnosticoIAController {

    private final DiagnosticoIAService diagnosticoService;

    public DiagnosticoIAController(DiagnosticoIAService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    /** Exige autorização explícita (autorizaEnvioIa = true) a cada geração. */
    @PostMapping("/gerar")
    public ResponseEntity<DiagnosticoResponse> gerarDiagnostico(
            @Valid @RequestBody GerarDiagnosticoRequest autorizacao,
            Authentication authentication
    ) {
        return ResponseEntity.ok(diagnosticoService.gerarDiagnostico(authentication.getName()));
    }

    @GetMapping("/ultimo")
    public ResponseEntity<DiagnosticoResponse> obterUltimo(Authentication authentication) {
        return ResponseEntity.ok(diagnosticoService.obterUltimoDiagnostico(authentication.getName()));
    }

    @GetMapping("/historico")
    public ResponseEntity<List<DiagnosticoResponse>> listarHistorico(Authentication authentication) {
        return ResponseEntity.ok(diagnosticoService.listarHistorico(authentication.getName()));
    }
}
