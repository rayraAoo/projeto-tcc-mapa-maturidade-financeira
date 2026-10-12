package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.CenarioSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.OpcaoSimulacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcaoSimulacaoRepository extends JpaRepository<OpcaoSimulacao, Long> {

    List<OpcaoSimulacao> findByCenario(CenarioSimulacao cenario);

    List<OpcaoSimulacao> findByCenarioId(Long cenarioId);
}
