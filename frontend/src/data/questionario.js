// Perguntas do questionário de perfil. O backend recebe só a letra escolhida (A a D) em cada campo.
export const PERGUNTAS = [
  {
    campo: "respostaOrcamento",
    tema: "Controle de gastos",
    enunciado: "Como você descreveria seu controle de gastos mensais?",
    opcoes: [
      { letra: "A", texto: "Não registro nem acompanho meus gastos" },
      { letra: "B", texto: "Tenho uma noção aproximada, mas não registro formalmente" },
      { letra: "C", texto: "Registro meus gastos de forma esporádica" },
      { letra: "D", texto: "Registro e acompanho meus gastos regularmente" },
    ],
  },
  {
    campo: "respostaReservaEmergencia",
    tema: "Reserva de emergência",
    enunciado: "Qual alternativa melhor descreve sua reserva de emergência?",
    opcoes: [
      { letra: "A", texto: "Não possuo reserva de emergência" },
      { letra: "B", texto: "Conheço o conceito, mas ainda não constituí uma reserva" },
      { letra: "C", texto: "Possuo reserva, mas cobre menos de três meses de despesas" },
      { letra: "D", texto: "Possuo reserva equivalente a três meses de despesas ou mais" },
    ],
  },
  {
    campo: "respostaDivida",
    tema: "Dívidas",
    enunciado: "Como está, atualmente, sua situação em relação a dívidas?",
    opcoes: [
      { letra: "A", texto: "Tenho dívidas em atraso ou meu nome está negativado" },
      { letra: "B", texto: "Tenho dívidas em dia, mas que comprometem parte significativa da minha renda" },
      { letra: "C", texto: "Tenho dívidas pontuais e consigo administrá-las sem dificuldade" },
      { letra: "D", texto: "Não possuo dívidas ativas no momento" },
    ],
  },
  {
    campo: "respostaConhecimentoInvestimento",
    tema: "Conhecimento sobre investimentos",
    enunciado: "Como você avalia seu conhecimento sobre conceitos de investimento (renda fixa, renda variável, CDB, ações etc.)?",
    opcoes: [
      { letra: "A", texto: "Não tenho familiaridade com esses conceitos" },
      { letra: "B", texto: "Já ouvi falar, mas não sei diferenciá-los" },
      { letra: "C", texto: "Conheço os conceitos básicos" },
      { letra: "D", texto: "Tenho domínio consolidado sobre esses conceitos" },
    ],
  },
  {
    campo: "respostaExperienciaInvestimento",
    tema: "Experiência prática",
    enunciado: "Qual sua experiência prática com investimentos até o momento?",
    opcoes: [
      { letra: "A", texto: "Nunca investi" },
      { letra: "B", texto: "Já pesquisei sobre o assunto, mas ainda não investi" },
      { letra: "C", texto: "Já investi, mas de forma pontual" },
      { letra: "D", texto: "Invisto regularmente" },
    ],
  },
];

// Texto explicativo de cada perfil (o nome vem do backend; esta frase é só apoio visual).
export const EXPLICACAO_PERFIL = {
  INICIANTE:
    "Você está dando os primeiros passos. Sua trilha começa pela organização financeira básica, que é a base de qualquer decisão futura.",
  EM_DESENVOLVIMENTO:
    "Você já tem alguma base. Sua trilha reforça o essencial e avança para o funcionamento do sistema financeiro.",
  INVESTIDOR:
    "Você já tem experiência prática. Sua trilha aprofunda conceitos, riscos e cuidados, sempre de forma educativa.",
};

export const TITULO_CAMADA = {
  1: "Camada 1 · Educação financeira",
  2: "Camada 2 · Sistema financeiro",
  3: "Camada 3 · Investimentos (conceitos)",
};

export const NOME_CATEGORIA = {
  ORCAMENTO: "Orçamento",
  RESERVA: "Reserva",
  CONSUMO_CONSCIENTE: "Consumo consciente",
};
