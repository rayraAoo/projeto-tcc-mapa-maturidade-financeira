import { createContext, useCallback, useContext, useMemo, useState } from "react";
import { lerSessao, limparSessao, salvarSessao } from "./sessao.js";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [sessao, setSessao] = useState(lerSessao);

  const entrar = useCallback((dados) => {
    // Salva primeiro, para que a próxima requisição já envie o token.
    salvarSessao(dados);
    setSessao(dados);
  }, []);

  const sair = useCallback(() => {
    limparSessao();
    setSessao(null);
  }, []);

  const valor = useMemo(
    () => ({ sessao, autenticado: Boolean(sessao), entrar, sair }),
    [sessao, entrar, sair]
  );

  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
