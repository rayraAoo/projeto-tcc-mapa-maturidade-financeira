# Mapa da Maturidade Financeira

Trabalho de Conclusão de Curso — Análise e Desenvolvimento de Sistemas — UCSAL

**Autores:** Diego Sousa, Douglas de Jesus, Alice Rayra, Pedro Freitas
**Orientadora:** Prof.ª Karina Leite

Plataforma web educacional de educação financeira para **adolescentes (a partir de 12 anos) e adultos**: diagnóstico de perfil,
trilha de aprendizagem adaptativa, quizzes, simulador de decisões com dinheiro fictício e diagnóstico personalizado por IA.
Finalidade exclusivamente educacional: não recomenda investimentos, não movimenta dinheiro real e não exibe publicidade.

## Estrutura do repositório

```
backend/    API em Java 17 + Spring Boot 4 + PostgreSQL (README próprio, com os endpoints e as regras de negócio)
frontend/   Aplicação React + Vite (README próprio, com o roteiro de teste)
docs/       Avaliação de riscos (ECA Digital) e lista do que atualizar no documento do TCC
scripts/    resetar-banco.ps1 (apaga e recria o banco de desenvolvimento)
```

## Como rodar tudo

Pré-requisitos: **JDK 17 ou superior** (21 recomendado; evite o 27), **Node.js 20.19 ou superior** e **PostgreSQL**.
Use sempre a **pasta raiz do repositório** (a que contém `backend` e `frontend`) aberta no VS Code.

**1. Banco de dados** — crie o banco uma vez:

```sql
CREATE DATABASE mapa_financeira;
```

**2. Backend** (terminal 1):

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Aguarde `Started MapaMaturidadeFinanceiraApplication`. Teste: <http://localhost:8080/api/publico/termos>.
Se o PostgreSQL não usar a senha padrão do projeto: `$env:DB_PASSWORD="sua-senha"; .\mvnw.cmd spring-boot:run`.

**3. Frontend** (terminal 2, com o backend já no ar):

```powershell
cd frontend
npm install
npm run dev
```

Abra <http://localhost:5173>. Os dois terminais precisam ficar abertos.

## Como as partes se conectam

- O front chama a API em `http://localhost:8080` (configurável em `frontend/.env`, variável `VITE_API_URL`).
- O backend só aceita chamadas vindas de `http://localhost:5173` e `http://localhost:3000` (variável `CORS_ALLOWED_ORIGINS`).
- O login devolve um token JWT; o front o guarda e o envia em `Authorization: Bearer <token>` em toda requisição.
- O texto da declaração dos responsáveis e a faixa etária vêm do backend (`/api/publico/termos`).
- Variáveis de ambiente do backend (`DB_PASSWORD`, `JWT_SECRET`, `GEMINI_API_KEY`, `GEMINI_MODEL`...): veja `backend/README.md`.

## Testes

```powershell
cd backend
.\mvnw.cmd test
```

## Trabalho em equipe (Git)

```bash
git pull                      # antes de começar
# ... altere o código ...
git add .
git commit -m "descreva o que mudou"
git push
```

Nunca versione chaves, senhas reais ou o arquivo `.env`. O `.gitignore` da raiz já protege `.env`, `target/` e `node_modules/`.

## Documentação

- `backend/README.md` — endpoints, regras de negócio, segurança e limitações.
- `frontend/README.md` — como rodar o front, roteiro de teste e decisões de interface.
- `docs/AVALIACAO_DE_RISCOS_ECA_DIGITAL.md` — riscos e medidas para o uso por adolescentes.
- `docs/ALTERACOES_PARA_O_TCC.md` — o que atualizar no documento do TCC e no relatório.

## Pendências

- Ampliar o texto dos 10 módulos (hoje têm uma frase cada).
- Conta de responsável e controles parentais (trabalho futuro).
- Testes automatizados do frontend.
