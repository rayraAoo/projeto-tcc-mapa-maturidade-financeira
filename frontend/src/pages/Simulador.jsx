import { Link } from "react-router-dom";
import { listarCenarios, listarHistoricoSimulacao } from "../api/servicos.js";
import { NOME_CATEGORIA } from "../data/questionario.js";
import { useDados } from "../hooks/useDados.js";
import { dataHora, moeda } from "../utils/formato.js";
import Layout from "../components/Layout.jsx";
import { Carregando, MensagemErro } from "../components/Estados.jsx";

export default function Simulador() {
  const { dados, carregando, erro } = useDados(async () => {
    const [cenarios, historico] = await Promise.all([listarCenarios(), listarHistoricoSimulacao()]);
    return { cenarios, historico };
  });

  return (
    <Layout>
      <h1>Simulador de decisões</h1>
      <p className="subtitulo">
        Escolha uma situação do dia a dia, decida o que fazer e veja a consequência. O dinheiro é fictício.
      </p>

      {carregando && <Carregando />}
      <div className="espaco-topo">
        <MensagemErro texto={erro} />
      </div>

      {dados && (
        <>
          <div className="pilha espaco-topo-g">
            {dados.cenarios.map((cenario) => (
              <Link to={`/simulador/${cenario.id}`} className="card" key={cenario.id} style={{ textDecoration: "none", color: "inherit" }}>
                <span className="tag">{NOME_CATEGORIA[cenario.categoria] || cenario.categoria}</span>
                <h3 className="espaco-topo">{cenario.titulo}</h3>
                <p className="subtitulo">Saldo inicial: {moeda(cenario.saldoInicial)}</p>
              </Link>
            ))}
          </div>

          {dados.historico.length > 0 && (
            <section className="espaco-topo-g">
              <p className="rotulo-secao">Suas últimas decisões</p>
              <div className="card espaco-topo">
                {dados.historico.slice(0, 5).map((item) => (
                  <div className="linha-valor" key={item.historicoId}>
                    <span>
                      {item.tituloCenario}
                      <br />
                      <span className="nota">{dataHora(item.dataSimulacao)}</span>
                    </span>
                    <span>{item.pontuacao}/10</span>
                  </div>
                ))}
              </div>
            </section>
          )}
        </>
      )}
    </Layout>
  );
}
