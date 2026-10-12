package br.edu.ucsal.mapa_maturidade_financeira.repository;

import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilOnboardingRepository extends JpaRepository<PerfilOnboarding, Long> {

    Optional<PerfilOnboarding> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}
