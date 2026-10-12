import { useState } from "react";
import { Link, useLocation, useNavigate, useSearchParams } from "react-router-dom";
import { entrarNaConta, obterPerfilAtual } from "../api/servicos.js";
import { useAuth } from "../auth/AuthContext.jsx";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { MensagemErro } from "../components/Estados.jsx";

export default function Login() {
  const navegar = useNavigate();
  const local = useLocation();
  const [parametros] = useSearchParams();
  const { entrar } = useAuth();

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  async function enviar(evento) {
    evento.preventDefault();
    setErro("");
    setEnviando(true);
    try {
      const sessao = await entrarNaConta(email, senha);
      entrar({ token: sessao.token, nome: sessao.nome, email: sessao.email });

      // Quem ainda não respondeu o questionário começa por ele.
      const perfil = await obterPerfilAtual();
      navegar(perfil ? local.state?.de || "/trilha" : "/questionario", { replace: true });
    } catch (e) {
      setErro(mensagemDeErro(e));
    } finally {
      setEnviando(false);
    }
  }

  return (
    <Layout comNavegacao={false}>
      <p className="marca">Mapa da Maturidade Financeira</p>

      <h1 className="espaco-topo-g">Bem-vindo de volta</h1>
      <p className="subtitulo">Continue de onde parou na sua trilha de aprendizagem.</p>

      <form className="espaco-topo-g" onSubmit={enviar}>
        {parametros.get("expirou") && (
          <div className="alerta alerta-info" role="status">
            Sua sessão expirou. Entre novamente para continuar.
          </div>
        )}

        <div className="campo">
          <label htmlFor="email">E-mail</label>
          <input id="email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="voce@exemplo.com" required />
        </div>

        <div className="campo">
          <label htmlFor="senha">Senha</label>
          <input id="senha" type="password" value={senha} onChange={(e) => setSenha(e.target.value)} placeholder="Sua senha" required />
        </div>

        <MensagemErro texto={erro} />

        <button type="submit" className="btn btn-primario" disabled={enviando}>
          {enviando ? "Entrando..." : "Entrar"}
        </button>
      </form>

      <p className="centralizado nota espaco-topo-g">
        Não tem conta? <Link to="/cadastro">Cadastre-se</Link>
      </p>
    </Layout>
  );
}
