import { NavLink } from "react-router-dom";
import { IconeDiagnostico, IconePerfil, IconeSimulador, IconeTrilha } from "./Icones.jsx";

const ITENS = [
  { para: "/trilha", rotulo: "Trilha", icone: <IconeTrilha /> },
  { para: "/simulador", rotulo: "Simulador", icone: <IconeSimulador /> },
  { para: "/diagnostico", rotulo: "Diagnóstico", icone: <IconeDiagnostico /> },
  { para: "/perfil", rotulo: "Perfil", icone: <IconePerfil /> },
];

export default function NavegacaoInferior() {
  return (
    <nav className="nav-inferior" aria-label="Navegação principal">
      <div className="nav-inferior-itens">
        {ITENS.map((item) => (
          <NavLink
            key={item.para}
            to={item.para}
            className={({ isActive }) => "nav-item" + (isActive ? " ativo" : "")}
          >
            {item.icone}
            <span>{item.rotulo}</span>
          </NavLink>
        ))}
      </div>
    </nav>
  );
}
