package br.edu.ucsal.mapa_maturidade_financeira.controller;

import br.edu.ucsal.mapa_maturidade_financeira.dto.CadastroRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginResponse;
import br.edu.ucsal.mapa_maturidade_financeira.dto.UsuarioResponse;
import br.edu.ucsal.mapa_maturidade_financeira.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroRequest dados) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponse.de(usuarioService.cadastrar(dados)));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest dados) {
        return ResponseEntity.ok(usuarioService.login(dados));
    }

    @DeleteMapping("/minha-conta")
    public ResponseEntity<Map<String, String>> excluirConta(Authentication authentication) {
        usuarioService.excluirConta(authentication.getName());
        return ResponseEntity.ok(Map.of("mensagem", "Conta e dados associados excluídos com sucesso."));
    }
}
