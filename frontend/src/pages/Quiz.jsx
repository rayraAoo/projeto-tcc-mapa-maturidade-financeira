import { useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { concluirModulo, obterPerguntas, responderPergunta } from "../api/servicos.js";
import { useDados } from "../hooks/useDados.js";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { BarraProgresso, Carregando, MensagemErro } from "../components/Estados.jsx";

export default function Quiz() {
  const { moduloId } = useParams();
  const navegar = useNavigate();

  const { dados: perguntas, carregando, erro } = useDados(() => obterPerguntas(moduloId), [moduloId]);

  const [indice, setIndice] = useState(0);
  const [escolhida, setEscolhida] = useState(null);
  const [resultado, setResultado] = useState(null); // correção devolvida pelo backend
  const [acertos, setAcertos] = useState(0);
  const [finalizado, setFinalizado] = useState(false);
  const [ocupado, setOcupado] = useState(false);
  const [erroAcao, setErroAcao] = useState("");

  function reiniciar() {
    setIndice(0);
    setEscolhida(null);
    setResultado(null);
    setAcertos(0);
    setFinalizado(false);
    setErroAcao("");
  }

  async function responder() {
    setErroAcao("");
    setOcupado(true);
    try {
      const correcao = await responderPergunta(perguntas[indice].id, escolhida);
      setResultado(correcao);
      if (correcao.correta) setAcertos((valor) => valor + 1);
    } catch (e) {
      setErroAcao(mensagemDeErro(e));
    } finally {
      setOcupado(false);
    }
  }

  function proxima() {
    if (indice === perguntas.length - 1) {
      setFinalizado(true);
      return;
    }
    setIndice(indice + 1);
    setEscolhida(null);
    setResultado(null);
  }

  async function concluirEVoltar() {
    setErroAcao("");
    setOcupado(true);
    try {
      await concluirModulo(Number(moduloId));
      navegar("/trilha");
    } catch (e) {
      setErroAcao(mensagemDeErro(e));
      setOcupado(false);
    }
  }

  if (carregando) {
    return (
      <Layout comNavegacao={false}>
        <Carregando />
      </Layout>
    );
  }

  if (erro || !perguntas) {
    return (
      <Layout comNavegacao={false}>
        <Link to={`/modulo/${moduloId}`} className="voltar">
          ← Voltar ao módulo
        </Link>
        <MensagemErro texto={erro} />
      </Layout>
    );
  }

  // ---- módulo sem perguntas ou quiz terminado ----
  if (perguntas.length === 0 || finalizado) {
    return (
      <Layout comNavegacao={false}>
        <p className="marca">Quiz</p>
        <h1 className="espaco-topo">{perguntas.length === 0 ? "Este módulo ainda não tem perguntas" : "Quiz finalizado!"}</h1>

        {perguntas.length > 0 && (
          <div className="card espaco-topo-g centralizado">
            <p className="rotulo-secao">Seu resultado</p>
            <p className="espaco-topo" style={{ fontFamily: "Fraunces, Georgia, serif", fontSize: 32 }}>
              {acertos} de {perguntas.length}
            </p>
            <p className="subtitulo">acertos neste módulo</p>
          </div>
        )}

        <div className="pilha espaco-topo-g">
          <MensagemErro texto={erroAcao} />
          <button type="button" className="btn btn-primario" onClick={concluirEVoltar} disabled={ocupado}>
            {ocupado ? "Salvando..." : "Concluir módulo e voltar à trilha"}
          </button>
          {perguntas.length > 0 && (
            <button type="button" className="btn btn-secundario" onClick={reiniciar}>
              Refazer o quiz
            </button>
          )}
          <Link to={`/modulo/${moduloId}`} className="btn btn-secundario">
            Voltar ao módulo
          </Link>
        </div>
      </Layout>
    );
  }

  // ---- pergunta atual ----
  const pergunta = perguntas[indice];
  const alternativas = [
    ["A", pergunta.alternativaA],
    ["B", pergunta.alternativaB],
    ["C", pergunta.alternativaC],
    ["D", pergunta.alternativaD],
  ];

  function classeDaAlternativa(letra) {
    if (resultado) {
      if (letra === resultado.alternativaCorreta) return "opcao correta";
      if (letra === escolhida) return "opcao errada";
      return "opcao";
    }
    return letra === escolhida ? "opcao selecionada" : "opcao";
  }

  return (
    <Layout comNavegacao={false}>
      <div className="pilha">
        <p className="nota">
          Pergunta {indice + 1} de {perguntas.length}
        </p>
        <BarraProgresso percentual={((indice + (resultado ? 1 : 0)) / perguntas.length) * 100} />
      </div>

      <h2 className="espaco-topo-g">{pergunta.enunciado}</h2>

      <div className="pilha espaco-topo-g">
        {alternativas.map(([letra, texto]) => (
          <button
            key={letra}
            type="button"
            className={classeDaAlternativa(letra)}
            onClick={() => setEscolhida(letra)}
            disabled={Boolean(resultado) || ocupado}
          >
            <span className="letra">{letra}</span>
            <span>{texto}</span>
          </button>
        ))}
      </div>

      {resultado && (
        <div className={"alerta espaco-topo-g " + (resultado.correta ? "alerta-info" : "alerta-erro")} role="status">
          <strong>{resultado.correta ? "Resposta correta!" : `Não foi dessa vez. A correta é a ${resultado.alternativaCorreta}.`}</strong>
          <p className="espaco-topo">{resultado.explicacao}</p>
        </div>
      )}

      <div className="espaco-topo-g">
        <MensagemErro texto={erroAcao} />
        {resultado ? (
          <button type="button" className="btn btn-primario" onClick={proxima}>
            {indice === perguntas.length - 1 ? "Ver resultado" : "Próxima pergunta"}
          </button>
        ) : (
          <button type="button" className="btn btn-primario" onClick={responder} disabled={!escolhida || ocupado}>
            {ocupado ? "Corrigindo..." : "Responder"}
          </button>
        )}
      </div>
    </Layout>
  );
}
