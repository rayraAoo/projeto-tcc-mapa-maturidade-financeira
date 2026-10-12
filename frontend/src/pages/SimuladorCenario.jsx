import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { executarSimulacao, obterCenario } from "../api/servicos.js";
import { useDados } from "../hooks/useDados.js";
import { moeda } from "../utils/formato.js";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { BarraProgresso, Carregando, MensagemErro } from "../components/Estados.jsx";

export default function SimuladorCenario() {
  const { id } = useParams();
  const { dados: cenario, carregando, erro } = useDados(() => obterCenario(id), [id]);

  const [opcaoId, setOpcaoId] = useState(null);
  const [resultado, setResultado] = useState(null);
  const [enviando, setEnviando] = useState(false);
  const [erroAcao, setErroAcao] = useState("");

  async function confirmar() {
    setErroAcao("");
    setEnviando(true);
    try {
      setResultado(await executarSimulacao(cenario.id, opcaoId));
    } catch (e) {
      setErroAcao(mensagemDeErro(e));
    } finally {
      setEnviando(false);
    }
  }

  function tentarDeNovo() {
    setResultado(null);
    setOpcaoId(null);
  }

  return (
    <Layout>
      <Link to="/simulador" className="voltar">
        ← Voltar aos cenários
      </Link>

      {carregando && <Carregando />}
      <MensagemErro texto={erro} />

      {cenario && !resultado && (
        <>
          <h1>{cenario.titulo}</h1>

          <div className="card espaco-topo-g">
            <p className="texto-corrido">{cenario.descricao}</p>
            <p className="espaco-topo rotulo-secao">Saldo inicial</p>
            <p style={{ fontSize: 20, fontWeight: 600 }}>{moeda(cenario.saldoInicial)}</p>
          </div>

          <p className="rotulo-secao espaco-topo-g">O que você faz?</p>
          {/* Os valores de cada opção ficam escondidos de propósito: na vida real não se conhece o resultado antes de decidir. */}
          <div className="pilha espaco-topo">
            {cenario.opcoes.map((opcao) => (
              <button
                key={opcao.id}
                type="button"
                className={"opcao" + (opcaoId === opcao.id ? " selecionada" : "")}
                onClick={() => setOpcaoId(opcao.id)}
              >
                <span>{opcao.descricao}</span>
              </button>
            ))}
          </div>

          <div className="espaco-topo-g">
            <MensagemErro texto={erroAcao} />
            <button type="button" className="btn btn-primario" onClick={confirmar} disabled={!opcaoId || enviando}>
              {enviando ? "Calculando..." : "Confirmar decisão"}
            </button>
          </div>
        </>
      )}

      {cenario && resultado && (
        <>
          <p className="marca">Resultado da sua decisão</p>
          <h1 className="espaco-topo">{resultado.tituloCenario}</h1>

          <div className="card espaco-topo-g">
            <p className="nota">Você escolheu:</p>
            <p>{resultado.opcaoEscolhida}</p>
          </div>

          <div className="card espaco-topo">
            <div className="linha-valor">
              <span>Saldo inicial</span>
              <span>{moeda(resultado.saldoInicial)}</span>
            </div>
            <div className="linha-valor">
              <span>Impacto da decisão</span>
              <span className={resultado.impactoFinanceiro < 0 ? "negativo" : "positivo"}>
                {moeda(resultado.impactoFinanceiro)}
              </span>
            </div>
            <div className="linha-valor">
              <span>Saldo final</span>
              <span className={resultado.saldoFinal < 0 ? "negativo" : ""} style={{ fontWeight: 600 }}>
                {moeda(resultado.saldoFinal)}
              </span>
            </div>
          </div>

          <div className="card espaco-topo">
            <p className="rotulo-secao">Nota da decisão</p>
            <p className="espaco-topo" style={{ fontWeight: 600 }}>
              {resultado.pontuacao} de 10
            </p>
            <div className="espaco-topo">
              <BarraProgresso percentual={resultado.pontuacao * 10} />
            </div>
          </div>

          <div className="alerta alerta-info espaco-topo">
            <strong>O que isso ensina</strong>
            <p className="espaco-topo">{resultado.feedbackEducativo}</p>
          </div>

          <div className="pilha espaco-topo-g">
            <Link to="/simulador" className="btn btn-primario">
              Escolher outro cenário
            </Link>
            <button type="button" className="btn btn-secundario" onClick={tentarDeNovo}>
              Refazer este cenário
            </button>
          </div>
        </>
      )}
    </Layout>
  );
}
