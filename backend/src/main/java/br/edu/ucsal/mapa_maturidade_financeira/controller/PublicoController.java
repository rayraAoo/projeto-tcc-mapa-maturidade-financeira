package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.config.PoliticaPlataforma;
import br.edu.ucsal.mapa_maturidade_financeira.dto.TermosResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Informações públicas, acessíveis sem cadastro (inclusive aos pais e responsáveis). */
@RestController
@RequestMapping("/api/publico")
public class PublicoController {

    @GetMapping("/termos")
    public ResponseEntity<TermosResponse> termos() {
        return ResponseEntity.ok(new TermosResponse(
                PoliticaPlataforma.FAIXA_ETARIA_INDICADA,
                PoliticaPlataforma.IDADE_MINIMA,
                PoliticaPlataforma.MAIORIDADE,
                PoliticaPlataforma.VERSAO_TERMO_CIENCIA,
                PoliticaPlataforma.TEXTO_CIENCIA,
                PoliticaPlataforma.INFORMACOES_RESPONSAVEIS
        ));
    }
}
