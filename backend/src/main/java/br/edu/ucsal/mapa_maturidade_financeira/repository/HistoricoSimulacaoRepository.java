package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.HistoricoSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoSimulacaoRepository extends JpaRepository<HistoricoSimulacao, Long> {

    List<HistoricoSimulacao> findByUsuarioOrderByDataSimulacaoDesc(Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}
