package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
