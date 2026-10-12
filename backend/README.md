# Mapa da Maturidade Financeira

Trabalho de Conclusão de Curso — Análise e Desenvolvimento de Sistemas — UCSAL

**Autores:** Diego Sousa, Douglas de Jesus, Alice Rayra, Pedro Freitas
**Orientadora:** Prof.ª Karina Leite

## Sobre o projeto

Plataforma web educacional de educação financeira para **adolescentes (a partir de 12 anos) e adultos**, com:

- **diagnóstico de perfil** por questionário (Iniciante, Em desenvolvimento ou Investidor);
- **trilha de aprendizagem** em 3 camadas, filtrada pelo perfil;
- **quizzes** com correção e explicação imediatas;
- **simulador de decisões** com dinheiro fictício;
- **diagnóstico personalizado** gerado por IA (Gemini), sempre com autorização do usuário.

Finalidade exclusivamente educacional: **não** recomenda produtos ou investimentos, **não** movimenta dinheiro real,
**não** integra corretoras e **não** exibe publicidade.

## Público e responsáveis

| Idade | Cadastro |
|---|---|
| Menos de 12 anos | Não permitido (a plataforma é indicada para 12 anos ou mais) |
| 12 a 17 anos | Permitido com a **declaração de que os pais ou responsáveis estão cientes** |
| 18 anos ou mais | Permitido, sem declaração |

A declaração é **autodeclarada e não é verificada** pela plataforma. Ela fica registrada (data, hora e versão do texto).
O texto exibido, a faixa etária indicada e as orientações aos responsáveis são servidos publicamente em
`GET /api/publico/termos`, para que o frontend use sempre a mesma versão.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Frontend | React + Vite (pasta `../frontend`) |
| Backend | Java 17 + Spring Boot 4.1 (Web, Data JPA, Security, Validation) |
| Banco de dados | PostgreSQL |
| Autenticação | JWT (sem sessão no servidor) |
| IA | API do Google Gemini |
| Versionamento | Git + GitHub |

## Como rodar

1. Crie o banco: `CREATE DATABASE mapa_financeira;`
2. Defina as variáveis de ambiente (todas opcionais para desenvolvimento local):

| Variável | Para quê | Padrão |
|---|---|---|
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | Conexão com o PostgreSQL | `localhost:5432/mapa_financeira`, `postgres`, `senha123` |
| `JWT_SECRET` | Chave de assinatura do token (mínimo 32 caracteres) | vazio = chave aleatória a cada execução |
| `GEMINI_API_KEY` | Chave do Gemini | vazio = diagnóstico de contingência (sem IA) |
| `GEMINI_MODEL` | Modelo do Gemini | `gemini-3.5-flash` |
| `CORS_ALLOWED_ORIGINS` | Origens do frontend, separadas por vírgula | `http://localhost:5173,http://localhost:3000` |

   > **Nunca** escreva chaves em arquivos versionados. No PowerShell: `$env:GEMINI_API_KEY="sua-chave"`.
   > Os modelos do Gemini são aposentados com frequência: confira o nome atual em
   > <https://ai.google.dev/gemini-api/docs/models> e teste com uma chamada real antes de apresentar.

3. Dentro da pasta `backend`, rode `.\mvnw.cmd spring-boot:run` (ou o `MapaMaturidadeFinanceiraApplication` pelo VS Code).
4. Na primeira execução com o banco vazio, o `DataInitializer` cria 10 módulos, 20 perguntas e 4 cenários.

> Se o seu banco já existia de uma versão anterior, **recrie-o** (`DROP DATABASE mapa_financeira; CREATE DATABASE mapa_financeira;`):
> o seed só roda com o banco vazio, e os cenários e perguntas foram corrigidos.

## Endpoints

