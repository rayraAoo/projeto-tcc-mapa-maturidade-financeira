package br.edu.ucsal.mapa_maturidade_financeira.dto;

public class ResultadoQuizResponse {

    private boolean correta;
    private String alternativaCorreta;
    private String explicacao;

    public ResultadoQuizResponse(boolean correta, String alternativaCorreta, String explicacao) {
        this.correta = correta;
        this.alternativaCorreta = alternativaCorreta;
        this.explicacao = explicacao;
    }

    public boolean isCorreta() {
        return correta;
    }

    public String getAlternativaCorreta() {
        return alternativaCorreta;
    }

    public String getExplicacao() {
        return explicacao;
    }
}
