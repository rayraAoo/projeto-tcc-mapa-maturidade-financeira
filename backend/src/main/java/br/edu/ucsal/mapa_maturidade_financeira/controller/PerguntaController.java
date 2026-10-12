package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.PerguntaDTO;
import br.edu.ucsal.mapa_maturidade_financeira.dto.RespostaQuizRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.ResultadoQuizResponse;
import br.edu.ucsal.mapa_maturidade_financeira.service.PerguntaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quiz")
public class PerguntaController {

    private final PerguntaService perguntaService;

    public PerguntaController(PerguntaService perguntaService) {
        this.perguntaService = perguntaService;
    }

    /** Devolve as perguntas SEM o gabarito: a resposta correta só aparece depois de responder. */
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<PerguntaDTO>> listarPerguntas(@PathVariable Long moduloId) {
        List<PerguntaDTO> perguntas = perguntaService.listarPerguntasDoModulo(moduloId).stream()
                .map(PerguntaDTO::de)
                .collect(Collectors.toList());
        return ResponseEntity.ok(perguntas);
    }

    @PostMapping("/responder")
    public ResponseEntity<ResultadoQuizResponse> responder(
            @Valid @RequestBody RespostaQuizRequest dados,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                perguntaService.corrigirResposta(
                        authentication.getName(), dados.getPerguntaId(), dados.getAlternativaEscolhida()));
    }
}
