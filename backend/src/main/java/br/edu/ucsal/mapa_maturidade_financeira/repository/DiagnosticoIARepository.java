package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.DiagnosticoIA;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoIARepository extends JpaRepository<DiagnosticoIA, Long> {

    List<DiagnosticoIA> findByUsuarioOrderByDataGeracaoDesc(Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}
