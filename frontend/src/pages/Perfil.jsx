import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { excluirConta, obterPerfilAtual } from "../api/servicos.js";
import { useAuth } from "../auth/AuthContext.jsx";
import { useDados } from "../hooks/useDados.js";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { MensagemErro } from "../components/Estados.jsx";

const PALAVRA_DE_CONFIRMACAO = "EXCLUIR";

export default function Perfil() {
  const { sessao, sair } = useAuth();
  const navegar = useNavigate();
  const { dados: perfil } = useDados(() => obterPerfilAtual());

  const [confirmando, setConfirmando] = useState(false);
  const [palavra, setPalavra] = useState("");
  const [excluindo, setExcluindo] = useState(false);
  const [erro, setErro] = useState("");

  function sairDaConta() {
    // Navega antes de limpar a sessão, para não passar pela tela de login.
    navegar("/", { replace: true });
    sair();
  }

  async function excluir() {
    setErro("");
    setExcluindo(true);
    try {
      await excluirConta();
      navegar("/", { replace: true });
      sair();
    } catch (e) {
      setErro(mensagemDeErro(e));
      setExcluindo(false);
    }
  }

  return (
    <Layout>
      <h1>Seu perfil</h1>

      <div className="card espaco-topo-g">
        <p className="rotulo-secao">Conta</p>
        <p className="espaco-topo" style={{ fontWeight: 600 }}>
          {sessao?.nome}
        </p>
        <p className="subtitulo" style={{ marginTop: 0 }}>
          {sessao?.email}
        </p>
      </div>

      <div className="card espaco-topo">
        <p className="rotulo-secao">Perfil de maturidade</p>
        <p className="espaco-topo" style={{ fontWeight: 600 }}>
          {perfil ? perfil.descricao : "Ainda não definido"}
        </p>
        <p className="espaco-topo">
          <Link to="/questionario">{perfil ? "Refazer o questionário" : "Responder o questionário"}</Link>
        </p>
      </div>

      <p className="nota espaco-topo">
        <Link to="/responsaveis">Informações para pais e responsáveis</Link>
      </p>

      <div className="pilha espaco-topo-g">
        <button type="button" className="btn btn-secundario" onClick={sairDaConta}>
          Sair da conta
        </button>
      </div>

      <section className="espaco-topo-g">
        <p className="rotulo-secao">Privacidade</p>

        {!confirmando ? (
          <button type="button" className="btn btn-perigo espaco-topo" onClick={() => setConfirmando(true)}>
            Excluir minha conta e meus dados
          </button>
        ) : (
          <div className="card espaco-topo">
            <p>
              Isso apaga definitivamente sua conta, seu progresso, suas respostas e seus diagnósticos. Não é possível desfazer.
            </p>
            <div className="campo espaco-topo">
              <label htmlFor="confirmacao">Digite {PALAVRA_DE_CONFIRMACAO} para confirmar</label>
              <input id="confirmacao" type="text" value={palavra} onChange={(e) => setPalavra(e.target.value)} />
            </div>
            <MensagemErro texto={erro} />
            <div className="pilha">
              <button
                type="button"
                className="btn btn-perigo"
                onClick={excluir}
                disabled={palavra !== PALAVRA_DE_CONFIRMACAO || excluindo}
              >
                {excluindo ? "Excluindo..." : "Excluir definitivamente"}
              </button>
              <button type="button" className="btn btn-secundario" onClick={() => setConfirmando(false)} disabled={excluindo}>
                Cancelar
              </button>
            </div>
          </div>
        )}
      </section>
    </Layout>
  );
}
