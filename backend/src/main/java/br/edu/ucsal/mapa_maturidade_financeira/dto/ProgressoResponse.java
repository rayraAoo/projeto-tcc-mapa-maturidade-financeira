package br.edu.ucsal.mapa_maturidade_financeira.dto;

public class ProgressoResponse {

    private final double percentualConcluido;
    private final int modulosConcluidos;
    private final int totalModulos;
    private final int xp;

    public ProgressoResponse(double percentualConcluido, int modulosConcluidos, int totalModulos, int xp) {
        this.percentualConcluido = percentualConcluido;
        this.modulosConcluidos = modulosConcluidos;
        this.totalModulos = totalModulos;
        this.xp = xp;
    }

    public double getPercentualConcluido() {
        return percentualConcluido;
    }

    public int getModulosConcluidos() {
        return modulosConcluidos;
    }

    public int getTotalModulos() {
        return totalModulos;
    }

    public int getXp() {
        return xp;
    }
}
