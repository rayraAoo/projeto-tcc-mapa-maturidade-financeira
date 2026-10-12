import NavegacaoInferior from "./NavegacaoInferior.jsx";

// Moldura de todas as telas. Telas "de foco" (cadastro, quiz...) escondem a navegação inferior.
export default function Layout({ children, comNavegacao = true }) {
  return (
    <div className="pagina">
      <main className={comNavegacao ? "conteudo" : "conteudo sem-nav"}>{children}</main>
      {comNavegacao && <NavegacaoInferior />}
    </div>
  );
}
