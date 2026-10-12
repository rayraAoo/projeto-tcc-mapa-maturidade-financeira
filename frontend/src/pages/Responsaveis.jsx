import { Link } from "react-router-dom";
import { obterTermos } from "../api/servicos.js";
import { useDados } from "../hooks/useDados.js";
import Layout from "../components/Layout.jsx";
import { Carregando, MensagemErro } from "../components/Estados.jsx";

// Página pública com as informações que os responsáveis precisam conhecer (texto vem do backend).
export default function Responsaveis() {
  const { dados: termos, carregando, erro } = useDados(() => obterTermos());

  return (
    <Layout comNavegacao={false}>
      <Link to="/" className="voltar">
        ← Voltar
      </Link>

      <h1>Para pais e responsáveis</h1>
      <p className="subtitulo">Entenda como a plataforma funciona e como os dados dos adolescentes são tratados.</p>

      {carregando && <Carregando />}
      <div className="espaco-topo">
        <MensagemErro texto={erro} />
      </div>

      {termos && (
        <>
          <div className="card espaco-topo">
            <p className="rotulo-secao">Faixa etária indicada</p>
            <p className="espaco-topo">{termos.faixaEtariaIndicada}</p>
          </div>

          <div className="card espaco-topo">
            <ul className="lista-simples" style={{ marginTop: 0 }}>
              {termos.informacoesResponsaveis.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </div>

          <p className="nota espaco-topo">Versão do termo de ciência: {termos.versaoTermoCiencia}</p>
        </>
      )}
    </Layout>
  );
}
