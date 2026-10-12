package br.edu.ucsal.mapa_maturidade_financeira.config;

import java.util.List;

/**
 * Regras de público e textos exibidos ao usuário e aos responsáveis.
 * Centralizados aqui para que o backend e o frontend usem sempre a mesma versão.
 */
public final class PoliticaPlataforma {

    private PoliticaPlataforma() {
    }

    /** Idade mínima indicada: a plataforma é voltada a adolescentes (12+) e adultos. */
    public static final int IDADE_MINIMA = 12;

    /** Abaixo disso, o cadastro exige a declaração de ciência dos responsáveis. */
    public static final int MAIORIDADE = 18;

    public static final String FAIXA_ETARIA_INDICADA = "12 anos ou mais (adolescentes e adultos)";

    /** Mude a versão sempre que o texto abaixo for alterado. */
    public static final String VERSAO_TERMO_CIENCIA = "2026-10-v1";

    public static final String TEXTO_CIENCIA =
            "Declaro que sou menor de 18 anos e que meus pais ou responsáveis legais estão cientes "
            + "de que utilizo esta plataforma educacional, de que meus dados de cadastro e de aprendizagem "
            + "são tratados para essa finalidade e de que, se eu autorizar, dados de aprendizagem "
            + "(sem nome ou e-mail) poderão ser enviados a um serviço de inteligência artificial de terceiros.";

    public static final List<String> INFORMACOES_RESPONSAVEIS = List.of(
            "O que é: plataforma educacional de educação financeira, com conteúdos, quizzes e simulações "
                    + "com dinheiro fictício. Não vende produtos, não exibe publicidade e não movimenta dinheiro real.",
            "Dados coletados: nome, e-mail, data de nascimento, respostas do questionário de perfil, progresso, "
                    + "respostas de quiz e decisões nas simulações. Não são coletados CPF, dados bancários nem localização.",
            "Finalidade: personalizar a trilha de estudo e acompanhar o aprendizado. Os dados não são vendidos "
                    + "nem usados para publicidade.",
            "Inteligência artificial: o diagnóstico personalizado só é gerado quando o usuário autoriza, a cada uso. "
                    + "São enviados dados de aprendizagem sem nome nem e-mail. Dependendo do plano do serviço de IA "
                    + "utilizado, o provedor pode usar o conteúdo enviado para melhorar seus produtos.",
            "Declaração de ciência: para menores de 18 anos, o cadastro exige a declaração de que os responsáveis "
                    + "estão cientes. Essa declaração é feita pelo próprio usuário e não é verificada pela plataforma.",
            "Exclusão de dados: o usuário pode excluir a conta e todos os dados associados a qualquer momento.",
            "Conteúdo educacional: nada na plataforma constitui recomendação de investimento.",
            "Supervisão parental: a plataforma ainda não oferece contas de responsável nem controles parentais. "
                    + "Recomendamos acompanhar o uso junto ao adolescente."
    );
}
