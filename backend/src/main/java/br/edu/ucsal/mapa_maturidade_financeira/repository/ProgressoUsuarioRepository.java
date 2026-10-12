package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.ProgressoUsuario;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressoUsuarioRepository extends JpaRepository<ProgressoUsuario, Long> {

    List<ProgressoUsuario> findByUsuario(Usuario usuario);

    Optional<ProgressoUsuario> findByUsuarioAndModulo(Usuario usuario, Modulo modulo);

    void deleteByUsuario(Usuario usuario);
}
