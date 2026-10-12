package br.edu.ucsal.mapa_maturidade_financeira.service;

import br.edu.ucsal.mapa_maturidade_financeira.config.JwtUtil;
import br.edu.ucsal.mapa_maturidade_financeira.config.PoliticaPlataforma;
import br.edu.ucsal.mapa_maturidade_financeira.dto.CadastroRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginRequest;
import br.edu.ucsal.mapa_maturidade_financeira.dto.LoginResponse;
import br.edu.ucsal.mapa_maturidade_financeira.exception.CredenciaisInvalidasException;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import br.edu.ucsal.mapa_maturidade_financeira.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PerfilOnboardingRepository perfilRepository;

    @Mock
    private ProgressoUsuarioRepository progressoRepository;

    @Mock
    private RespostaUsuarioRepository respostaRepository;

    @Mock
    private DiagnosticoIARepository diagnosticoRepository;

    @Mock
    private HistoricoSimulacaoRepository historicoSimulacaoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private CadastroRequest cadastroValido;

    @BeforeEach
    void setUp() {
        cadastroValido = new CadastroRequest();
        cadastroValido.setNome("Alice Rayra");
        cadastroValido.setEmail("alice@teste.com");
        cadastroValido.setSenha("senha1234");
        cadastroValido.setDataNascimento(LocalDate.now().minusYears(20));
    }

    private void prepararCadastroPermitido() {
        when(usuarioRepository.existsByEmail("alice@teste.com")).thenReturn(false);
        when(passwordEncoder.encode("senha1234")).thenReturn("hash123");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void deveCadastrarAdultoSemPrecisarDeDeclaracao() {
        prepararCadastroPermitido();

        Usuario usuario = usuarioService.cadastrar(cadastroValido);

        assertNotNull(usuario);
        assertEquals("Alice Rayra", usuario.getNome());
        assertEquals("alice@teste.com", usuario.getEmail());
        assertEquals("hash123", usuario.getSenha());
        assertNull(usuario.getCienciaResponsavel());
        assertNull(usuario.getDataCienciaResponsavel());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveCadastrarAdolescenteDe12AnosComDeclaracaoERegistrarCiencia() {
        cadastroValido.setDataNascimento(LocalDate.now().minusYears(12));
        cadastroValido.setConfirmaCienciaResponsavel(true);
        prepararCadastroPermitido();

        Usuario usuario = usuarioService.cadastrar(cadastroValido);

        assertEquals(Boolean.TRUE, usuario.getCienciaResponsavel());
        assertNotNull(usuario.getDataCienciaResponsavel());
        assertEquals(PoliticaPlataforma.VERSAO_TERMO_CIENCIA, usuario.getVersaoTermoCiencia());
    }

    @Test
    void deveCadastrarAdolescenteDe17AnosComDeclaracao() {
        cadastroValido.setDataNascimento(LocalDate.now().minusYears(17));
        cadastroValido.setConfirmaCienciaResponsavel(true);
        prepararCadastroPermitido();

        Usuario usuario = usuarioService.cadastrar(cadastroValido);

        assertEquals(Boolean.TRUE, usuario.getCienciaResponsavel());
    }

    @Test
    void deveExigirDeclaracaoParaMenorDe18() {
        cadastroValido.setDataNascimento(LocalDate.now().minusYears(15));
        cadastroValido.setConfirmaCienciaResponsavel(null);
        when(usuarioRepository.existsByEmail("alice@teste.com")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(cadastroValido));

        assertTrue(exception.getMessage().contains("responsáveis"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveRecusarDeclaracaoFalsaParaMenorDe18() {
        cadastroValido.setDataNascimento(LocalDate.now().minusYears(16));
        cadastroValido.setConfirmaCienciaResponsavel(false);
        when(usuarioRepository.existsByEmail("alice@teste.com")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrar(cadastroValido));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveBloquearCadastroDeMenorDe12Anos() {
        cadastroValido.setDataNascimento(LocalDate.now().minusYears(11));
        cadastroValido.setConfirmaCienciaResponsavel(true);
        when(usuarioRepository.existsByEmail("alice@teste.com")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(cadastroValido));

        assertTrue(exception.getMessage().contains("12 anos"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveBloquearCadastroComDataDeNascimentoInvalida() {
        cadastroValido.setDataNascimento(LocalDate.now().minusYears(2));
        when(usuarioRepository.existsByEmail("alice@teste.com")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrar(cadastroValido));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveIgnorarDeclaracaoQuandoUsuarioForMaiorDeIdade() {
        cadastroValido.setConfirmaCienciaResponsavel(true);
        prepararCadastroPermitido();

        Usuario usuario = usuarioService.cadastrar(cadastroValido);

        assertNull(usuario.getCienciaResponsavel(), "Adulto não deve ter declaração de ciência registrada");
    }

    @Test
    void deveFalharAoCadastrarComEmailJaExistente() {
        when(usuarioRepository.existsByEmail("alice@teste.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(cadastroValido));

        assertTrue(exception.getMessage().contains("Já existe um usuário"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveRealizarLoginComSucesso() {
        Usuario usuario = new Usuario("Alice", "alice@teste.com", "hash123", LocalDate.now().minusYears(20));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("alice@teste.com");
        loginRequest.setSenha("senha1234");

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha1234", "hash123")).thenReturn(true);
        when(jwtUtil.gerarToken("alice@teste.com")).thenReturn("mock-token-jwt");

        LoginResponse response = usuarioService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock-token-jwt", response.getToken());
    }

    @Test
    void deveFalharLoginComSenhaIncorreta() {
        Usuario usuario = new Usuario("Alice", "alice@teste.com", "hash123", LocalDate.now().minusYears(20));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("alice@teste.com");
        loginRequest.setSenha("senha_errada");

        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha_errada", "hash123")).thenReturn(false);

        assertThrows(CredenciaisInvalidasException.class, () -> usuarioService.login(loginRequest));
    }

    @Test
    void deveFalharLoginComEmailInexistenteComAMesmaMensagem() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("naoexiste@teste.com");
        loginRequest.setSenha("qualquer123");
        when(usuarioRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

        CredenciaisInvalidasException ex = assertThrows(CredenciaisInvalidasException.class,
                () -> usuarioService.login(loginRequest));

        // Mesma mensagem do caso "senha incorreta": não revela se o e-mail existe.
        assertEquals("E-mail ou senha inválidos.", ex.getMessage());
    }

    @Test
    void deveExcluirContaComSucesso() {
        Usuario usuario = new Usuario("Alice", "alice@teste.com", "hash123", LocalDate.now().minusYears(20));
        when(usuarioRepository.findByEmail("alice@teste.com")).thenReturn(Optional.of(usuario));

        usuarioService.excluirConta("alice@teste.com");

        verify(historicoSimulacaoRepository, times(1)).deleteByUsuario(usuario);
        verify(respostaRepository, times(1)).deleteByUsuario(usuario);
        verify(diagnosticoRepository, times(1)).deleteByUsuario(usuario);
        verify(progressoRepository, times(1)).deleteByUsuario(usuario);
        verify(perfilRepository, times(1)).deleteByUsuario(usuario);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void deveFalharAoExcluirContaDeUsuarioInexistente() {
        when(usuarioRepository.findByEmail("inexistente@teste.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> usuarioService.excluirConta("inexistente@teste.com"));

        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
}
