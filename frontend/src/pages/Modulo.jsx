import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { concluirModulo, obterModulosConcluidos, obterTrilha } from "../api/servicos.js";
import { useDados } from "../hooks/useDados.js";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { Carregando, MensagemErro } from "../components/Estados.jsx";

export default function Modulo() {
  const { id } = useParams();
  const [concluindo, setConcluindo] = useState(false);
  const [erroAcao, setErroAcao] = useState("");

  // O backend não tem "buscar módulo por id": usamos a trilha do usuário, que também garante
  // que só módulos da própria trilha sejam abertos.
  const { dados, carregando, erro, recarregar } = useDados(async () => {
    const [trilha, concluidos] = await Promise.all([obterTrilha(), obterModulosConcluidos()]);
    const modulo = trilha.find((m) => String(m.id) === id);
    return { modulo, concluido: modulo ? concluidos.includes(modulo.id) : false };
  }, [id]);

  async function marcarConcluido() {
    setErroAcao("");
    setConcluindo(true);
    try {
      await concluirModulo(dados.modulo.id);
      await recarregar();
    } catch (e) {
      setErroAcao(mensagemDeErro(e));
    } finally {
      setConcluindo(false);
    }
  }

  return (
    <Layout>
      <Link to="/trilha" className="voltar">
        ← Voltar à trilha
      </Link>

      {carregando && <Carregando />}
      <MensagemErro texto={erro} />

      {dados && !dados.modulo && (
        <div className="alerta alerta-info">Este módulo não faz parte da sua trilha.</div>
      )}

      {dados?.modulo && (
        <>
          <span className="tag">Camada {dados.modulo.camada}</span>
          <h1 className="espaco-topo">{dados.modulo.titulo}</h1>

          <div className="card texto-modulo espaco-topo-g">
            {dados.modulo.conteudo
              .split("\n")
              .filter((paragrafo) => paragrafo.trim() !== "")
              .map((paragrafo, posicao) => (
                <p key={posicao}>{paragrafo}</p>
              ))}
          </div>

          <div className="pilha espaco-topo-g">
            <MensagemErro texto={erroAcao} />
            <Link to={`/quiz/${dados.modulo.id}`} className="btn btn-primario">
              Fazer o quiz deste módulo
            </Link>
            {dados.concluido ? (
              <p className="centralizado nota">Módulo concluído ✓</p>
            ) : (
              <button type="button" className="btn btn-secundario" onClick={marcarConcluido} disabled={concluindo}>
                {concluindo ? "Salvando..." : "Marcar como concluído"}
              </button>
            )}
          </div>
        </>
      )}
    </Layout>
  );
}
