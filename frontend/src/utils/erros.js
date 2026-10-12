// Transforma o erro do axios em uma frase para o usuário.
// O backend responde sempre no formato { timestamp, status, mensagem, erros? }.
export function mensagemDeErro(erro) {
  if (!erro?.response) {
    return "Não foi possível conectar ao servidor. Verifique se o backend está rodando.";
  }

  const dados = erro.response.data;

  if (dados?.erros && typeof dados.erros === "object") {
    return Object.values(dados.erros).join(" ");
  }
  if (dados?.mensagem) {
    return dados.mensagem;
  }
  if (erro.response.status === 401) {
    return "Sua sessão expirou. Entre novamente.";
  }
  return "Ocorreu um erro inesperado. Tente novamente.";
}
