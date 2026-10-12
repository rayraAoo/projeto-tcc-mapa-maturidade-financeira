package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    List<Pergunta> findByModulo(Modulo modulo);
}
