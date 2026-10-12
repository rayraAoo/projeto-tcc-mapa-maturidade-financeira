import { useState } from "react";
import { Link } from "react-router-dom";
import { enviarQuestionario } from "../api/servicos.js";
import { EXPLICACAO_PERFIL, PERGUNTAS } from "../data/questionario.js";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { BarraProgresso, MensagemErro } from "../components/Estados.jsx";

export default function Questionario() {
  const [indice, setIndice] = useState(0);
  const [respostas, setRespostas] = useState({});
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);
  const [perfil, setPerfil] = useState(null);

  const pergunta = PERGUNTAS[indice];
  const escolhida = respostas[pergunta.campo];
  const ehUltima = indice === PERGUNTAS.length - 1;

  async function avancar() {
    setErro("");
    if (!ehUltima) {
      setIndice(indice + 1);
      return;
    }

    setEnviando(true);
    try {
      setPerfil(await enviarQuestionario(respostas));
    } catch (e) {
      setErro(mensagemDeErro(e));
    } finally {
      setEnviando(false);
    }
  }

  // ---- resultado ----
  if (perfil) {
    return (
      <Layout comNavegacao={false}>
        <p className="marca">Seu perfil de maturidade</p>
        <h1 className="espaco-topo">{perfil.descricao}</h1>
        <p className="subtitulo">
          {EXPLICACAO_PERFIL[perfil.perfil] || "Sua trilha foi montada de acordo com as suas respostas."}
        </p>

        <div className="card espaco-topo-g">
          <p className="rotulo-secao">Pontuação no questionário</p>
          <p className="espaco-topo">{perfil.pontuacaoTotal} de 15 pontos</p>
          <p className="nota espaco-topo">
            O perfil serve apenas para organizar a sua trilha de estudo. Você pode refazer o questionário quando quiser.
          </p>
        </div>

        <Link to="/trilha" className="btn btn-primario espaco-topo-g">
          Ver minha trilha
        </Link>
      </Layout>
    );
  }

  // ---- perguntas ----
  return (
    <Layout comNavegacao={false}>
      <div className="pilha">
        <p className="nota">
          Pergunta {indice + 1} de {PERGUNTAS.length} · {pergunta.tema}
        </p>
        <BarraProgresso percentual={((indice + 1) / PERGUNTAS.length) * 100} />
      </div>

      <h2 className="espaco-topo-g">{pergunta.enunciado}</h2>

      <div className="pilha espaco-topo-g" role="radiogroup" aria-label={pergunta.enunciado}>
        {pergunta.opcoes.map((opcao) => (
          <button
            key={opcao.letra}
            type="button"
            role="radio"
            aria-checked={escolhida === opcao.letra}
            className={"opcao" + (escolhida === opcao.letra ? " selecionada" : "")}
            onClick={() => setRespostas({ ...respostas, [pergunta.campo]: opcao.letra })}
          >
            <span className="letra">{opcao.letra}</span>
            <span>{opcao.texto}</span>
          </button>
        ))}
      </div>

      <div className="espaco-topo-g">
        <MensagemErro texto={erro} />
        <div className="pilha">
          <button type="button" className="btn btn-primario" onClick={avancar} disabled={!escolhida || enviando}>
            {enviando ? "Calculando seu perfil..." : ehUltima ? "Concluir" : "Próxima pergunta"}
          </button>
          {indice > 0 && (
            <button type="button" className="btn btn-secundario" onClick={() => setIndice(indice - 1)} disabled={enviando}>
              Voltar
            </button>
          )}
        </div>
      </div>
    </Layout>
  );
}
