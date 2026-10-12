package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.CenarioSimulacaoDTO;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ExecutarSimulacaoRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ResultadoSimulacaoResponse;
import br.edu.ucsal.mapa_maturidade_financeira.service.SimuladorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulador")
public class SimuladorController {

    private final SimuladorService simuladorService;

    public SimuladorController(SimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @GetMapping("/cenarios")
    public ResponseEntity<List<CenarioSimulacaoDTO>> listarCenarios() {
        return ResponseEntity.ok(simuladorService.listarCenarios());
    }

    @GetMapping("/cenarios/{id}")
    public ResponseEntity<CenarioSimulacaoDTO> buscarCenario(@PathVariable Long id) {
        return ResponseEntity.ok(simuladorService.buscarCenarioPorId(id));
    }

    @PostMapping("/executar")
    public ResponseEntity<ResultadoSimulacaoResponse> executarSimulacao(
            @Valid @RequestBody ExecutarSimulacaoRequest request,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        return ResponseEntity.ok(simuladorService.executarSimulacao(emailUsuario, request));
    }

    @GetMapping("/historico")
    public ResponseEntity<List<ResultadoSimulacaoResponse>> listarHistorico(Authentication authentication) {
        String emailUsuario = authentication.getName();
        return ResponseEntity.ok(simuladorService.listarHistoricoDoUsuario(emailUsuario));
    }
}
