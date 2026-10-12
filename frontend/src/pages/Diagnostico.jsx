import { useState } from "react";
import { Link } from "react-router-dom";
import { gerarDiagnostico, obterUltimoDiagnostico } from "../api/servicos.js";
import { useDados } from "../hooks/useDados.js";
import { dataHora } from "../utils/formato.js";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { Carregando, MensagemErro } from "../components/Estados.jsx";

export default function Diagnostico() {
  // O backend responde 400 quando ainda não existe diagnóstico: isso não é um erro para a tela.
  const { dados: ultimo, carregando, erro } = useDados(async () => {
    try {
      return await obterUltimoDiagnostico();
    } catch (e) {
      if (e.response?.status === 400) return null;
      throw e;
    }
  });

  const [autoriza, setAutoriza] = useState(false);
  const [gerando, setGerando] = useState(false);
  const [novo, setNovo] = useState(null);
  const [erroAcao, setErroAcao] = useState("");

  const exibido = novo || ultimo;

  async function gerar() {
    setErroAcao("");
    setGerando(true);
    try {
      setNovo(await gerarDiagnostico());
      setAutoriza(false);
    } catch (e) {
      setErroAcao(mensagemDeErro(e));
    } finally {
      setGerando(false);
    }
  }

  return (
    <Layout>
      <h1>Diagnóstico personalizado</h1>
      <p className="subtitulo">
        Um resumo do seu aprendizado: o que você já domina e o que vale revisar, com base no seu perfil, nos quizzes e no simulador.
      </p>

      {carregando && <Carregando />}
      <div className="espaco-topo">
        <MensagemErro texto={erro} />
      </div>

      {exibido && (
        <div className="card espaco-topo-g">
          <span className="tag">{exibido.geradoPorIa ? "Gerado por inteligência artificial" : "Gerado por regras (sem IA)"}</span>
          <p className="texto-corrido espaco-topo">{exibido.texto}</p>
          <p className="nota espaco-topo">{exibido.aviso}</p>
          <p className="nota">Gerado em {dataHora(exibido.dataGeracao)}</p>
        </div>
      )}

      {!carregando && !exibido && !erro && (
        <div className="alerta alerta-info espaco-topo-g">Você ainda não gerou nenhum diagnóstico.</div>
      )}

      <div className="espaco-topo-g">
        <p className="rotulo-secao">{exibido ? "Gerar um novo diagnóstico" : "Gerar meu diagnóstico"}</p>

        <label className="caixa-marcar espaco-topo">
          <input type="checkbox" checked={autoriza} onChange={(e) => setAutoriza(e.target.checked)} />
          <span>
            Autorizo o envio dos meus dados de aprendizagem (perfil, módulos, quizzes e simulador, <strong>sem nome e sem e-mail</strong>)
            a um serviço de inteligência artificial de terceiros para gerar este diagnóstico.{" "}
            <Link to="/responsaveis">Saiba mais</Link>
          </span>
        </label>

        <MensagemErro texto={erroAcao} />
        {erroAcao.includes("questionário") && (
          <p className="nota" style={{ marginBottom: 16 }}>
            <Link to="/questionario">Responder o questionário agora</Link>
          </p>
        )}

        <button type="button" className="btn btn-primario" onClick={gerar} disabled={!autoriza || gerando}>
          {gerando ? "Gerando diagnóstico..." : "Gerar diagnóstico"}
        </button>
      </div>
    </Layout>
  );
}
