package br.edu.ucsal.mapa_maturidade_financeira.dto;

import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.model.Pergunta;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Garante que nenhuma resposta da API volta a vazar dados que não devem sair:
 * hash de senha, gabarito do quiz ou o usuário embutido em outra entidade.
 */
class SegurancaDasRespostasTest {

    private static Set<String> campos(Class<?> tipo) {
        return Arrays.stream(tipo.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
    }

    @Test
    void perguntaDtoNaoExpoeGabaritoNemExplicacao() {
        Set<String> campos = campos(PerguntaDTO.class);

        assertFalse(campos.contains("alternativaCorreta"));
        assertFalse(campos.contains("explicacao"));
    }

    @Test
    void perguntaDtoPreservaEnunciadoEAlternativas() {
        Modulo modulo = new Modulo("Orçamento", "conteúdo", 1, 1);
        ReflectionTestUtils.setField(modulo, "id", 7L);
        Pergunta pergunta = new Pergunta(modulo, "Enunciado?", "a", "b", "c", "d", "C", "Explicação");
        ReflectionTestUtils.setField(pergunta, "id", 3L);

        PerguntaDTO dto = PerguntaDTO.de(pergunta);

        assertEquals(3L, dto.getId());
        assertEquals(7L, dto.getModuloId());
        assertEquals("Enunciado?", dto.getEnunciado());
        assertEquals("c", dto.getAlternativaC());
    }

    @Test
    void usuarioResponseNaoExpoeSenha() {
        assertFalse(campos(UsuarioResponse.class).contains("senha"));

        Usuario usuario = new Usuario("Alice", "alice@teste.com", "hash-secreto", LocalDate.now().minusYears(20));
        UsuarioResponse resposta = UsuarioResponse.de(usuario);

        assertEquals("Alice", resposta.getNome());
        assertEquals("alice@teste.com", resposta.getEmail());
    }

    @Test
    void senhaDoUsuarioNuncaEhSerializada() throws NoSuchFieldException {
        assertTrue(Usuario.class.getDeclaredField("senha").isAnnotationPresent(JsonIgnore.class));
    }

    @Test
    void perfilNaoSerializaOUsuarioEmbutido() throws NoSuchFieldException {
        assertTrue(PerfilOnboarding.class.getDeclaredField("usuario").isAnnotationPresent(JsonIgnore.class));
        assertFalse(campos(PerfilResponse.class).contains("usuario"));
    }
}
