package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.Pergunta;
import br.edu.ucsal.mapa_maturidade_financeira.model.RespostaUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RespostaUsuarioRepository extends JpaRepository<RespostaUsuario, Long> {

    List<RespostaUsuario> findByUsuario(Usuario usuario);

    Optional<RespostaUsuario> findFirstByUsuarioAndPerguntaOrderByIdDesc(Usuario usuario, Pergunta pergunta);

    void deleteByUsuario(Usuario usuario);
}
