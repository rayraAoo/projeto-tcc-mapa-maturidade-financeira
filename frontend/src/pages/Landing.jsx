import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";
import { obterTermos } from "../api/servicos.js";
import { useDados } from "../hooks/useDados.js";
import Layout from "../components/Layout.jsx";

const PILARES = [
  {
    titulo: "Diagnóstico de perfil",
    texto: "Um questionário rápido identifica seu ponto de partida: Iniciante, Em desenvolvimento ou Investidor.",
  },
  {
    titulo: "Trilha que se adapta a você",
    texto: "Módulos curtos, com quiz e explicação a cada resposta, na ordem certa para o seu perfil.",
  },
  {
    titulo: "Simulador de decisões",
    texto: "Escolha o que fazer em situações do dia a dia e veja a consequência, com dinheiro fictício.",
  },
];

export default function Landing() {
  const { autenticado } = useAuth();
  const { dados: termos } = useDados(() => obterTermos());

  return (
    <Layout comNavegacao={false}>
      <p className="marca">Mapa da Maturidade Financeira</p>

      <h1 className="espaco-topo">Aprenda a cuidar do seu dinheiro, no seu ritmo.</h1>
      <p className="subtitulo">
        Plataforma educacional gratuita de educação financeira para adolescentes e adultos, com inteligência artificial
        para personalizar o seu aprendizado.
      </p>

      <div className="pilha espaco-topo-g">
        {autenticado ? (
          <Link to="/trilha" className="btn btn-primario">
            Continuar minha trilha
          </Link>
        ) : (
          <>
            <Link to="/cadastro" className="btn btn-primario">
              Criar conta
            </Link>
            <Link to="/login" className="btn btn-secundario">
              Entrar
            </Link>
          </>
        )}
      </div>

      <div className="pilha espaco-topo-g">
        {PILARES.map((pilar) => (
          <div className="card" key={pilar.titulo}>
            <h3>{pilar.titulo}</h3>
            <p className="subtitulo">{pilar.texto}</p>
          </div>
        ))}
      </div>

      <p className="nota espaco-topo-g">
        Indicado para {termos?.faixaEtariaIndicada || "12 anos ou mais (adolescentes e adultos)"}. Conteúdo educacional:
        não recomendamos investimentos e não movimentamos dinheiro real.
      </p>
      <p className="nota espaco-topo">
        <Link to="/responsaveis">Para pais e responsáveis</Link>
      </p>
    </Layout>
  );
}
