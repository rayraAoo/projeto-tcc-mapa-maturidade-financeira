package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    List<Modulo> findByCamadaOrderByOrdemAsc(Integer camada);
}
