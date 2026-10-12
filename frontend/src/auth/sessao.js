// Guarda e lê a sessão (token + nome + e-mail) no localStorage.
// Limitação conhecida: o localStorage fica exposto a ataques XSS. Para um TCC é uma escolha simples e aceitável;
// em produção, o ideal seria cookie HttpOnly emitido pelo backend.
const CHAVE = "mapa.sessao";

export function lerSessao() {
  try {
    const texto = localStorage.getItem(CHAVE);
    return texto ? JSON.parse(texto) : null;
  } catch {
    return null;
  }
}

export function salvarSessao(sessao) {
  localStorage.setItem(CHAVE, JSON.stringify(sessao));
}

export function limparSessao() {
  localStorage.removeItem(CHAVE);
}
