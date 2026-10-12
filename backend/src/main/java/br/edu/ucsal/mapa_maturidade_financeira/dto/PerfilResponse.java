package br.edu.ucsal.mapa_maturidade_financeira.dto;

import br.edu.ucsal.mapa_maturidade_financeira.model.PerfilOnboarding;
import br.edu.ucsal.mapa_maturidade_financeira.service.PerfilOnboardingService;

public class PerfilResponse {

    private final String perfil;
    private final String descricao;
    private final Integer pontuacaoTotal;

    public PerfilResponse(String perfil, String descricao, Integer pontuacaoTotal) {
        this.perfil = perfil;
        this.descricao = descricao;
        this.pontuacaoTotal = pontuacaoTotal;
    }

    public static PerfilResponse de(PerfilOnboarding p) {
        return new PerfilResponse(
                p.getPerfilCalculado(),
                PerfilOnboardingService.descricaoDoPerfil(p.getPerfilCalculado()),
                p.getPontuacaoTotal()
        );
    }

    public String getPerfil() {
        return perfil;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getPontuacaoTotal() {
        return pontuacaoTotal;
    }
}
