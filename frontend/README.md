# Frontend — Mapa da Maturidade Financeira

React + Vite. Conversa com o backend Spring Boot (`http://localhost:8080`). Estilo em CSS simples, sem framework.

## Como rodar

Pré-requisito: Node.js 20.19 ou superior (`node -v`) e o **backend rodando** (veja o README da raiz do repositório).

```bash
cd frontend
npm install
npm run dev
```

Abra **http://localhost:5173** e deixe o terminal aberto: o front só funciona enquanto o comando estiver rodando.
Se a porta 5173 estiver ocupada, o Vite avisa com erro (de propósito, para não gerar erro de CORS): feche o que está usando a porta.

> Se o `npm install` reclamar de versões, apague `package.json`, crie um projeto novo com
> `npm create vite@latest teste -- --template react`, copie o `package.json` dele para cá e rode
> `npm install axios react-router-dom`.

## Roteiro de teste (do começo ao fim)

1. **Cadastro de adulto** (18 anos ou mais): não aparece declaração. Você entra e cai no questionário.
2. **Cadastro de adolescente** (12 a 17 anos): aparece a declaração de ciência dos responsáveis, obrigatória.
3. **Menor de 12 anos:** a tela avisa e bloqueia o botão.
4. **Questionário** (5 perguntas) → mostra o perfil → **Trilha**.
5. Abra um **módulo**, faça o **quiz** (a explicação aparece após cada resposta) e conclua. Percentual e XP sobem na trilha.
6. **Simulador:** escolha um cenário, decida e veja o resultado.
7. **Diagnóstico:** marque a autorização e gere. Sem chave do Gemini, o texto vem "por regras (sem IA)", e isso está certo.
8. **Perfil:** sair da conta e, por último, excluir a conta (digitar EXCLUIR).

## Problemas comuns

| Sintoma | Causa provável |
|---|---|
| "Não foi possível conectar ao servidor" | Backend desligado, ou front fora de `localhost:5173`. |
| Volta para o login do nada | O backend foi reiniciado sem `JWT_SECRET`: os tokens antigos deixam de valer. Entre de novo. |
| Trilha ou simulador vazios | O banco não foi populado. Recrie o banco (`scripts/resetar-banco.ps1`) e suba o backend. |
| Cadastro recusa a senha | A senha precisa de pelo menos 8 caracteres. |

Para apontar para outro backend, copie `.env.example` para `.env` e altere `VITE_API_URL`.

## Estrutura

```
src/
  api/          http.js (axios + token) e servicos.js (uma função por endpoint)
  auth/         sessão no navegador e contexto de login
  components/   Layout, navegação inferior, rota protegida, mensagens
  data/         perguntas do questionário e textos fixos
  hooks/        useDados (carrega dados e controla "carregando" e "erro")
  pages/        uma tela por arquivo
  utils/        idade, formatação, mensagens de erro
```

## Decisões de projeto

- **O simulador não mostra o valor de cada opção antes da decisão:** na vida real não se conhece o resultado antes de decidir.
- **Navegação inferior:** Trilha, Simulador, Diagnóstico e Perfil. O quiz fica dentro de cada módulo.
- **O texto da declaração dos responsáveis e a faixa etária vêm do backend** (`/api/publico/termos`).
- **Token no `localStorage`:** simples, mas exposto a ataques XSS. Registrado como limitação; o ideal para produção seria cookie HttpOnly.
