import axios from "axios";
import { lerSessao, limparSessao } from "../auth/sessao.js";

// Instância única do axios: todas as telas falam com o backend por aqui.
const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
  timeout: 40000, // o diagnóstico por IA pode levar alguns segundos
});

// Envia o token JWT em toda requisição, quando houver sessão.
http.interceptors.request.use((config) => {
  const sessao = lerSessao();
  if (sessao?.token) {
    config.headers.Authorization = `Bearer ${sessao.token}`;
  }
  return config;
});

// Se o token vencer (401) fora das telas de login/cadastro, volta para o login.
http.interceptors.response.use(
  (resposta) => resposta,
  (erro) => {
    const status = erro.response?.status;
    const url = erro.config?.url || "";
    const ehTelaDeAcesso = url.includes("/api/usuarios/login") || url.includes("/api/usuarios/cadastro");

    if (status === 401 && !ehTelaDeAcesso && lerSessao()) {
      limparSessao();
      window.location.assign("/login?expirou=1");
    }
    return Promise.reject(erro);
  }
);

export default http;
