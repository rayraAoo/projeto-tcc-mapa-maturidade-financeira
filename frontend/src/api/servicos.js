import http from "./http.js";

// Uma função por endpoint do backend. As telas só chamam estas funções.

// ---- Público ----
export const obterTermos = () => http.get("/api/publico/termos").then((r) => r.data);
export const cadastrar = (dados) => http.post("/api/usuarios/cadastro", dados).then((r) => r.data);
export const entrarNaConta = (email, senha) =>
  http.post("/api/usuarios/login", { email, senha }).then((r) => r.data);

// ---- Conta ----
export const excluirConta = () => http.delete("/api/usuarios/minha-conta").then((r) => r.data);

// ---- Perfil ----
export async function obterPerfilAtual() {
  const resposta = await http.get("/api/perfil/atual");
  return resposta.status === 204 ? null : resposta.data; // 204 = ainda não respondeu o questionário
}
export const enviarQuestionario = (respostas) =>
  http.post("/api/perfil/questionario", respostas).then((r) => r.data);

// ---- Trilha e módulos ----
export const obterTrilha = () => http.get("/api/modulos/trilha").then((r) => r.data);
export const obterModulosConcluidos = () => http.get("/api/modulos/concluidos").then((r) => r.data);
export const obterProgresso = () => http.get("/api/modulos/progresso").then((r) => r.data);
export const concluirModulo = (moduloId) => http.post("/api/modulos/concluir", { moduloId });

// ---- Quiz ----
export const obterPerguntas = (moduloId) => http.get(`/api/quiz/modulo/${moduloId}`).then((r) => r.data);
export const responderPergunta = (perguntaId, alternativaEscolhida) =>
  http.post("/api/quiz/responder", { perguntaId, alternativaEscolhida }).then((r) => r.data);

// ---- Simulador ----
export const listarCenarios = () => http.get("/api/simulador/cenarios").then((r) => r.data);
export const obterCenario = (id) => http.get(`/api/simulador/cenarios/${id}`).then((r) => r.data);
export const executarSimulacao = (cenarioId, opcaoId) =>
  http.post("/api/simulador/executar", { cenarioId, opcaoId }).then((r) => r.data);
export const listarHistoricoSimulacao = () => http.get("/api/simulador/historico").then((r) => r.data);

// ---- Diagnóstico por IA ----
export const gerarDiagnostico = () =>
  http.post("/api/ia/diagnostico/gerar", { autorizaEnvioIa: true }).then((r) => r.data);
export const obterUltimoDiagnostico = () => http.get("/api/ia/diagnostico/ultimo").then((r) => r.data);
