package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.CenarioSimulacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenarioSimulacaoRepository extends JpaRepository<CenarioSimulacao, Long> {

    List<CenarioSimulacao> findByCategoria(String categoria);
}