**Públicos**

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/publico/termos` | Faixa etária indicada, texto de ciência e informações aos responsáveis |
| POST | `/api/usuarios/cadastro` | Cadastro (regras de idade acima) |
| POST | `/api/usuarios/login` | Login; devolve o token JWT |

**Exigem token** (`Authorization: Bearer <token>`)

| Método | Rota | Descrição |
|---|---|---|
| DELETE | `/api/usuarios/minha-conta` | Exclui a conta e todos os dados associados |
| GET / POST | `/api/perfil/atual`, `/api/perfil/questionario` | Perfil e questionário (alternativas A a D) |
| GET | `/api/modulos/trilha` | Módulos da trilha do usuário |
| POST | `/api/modulos/concluir` | Conclui um módulo **da própria trilha** |
| GET | `/api/modulos/concluidos` | Ids dos módulos da trilha já concluídos |
| GET | `/api/modulos/progresso` | Percentual, módulos concluídos e XP |
| GET | `/api/quiz/modulo/{id}` | Perguntas do módulo, **sem o gabarito** |
| POST | `/api/quiz/responder` | Corrige e devolve a explicação |
| GET / POST | `/api/simulador/cenarios`, `/cenarios/{id}`, `/executar`, `/historico` | Simulador de decisões |
| POST | `/api/ia/diagnostico/gerar` | Gera o diagnóstico; exige `{"autorizaEnvioIa": true}` |
| GET | `/api/ia/diagnostico/ultimo`, `/historico` | Diagnósticos já gerados |

Exemplo de cadastro de adolescente:

```json
{
  "nome": "Maria Souza",
  "email": "maria@exemplo.com",
  "senha": "umaSenhaForte1",
  "dataNascimento": "2010-04-12",
  "confirmaCienciaResponsavel": true
}
```

Todos os erros seguem o mesmo formato: `{ "timestamp", "status", "mensagem" }` (e `"erros"` na validação).

## Regras de negócio

- **Perfis:** quem já investe (respostas C ou D na experiência) é *Investidor*; sem experiência, a base financeira
  (orçamento + reserva + dívida) decide entre *Iniciante* (base de 0 a 3) e *Em desenvolvimento*.
  O *Investidor* com base fraca recebe também os módulos da camada 1 para reforçá-la.
- **Trilha:** Iniciante 3/2/1 módulos (camadas 1/2/3); Em desenvolvimento 1/2/3; Investidor 0/1/5.
- **XP:** 20 por módulo concluído, 10 por pergunta acertada (conta uma vez) e a melhor nota (0 a 10) de cada cenário.
  Recompensa **aprendizado**, nunca tempo de uso nem sequência de acessos.
- **Quiz:** guarda apenas a tentativa mais recente de cada pergunta.
- **IA:** o prompt **não contém nome nem e-mail**. Se o Gemini estiver indisponível, é gerado um texto de contingência por
  regras, marcado como "sem IA". Há intervalo mínimo de 30 segundos entre diagnósticos.

## Segurança

Senhas com BCrypt; JWT stateless com segredo vindo do ambiente; hash de senha e gabarito do quiz nunca saem pela API;
validação de entrada em todos os formulários; erros inesperados não expõem detalhes ao cliente; CORS restrito às origens
configuradas; chave do Gemini enviada em cabeçalho (não na URL) e com tempo limite de conexão.

## Testes

```
.\mvnw.cmd test
```

Cobrem os serviços (cadastro por idade, trilha, progresso/XP, quiz, perfil, simulador, diagnóstico) e garantem que
as respostas não voltem a expor hash de senha, gabarito ou detalhes de erro.

## Limitações conhecidas

- A declaração de ciência dos responsáveis é autodeclarada; não há conta de responsável nem controles parentais.
- O conteúdo textual dos módulos ainda é resumido e precisa ser ampliado pela equipe.
- Não há limite de tentativas de login nem verificação de e-mail.
- No plano gratuito do Gemini, o conteúdo enviado pode ser usado pelo provedor para melhorar seus produtos.

Veja também `../docs/AVALIACAO_DE_RISCOS_ECA_DIGITAL.md` e `../docs/ALTERACOES_PARA_O_TCC.md`.

## Progresso

- [x] RF01 — Cadastro com idade mínima de 12 anos e declaração de ciência para menores de 18
- [x] RF02 — Login com JWT
- [x] RF03/RF04 — Questionário e perfil
- [x] RF05/RF06 — Trilha por perfil e conclusão de módulos
- [x] RF07 — Quiz com correção e feedback
- [x] RF08/RF09 — Simulador de decisões com dinheiro fictício
- [x] RF10 — Diagnóstico por IA, com contingência
- [x] RF11 — Progresso em percentual e XP
- [x] RF12 — Exclusão de conta
- [ ] Ampliar o texto dos módulos
- [x] Frontend (React) — pasta `../frontend`

## Escopo — o que o sistema não faz

- Não recomenda produtos, ativos ou fundos de investimento.
- Não movimenta dinheiro real nem integra corretoras ou instituições financeiras.
- Não coleta dado financeiro real (CPF, número de conta, saldo bancário).
- Não exibe publicidade.
