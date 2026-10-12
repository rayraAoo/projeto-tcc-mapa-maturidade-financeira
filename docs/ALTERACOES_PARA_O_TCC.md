# O que atualizar no documento do TCC e no relatório

Lista do que mudou no sistema e precisa constar na documentação, com sugestão de texto.

## Público (introdução, objetivos e escopo)

- Trocar "maiores de 18 anos" por **"adolescentes (a partir de 12 anos) e adultos"**.
- **Escopo 4.4:** trocar "não aceita usuários menores de 18 anos" por: *"A plataforma é indicada para pessoas com 12 anos ou mais.
  Menores de 18 anos devem declarar que seus pais ou responsáveis estão cientes do uso; a declaração é autodeclarada e não é verificada."*
- **Seção 3 (trabalhos relacionados):** o diferencial deixa de ser "público adulto". Reescrever como a **combinação de IA, trilha
  adaptativa e simulação de decisões em uma única plataforma**, com transparência sobre o uso de IA. Reconhecer a proximidade com
  Fimpact, TinDin e Tangram (público escolar).

## Requisitos

- **RF01 (novo texto):** *O sistema deve permitir o cadastro de usuários com 12 anos ou mais; para menores de 18 anos, deve exigir a
  declaração de ciência dos pais ou responsáveis e registrá-la (data, hora e versão do texto).*
- **Novo RF:** *O sistema deve exibir publicamente a faixa etária indicada e informações aos pais e responsáveis.*
- **Novo RF:** *O diagnóstico por IA só deve ser gerado mediante autorização explícita do usuário a cada uso e sem enviar nome ou e-mail.*
- **RF10:** acrescentar *"com texto de contingência por regras quando o serviço de IA estiver indisponível"*.
- **RF11:** *progresso em percentual e XP, sendo o XP concedido por aprendizado (módulos, acertos e notas do simulador), sem recompensa por tempo de uso.*
- **RNF06:** manter e acrescentar que os textos estáticos foram revisados para não indicar produtos.
- **Novo RNF:** *Segredos (chaves de API, JWT e banco) devem ser fornecidos por variáveis de ambiente, nunca versionados.*

## Texto técnico

- **Stack:** Spring Boot **4.1** (não 3).
- **Entidades (6.2):** além das já descritas, incluir `Pergunta`, `RespostaUsuario`, `CenarioSimulacao`, `OpcaoSimulacao` e
  `HistoricoSimulacao` (em lugar de `SimulacaoDecisao`).
- **Perfis:** nomes `Iniciante`, `Em desenvolvimento` e `Investidor`, e a regra de classificação por experiência prática e base financeira.
- **LGPD:** trocar "direito ao esquecimento" por **"eliminação dos dados pessoais" (LGPD, art. 18, VI)**.
- **Conformidade:** substituir "estrita conformidade com a LGPD" por *"medidas alinhadas aos princípios da LGPD e do ECA Digital"*,
  seguido das limitações de `docs/AVALIACAO_DE_RISCOS_ECA_DIGITAL.md`.
- **Relatório:** o CORS passou de "qualquer origem" para origens configuradas; o tratamento de erros agora é de fato centralizado.

## Conteúdo dos módulos (pendente)

O RF05 prevê "texto explicativo escrito pela equipe". Hoje cada módulo tem cerca de 20 palavras e as perguntas do quiz tratam de
pontos que o texto não ensina. Ampliar os 10 textos (e incluir os temas que o TCC 4.1 promete e o seed ainda não cobre:
CVM, abertura de conta, tributação, banco x corretora e golpes/pirâmide como módulo).
