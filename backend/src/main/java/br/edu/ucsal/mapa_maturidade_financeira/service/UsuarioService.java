package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.config.JwtUtil;
import br.edu.ucsal.mapa_maturidade_financeira.config.PoliticaPlataforma;
import br.edu.ucsal.mapa_maturidade_financeira.dto.CadastroRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginResponse;
import br.edu.ucsal.mapa_maturidade_financeira.exception.CredenciaisInvalidasException;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PerfilOnboardingRepository perfilRepository;
    private final ProgressoUsuarioRepository progressoRepository;
    private final RespostaUsuarioRepository respostaRepository;
    private final DiagnosticoIARepository diagnosticoRepository;
    private final HistoricoSimulacaoRepository historicoSimulacaoRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            PerfilOnboardingRepository perfilRepository,
            ProgressoUsuarioRepository progressoRepository,
            RespostaUsuarioRepository respostaRepository,
            DiagnosticoIARepository diagnosticoRepository,
            HistoricoSimulacaoRepository historicoSimulacaoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.perfilRepository = perfilRepository;
        this.progressoRepository = progressoRepository;
        this.respostaRepository = respostaRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.historicoSimulacaoRepository = historicoSimulacaoRepository;
    }

    /**
     * Regras de cadastro:
     * - a plataforma é indicada para adolescentes (12+) e adultos; abaixo de 12 anos o cadastro é recusado;
     * - de 12 a 17 anos, é obrigatória a declaração de que os pais ou responsáveis estão cientes
     *   (declaração autodeclarada, registrada com data, hora e versão do texto);
     * - a partir de 18 anos, nenhuma declaração é necessária.
     */
    public Usuario cadastrar(CadastroRequest dados) {

        if (usuarioRepository.existsByEmail(dados.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        int idade = Period.between(dados.getDataNascimento(), LocalDate.now()).getYears();
        if (idade < PoliticaPlataforma.IDADE_MINIMA) {
            throw new IllegalArgumentException("A plataforma é indicada para pessoas com "
                    + PoliticaPlataforma.IDADE_MINIMA + " anos ou mais.");
        }

        boolean menorDeIdade = idade < PoliticaPlataforma.MAIORIDADE;
        if (menorDeIdade && !Boolean.TRUE.equals(dados.getConfirmaCienciaResponsavel())) {
            throw new IllegalArgumentException("Para menores de 18 anos é necessário declarar que os pais "
                    + "ou responsáveis estão cientes do uso da plataforma.");
        }

        String senhaCriptografada = passwordEncoder.encode(dados.getSenha());

        Usuario novoUsuario = new Usuario(
                dados.getNome(),
                dados.getEmail(),
                senhaCriptografada,
                dados.getDataNascimento()
        );

        if (menorDeIdade) {
            novoUsuario.setCienciaResponsavel(true);
            novoUsuario.setDataCienciaResponsavel(LocalDateTime.now());
            novoUsuario.setVersaoTermoCiencia(PoliticaPlataforma.VERSAO_TERMO_CIENCIA);
        }

        return usuarioRepository.save(novoUsuario);
    }

    public LoginResponse login(LoginRequest dados) {

        Usuario usuario = usuarioRepository.findByEmail(dados.getEmail())
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos."));

        boolean senhaCorreta = passwordEncoder.matches(dados.getSenha(), usuario.getSenha());
        if (!senhaCorreta) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());

        return new LoginResponse(token, usuario.getNome(), usuario.getEmail());
    }

    @Transactional
    public void excluirConta(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        historicoSimulacaoRepository.deleteByUsuario(usuario);
        respostaRepository.deleteByUsuario(usuario);
        diagnosticoRepository.deleteByUsuario(usuario);
        progressoRepository.deleteByUsuario(usuario);
        perfilRepository.deleteByUsuario(usuario);

        usuarioRepository.delete(usuario);
    }
}
