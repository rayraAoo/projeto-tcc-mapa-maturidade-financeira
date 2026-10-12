import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";
import {
  obterModulosConcluidos,
  obterPerfilAtual,
  obterProgresso,
  obterTrilha,
} from "../api/servicos.js";
import { TITULO_CAMADA } from "../data/questionario.js";
import { useDados } from "../hooks/useDados.js";
import Layout from "../components/Layout.jsx";
import { IconeCheck } from "../components/Icones.jsx";
import { BarraProgresso, Carregando, MensagemErro } from "../components/Estados.jsx";

export default function Trilha() {
  const { sessao } = useAuth();

  const { dados, carregando, erro } = useDados(async () => {
    // As quatro chamadas são independentes: rodam juntas para a tela abrir mais rápido.
    const [trilha, concluidos, progresso, perfil] = await Promise.all([
      obterTrilha(),
      obterModulosConcluidos(),
      obterProgresso(),
      obterPerfilAtual(),
    ]);
    return { trilha, concluidos, progresso, perfil };
  });

  const primeiroNome = sessao?.nome?.split(" ")[0] || "";

  return (
    <Layout>
      <p className="subtitulo" style={{ marginTop: 0 }}>
        Olá, {primeiroNome}
      </p>
      <h1>Sua trilha</h1>

      {carregando && <Carregando />}
      <div className="espaco-topo">
        <MensagemErro texto={erro} />
      </div>

      {dados && (
        <>
          {!dados.perfil && (
            <div className="alerta alerta-info espaco-topo">
              Você ainda não respondeu o questionário. Enquanto isso, mostramos a trilha básica.{" "}
              <Link to="/questionario">Responder agora</Link>
            </div>
          )}

          <div className="card card-destaque espaco-topo">
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "baseline" }}>
              <span className="rotulo-secao" style={{ color: "#ffffff", opacity: 0.85 }}>
                {dados.perfil ? `Perfil: ${dados.perfil.descricao}` : "Perfil não definido"}
              </span>
              <span style={{ fontFamily: "Fraunces, Georgia, serif", fontSize: 22, fontWeight: 600 }}>
                {Math.round(dados.progresso.percentualConcluido)}%
              </span>
            </div>
            <div className="espaco-topo">
              <BarraProgresso percentual={dados.progresso.percentualConcluido} />
            </div>
            <p className="espaco-topo" style={{ fontSize: 13, opacity: 0.9 }}>
              {dados.progresso.modulosConcluidos} de {dados.progresso.totalModulos} módulos concluídos ·{" "}
              {dados.progresso.xp} XP
            </p>
          </div>

          {[1, 2, 3].map((camada) => {
            const modulos = dados.trilha.filter((m) => m.camada === camada);
            if (modulos.length === 0) return null;

            return (
              <section className="espaco-topo-g" key={camada}>
                <p className="rotulo-secao">{TITULO_CAMADA[camada]}</p>
                <div className="pilha espaco-topo">
                  {modulos.map((modulo) => {
                    const feito = dados.concluidos.includes(modulo.id);
                    return (
                      <Link to={`/modulo/${modulo.id}`} className="linha-modulo" key={modulo.id}>
                        <span className={"bolinha" + (feito ? " feita" : "")}>{feito && <IconeCheck />}</span>
                        <span className="info">
                          <span className="titulo">{modulo.titulo}</span>
                          <br />
                          <span className="status-texto">{feito ? "Concluído" : "A fazer"}</span>
                        </span>
                      </Link>
                    );
                  })}
                </div>
              </section>
            );
          })}
        </>
      )}
    </Layout>
  );
}
