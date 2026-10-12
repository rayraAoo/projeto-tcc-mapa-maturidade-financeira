package br.edu.ucsal.mapa_maturidade_financeira.config;

import br.edu.ucsal.mapa_maturidade_financeira.model.CenarioSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.Modulo;
import br.edu.ucsal.mapa_maturidade_financeira.model.OpcaoSimulacao;
import br.edu.ucsal.mapa_maturidade_financeira.model.Pergunta;
import br.edu.ucsal.mapa_maturidade_financeira.repository.CenarioSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.ModuloRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.OpcaoSimulacaoRepository;
import br.edu.ucsal.mapa_maturidade_financeira.repository.PerguntaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ModuloRepository moduloRepository;
    private final PerguntaRepository perguntaRepository;
    private final CenarioSimulacaoRepository cenarioRepository;
    private final OpcaoSimulacaoRepository opcaoRepository;

    public DataInitializer(
            ModuloRepository moduloRepository,
            PerguntaRepository perguntaRepository,
            CenarioSimulacaoRepository cenarioRepository,
            OpcaoSimulacaoRepository opcaoRepository
    ) {
        this.moduloRepository = moduloRepository;
        this.perguntaRepository = perguntaRepository;
        this.cenarioRepository = cenarioRepository;
        this.opcaoRepository = opcaoRepository;
    }

    @Override
    public void run(String... args) {
        inicializarModulosEQuizzes();
        inicializarCenariosSimulador();
    }

    private void inicializarModulosEQuizzes() {
        if (moduloRepository.count() > 0) {
            return;
        }

        // Camada 1: Educação Financeira Geral
        Modulo m1 = moduloRepository.save(new Modulo(
                "Consciência Financeira e Orçamento Básico",
                "Compreender a diferença entre receitas e despesas, necessidades e desejos, e como montar um orçamento pelo método 50-30-20.",
                1, 1
        ));
        criarPergunta(m1,
                "Qual é a primeira etapa para começar a organizar as finanças pessoais?",
                "Contratar um empréstimo para quitar dívidas",
                "Mapear todas as receitas e despesas mensais para saber o destino do dinheiro",
                "Investir em ações de alto risco",
                "Comprar bens de luxo para revender",
                "B",
                "O mapeamento de despesas e receitas permite identificar gargalos e planejar gastos conscientes.");
        criarPergunta(m1,
                "Na regra de orçamento 50-30-20, qual fatia deve ser destinada às despesas essenciais/necessidades?",
                "50% da renda líquida",
                "30% da renda líquida",
                "20% da renda líquida",
                "100% da renda líquida",
                "A",
                "O modelo 50-30-20 sugere 50% para necessidades, 30% para desejos pessoais e 20% para poupança/investimentos.");

        Modulo m2 = moduloRepository.save(new Modulo(
                "Reserva de Emergência e Planejamento",
                "Importância de manter um valor acessível para imprevistos (saúde, reparos, perda de renda) com liquidez imediata e segurança.",
                1, 2
        ));
        criarPergunta(m2,
                "Qual característica é fundamental para o dinheiro da reserva de emergência?",
                "Alto risco e rentabilidade máxima",
                "Prazo de carência de vários anos",
                "Alta liquidez (resgate rápido) e baixíssimo risco de perda",
                "Ser aplicado em criptomoedas voláteis",
                "C",
                "A reserva de emergência precisa estar disponível no momento exato em que o imprevisto ocorrer.");
        criarPergunta(m2,
                "Quantos meses de custo de vida costumam ser recomendados para uma reserva de emergência?",
                "1 a 2 semanas",
                "Entre 3 e 6 meses de gastos fixos essenciais",
                "20 anos de despesas",
                "Não é necessário ter nenhum mês reservado",
                "B",
                "Entre 3 e 6 meses de despesas básicas garante estabilidade durante imprevistos ou transição profissional.");

        Modulo m3 = moduloRepository.save(new Modulo(
                "Controle de Gastos e Armadilhas do Crédito",
                "Entendendo juros rotativos de cartão, cheque especial, compras parceladas por impulso e como evitar o endividamento.",
                1, 3
        ));
        criarPergunta(m3,
                "O que acontece ao pagar apenas o valor mínimo da fatura do cartão de crédito?",
                "O restante é perdoado pelo banco",
                "Entra-se no rotativo do cartão, incidindo uma das maiores taxas de juros do mercado",
                "O limite do cartão aumenta automaticamente",
                "Gera rendimento de cashback adicional",
                "B",
                "O crédito rotativo possui juros altíssimos e pode dobrar o valor da dívida em pouco tempo.");
        criarPergunta(m3,
                "Qual é uma atitude saudável ao utilizar o cartão de crédito?",
                "Considerar o limite como uma extensão do salário",
                "Pagar sempre o valor integral da fatura até a data de vencimento",
                "Atrasar o pagamento para priorizar gastos supérfluos",
                "Ter vários cartões com limites altos sem acompanhamento",
                "B",
                "Pagar o valor total da fatura evita cobrança de juros e multas de atraso.");

        // Camada 2: Sistema Financeiro
        Modulo m4 = moduloRepository.save(new Modulo(
                "Como Funcionam os Bancos e Taxas de Juros",
                "Entenda o papel das instituições financeiras, o spread bancário, a remuneração de capital e como os juros compostos agem a seu favor ou contra você.",
                2, 1
        ));
        criarPergunta(m4,
                "Quando os juros compostos trabalham contra o indivíduo?",
                "Quando ele investe regularmente na poupança",
                "Quando ele acumula dívidas não pagas que crescem exponencialmente",
                "Quando ele quita suas compras à vista",
                "Quando ele recebe o salário antecipado",
                "B",
                "Nas dívidas, os juros incidem sobre os juros acumulados, aumentando a dívida rapidamente.");
        criarPergunta(m4,
                "O que representa o spread bancário?",
                "A diferença entre a taxa que o banco paga ao captar recursos e a taxa cobrada nos empréstimos",
                "O limite do cheque especial concedido",
                "O imposto sobre operações financeiras (IOF)",
                "O valor da tarifa de anuidade do cartão",
                "A",
                "Spread bancário é a margem de intermediação entre custo de captação e taxa de empréstimo.");

        Modulo m5 = moduloRepository.save(new Modulo(
                "Inflação, Poder de Compra e Taxa Selic",
                "Como a inflação (IPCA) corrói o poder de compra e por que a taxa básica de juros (Selic) é utilizada como instrumento de política monetária.",
                2, 2
        ));
        criarPergunta(m5,
                "Qual é o impacto da inflação no poder de compra ao longo do tempo?",
                "O dinheiro ganha mais valor de compra sem esforço",
                "Reduz o poder de compra, exigindo mais dinheiro para comprar os mesmos bens e serviços",
                "Zera automaticamente as dívidas bancárias",
                "Diminui os preços dos produtos no supermercado",
                "B",
                "A inflação é o aumento generalizado de preços, o que corrói o valor real do dinheiro.");
        criarPergunta(m5,
                "Qual é a taxa básica de juros da economia brasileira definida pelo Copom?",
                "Taxa CDI",
                "Taxa Selic",
                "Taxa IPCA",
                "Taxa Ibovespa",
                "B",
                "A Selic é a taxa básica de juros estipulada pelo Comitê de Política Monetária (Copom) do Banco Central.");

        // Camada 3: Investimentos e Futuro
        Modulo m6 = moduloRepository.save(new Modulo(
                "Fundamentos de Renda Fixa (Tesouro Direto e CDBs)",
                "Conceito de emprestar dinheiro para governos ou bancos em troca de rentabilidade pré-fixada, pós-fixada ou híbrida com proteção do FGC.",
                3, 1
        ));
        criarPergunta(m6,
                "Qual órgão ou mecanismo protege aplicações em CDBs até o limite regulamentar em caso de quebra da instituição?",
                "Bolsa de Valores (B3)",
                "Fundo Garantidor de Créditos (FGC)",
                "Receita Federal",
                "Procon",
                "B",
                "O FGC garante depósitos e investimentos elegíveis como CDBs e LCIs até o teto estabelecido por CPF e instituição.");
        criarPergunta(m6,
                "Em renda fixa, o que significa dizer que a rentabilidade é pós-fixada?",
                "O retorno é conhecido na data da compra, em porcentagem fixa",
                "O retorno acompanha um indicador de mercado, como a taxa Selic ou o CDI",
                "Os juros só são pagos no último dia do ano",
                "O valor aplicado é garantido pelo governo em qualquer situação",
                "B",
                "Na rentabilidade pós-fixada o ganho varia conforme um indicador de referência (como a Selic ou o CDI). Na pré-fixada, a taxa é combinada no momento da contratação.");

        Modulo m7 = moduloRepository.save(new Modulo(
                "Fundos de Investimento e Relação Risco x Retorno",
                "Como funcionam condomínios de investidores geridos por profissionais, taxas de administração, comer-cotas e diversificação de ativos.",
                3, 2
        ));
        criarPergunta(m7,
                "O que diz o princípio da relação Risco versus Retorno?",
                "Investimentos sem nenhum risco oferecem sempre os maiores retornos possíveis",
                "Para buscar potenciais retornos mais elevados, geralmente é necessário aceitar maiores riscos e oscilações",
                "Quanto maior o risco, menor é a possibilidade de perda",
                "Todos os investimentos oferecem retornos garantidos pelo governo",
                "B",
                "No mercado financeiro, maior rentabilidade potencial exige tolerância a riscos e volatilidade proporcionais.");
        criarPergunta(m7,
                "O que é a taxa de administração cobrada em um fundo de investimento?",
                "Um imposto obrigatório repassado à Prefeitura",
                "Uma taxa percentual anual destinada à remuneração dos gestores e administração do fundo",
                "Uma penalidade por resgate antecipado",
                "Um prêmio pago ao investidor com melhor pontuação",
                "B",
                "A taxa remunera a equipe profissional responsável pela gestão e custódia da carteira do fundo.");

        Modulo m8 = moduloRepository.save(new Modulo(
                "Introdução à Renda Variável e Diversificação",
                "Ações, fundos imobiliários, volatilidade de mercado e a regra de ouro de não colocar todos os ovos na mesma cesta.",
                3, 3
        ));
        criarPergunta(m8,
                "Por que a diversificação é considerada essencial nos investimentos?",
                "Porque garante 100% de lucro em todas as escolhas",
                "Porque dilui os riscos distribuindo o capital entre diferentes classes de ativos e setores",
                "Porque elimina a necessidade de pagar impostos",
                "Porque permite comprar ativos sem cadastro em corretora",
                "B",
                "Diversificar impede que a perda em um único ativo comprometa todo o patrimônio do investidor.");
        criarPergunta(m8,
                "O que acontece com o preço das ações no curto prazo?",
                "É fixo e determinado pelo Banco Central",
                "Oscila de acordo com oferta, demanda, expectativas de mercado e resultados das empresas",
                "Sempre sobe diariamente sem quedas",
                "Só muda uma vez a cada dez anos",
                "B",
                "Ações são ativos de renda variável sujeitos à volatilidade do mercado.");

        Modulo m9 = moduloRepository.save(new Modulo(
                "Planejamento Previdenciário e Longo Prazo",
                "Previdência pública vs privada (PGBL e VGBL), o efeito do tempo nos juros compostos e construção de patrimônio para o futuro.",
                3, 4
        ));
        criarPergunta(m9,
                "Qual fator tem o maior efeito multiplicador na construção de patrimônio através de juros compostos?",
                "O tempo de permanência e constância nos aportes",
                "Tentar acertar o topo diário da bolsa de valores",
                "Trocar de aplicação financeira toda semana",
                "Investir apenas quando houver sobra esporádica de dinheiro",
                "A",
                "O tempo exponencializa os juros compostos, transformando aportes regulares em patrimônio substancial no longo prazo.");
        criarPergunta(m9,
                "Qual é o foco principal da previdência e investimentos de longo prazo?",
                "Gastar tudo no próximo final de semana",
                "Garantir estabilidade e renda sustentável na fase de aposentadoria ou maturidade",
                "Ganhar dinheiro rápido para ostentação imediata",
                "Comprar bens de consumo perecíveis",
                "B",
                "O planejamento de longo prazo visa segurança financeira na terceira idade.");

        Modulo m10 = moduloRepository.save(new Modulo(
                "Psicologia Financeira e Tomada de Decisão",
                "Vieses cognitivos, efeito manada, FOMO (medo de ficar de fora) e como a disciplina emocional protege contra perdas financeiras.",
                3, 5
        ));
        criarPergunta(m10,
                "O que é o viés comportamental conhecido como 'efeito manada' nas finanças?",
                "Comprar um ativo apenas porque todos ao redor estão comprando, sem analisar fundamentos",
                "Estudar minuciosamente os relatórios contábeis de uma empresa antes de investir",
                "Manter um orçamento estritamente controlado em planilha",
                "Recusar ofertas de empréstimos sem necessidade",
                "A",
                "O efeito manada leva investidores a seguirem a multidão por impulso ou medo de ficar de fora (FOMO).");
        criarPergunta(m10,
                "Como a disciplina emocional auxilia o investidor?",
                "Evita decisões precipitadas durante momentos de pânico ou euforia do mercado",
                "Garante que o investidor nunca precise trabalhar",
                "Faz com que as empresas nunca tenham prejuízo",
                "Elimina a inflação do país",
                "A",
                "Manter a calma e focar na estratégia definida protege o patrimônio contra decisões irracionais.");
    }

    // Contador usado para distribuir a alternativa correta de forma uniforme (A, B, C, D, A, B...),
    // evitando que o gabarito seja previsível.
    private int perguntasCriadas = 0;

    private void criarPergunta(Modulo modulo, String enunciado, String a, String b, String c, String d, String correta, String explicacao) {
        List<String> alternativas = new ArrayList<>(List.of(a, b, c, d));
        int posicaoOriginal = "ABCD".indexOf(correta.toUpperCase());
        String textoCorreto = alternativas.remove(posicaoOriginal);

        int novaPosicao = perguntasCriadas % 4;
        perguntasCriadas++;
        alternativas.add(novaPosicao, textoCorreto);

        perguntaRepository.save(new Pergunta(
                modulo, enunciado,
                alternativas.get(0), alternativas.get(1), alternativas.get(2), alternativas.get(3),
                String.valueOf("ABCD".charAt(novaPosicao)),
                explicacao
        ));
    }

    private void inicializarCenariosSimulador() {
        if (cenarioRepository.count() > 0) {
            return;
        }

        // Cenário 1
        CenarioSimulacao c1 = cenarioRepository.save(new CenarioSimulacao(
                "Recebimento da Primeira Bolsa de Estágio / Salário",
                "Você acaba de receber sua primeira bolsa de estágio no valor fictício de R$ 1.200,00. É o início da sua jornada e você precisa decidir o que fazer com esse dinheiro.",
                1200.0,
                "ORCAMENTO"
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c1,
                "Gastar todo o valor em compras por impulso, roupas e festas no primeiro final de semana.",
                -1200.0,
                2,
                "Cuidado! Gastar tudo no primeiro momento sem reservar nada compromete o restante do mês e não cria hábitos saudáveis."
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c1,
                "Separar 20% (R$ 240,00) para iniciar sua reserva de emergência, cobrir suas despesas essenciais e reservar uma quantia controlada para lazer.",
                -960.0,
                10,
                "Excelente escolha! Equilibrar despesas, construir sua reserva e permitir um lazer planejado é o alicerce da maturidade financeira."
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c1,
                "Guardar 100% do dinheiro e deixar de pagar compromissos essenciais combinados.",
                0.0,
                4,
                "Poupar é ótimo, mas honrar suas responsabilidades e necessidades básicas com equilíbrio é fundamental."
        ));

        // Cenário 2
        CenarioSimulacao c2 = cenarioRepository.save(new CenarioSimulacao(
                "Imprevisto do Cotidiano: Celular Quebrado",
                "Seu smartphone de trabalho/estudo quebrou. Você tem R$ 800,00 disponíveis em sua conta de reserva e precisa de uma solução para continuar suas atividades.",
                800.0,
                "RESERVA"
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c2,
                "Comprar o modelo mais recente parcelado em 24 vezes com juros altos no cartão de crédito de terceiros.",
                -800.0,
                2,
                "Parcelamentos longos com juros comprometem sua renda futura e encarecem drasticamente o produto por vaidade momentânea."
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c2,
                "Utilizar parte da sua reserva (R$ 250,00) para consertar a tela e continuar usando o aparelho atual.",
                -250.0,
                10,
                "Muito bem! A reserva de emergência serve exatamente para suprir imprevistos funcionais sem gerar novas dívidas."
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c2,
                "Recorrer a um empréstimo com agiota ou crédito informal com juros abusivos.",
                -800.0,
                0,
                "Perigo extremo! Crédito sem regulação expõe você a cobranças abusivas, riscos patrimoniais e descontrole total."
        ));

        // Cenário 3
        CenarioSimulacao c3 = cenarioRepository.save(new CenarioSimulacao(
                "Promessa de Lucro Fácil na Internet",
                "Um anúncio nas redes sociais promete 'lucro garantido de 15% ao dia' em um aplicativo desconhecido. Você tem R$ 500,00 guardados e pondera o que fazer.",
                500.0,
                "CONSUMO_CONSCIENTE"
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c3,
                "Aplicar todo o dinheiro imediatamente na plataforma acreditando na promessa de enriquecimento rápido.",
                -500.0,
                0,
                "Atenção máxima a golpes! Nenhuma aplicação legítima no mundo garante rentabilidades exorbitantes sem risco. Desconfie sempre!"
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c3,
                "Desconfiar da proposta, não aplicar o dinheiro e verificar se a empresa é autorizada pela CVM ou pelo Banco Central antes de qualquer decisão.",
                0.0,
                10,
                "Parabéns pelo senso crítico! Desconfiar de promessas de lucro garantido e conferir se a empresa é autorizada por órgãos reguladores protege o seu dinheiro."
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c3,
                "Pedir dinheiro emprestado a amigos para colocar um valor ainda maior na plataforma suspeita.",
                -1000.0,
                0,
                "Nunca pegue empréstimos para aplicar em apostas ou esquemas suspeitos. Isso multiplica prejuízos e quebra relações de confiança."
        ));

        // Cenário 4
        CenarioSimulacao c4 = cenarioRepository.save(new CenarioSimulacao(
                "Planejando um Objetivo de Médio Prazo",
                "Você tem o sonho de fazer um curso técnico de especialização que custa R$ 1.000,00 e sua renda mensal líquida fictícia é de R$ 1.500,00.",
                1500.0,
                "ORCAMENTO"
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c4,
                "Separar R$ 250,00 por mês do orçamento, durante 4 meses, até juntar o valor total do curso.",
                -250.0,
                10,
                "Excelente planejamento! Juntar o valor antes de comprar dá previsibilidade ao orçamento e costuma evitar o pagamento de juros de financiamento."
        ));
        opcaoRepository.save(new OpcaoSimulacao(
                c4,
                "Fazer uma viagem impulsiva no feriado e adiar o curso por tempo indeterminado.",
                -1500.0,
                3,
                "Lazer é importante, mas priorizar impulsos em detrimento da sua qualificação profissional atrasa o crescimento da sua renda futura."
        ));
    }
}
